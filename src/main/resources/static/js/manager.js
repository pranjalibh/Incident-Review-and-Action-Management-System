$(document).ready(function () {

    protectPage(["MANAGER"]);

    const token = getToken();

    let allIncidents = [];
    let currentPage = 1;
    const rowsPerPage = 5;
    let incidentChart;

    loadIncidents();

    // Logout
    $("#logoutBtn").click(function () {
        logout();
    });

    // Toggle switch (event delegation)
    $(document).on("change", ".toggle-switch", function () {
        const id = $(this).data("id");
        toggleBlameless(id);
    });

    // Pagination click
    $(document).on("click", ".page-link", function (e) {
        e.preventDefault();

        const page = parseInt($(this).text());
        currentPage = page;

        renderTable();
        setupPagination();
    });

    function loadIncidents() {

        $.ajax({
            url: "/api/incidents",
            type: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },

            success: function (data) {
                allIncidents = data;
                currentPage = 1;

                renderTable();
                setupPagination();
            },

            error: function (xhr) {
                if (xhr.status === 401 || xhr.status === 403) {
                    showModal("Session expired. Please login again.");
                    logout();
                }
            }
        });
    }

    function renderTable() {

        let rows = "";

        let openCount = 0;
        let closedCount = 0;

        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        const paginatedItems = allIncidents.slice(start, end);

        // Count all incidents for chart
        allIncidents.forEach(function (incident) {
            if (incident.status === "OPEN") {
                openCount++;
            } else if (incident.status === "CLOSED") {
                closedCount++;
            }
        });

        paginatedItems.forEach(function (incident) {

            const blamelessLabel = incident.blameless
                ? `<span class="badge bg-success">ON</span>`
                : `<span class="badge bg-secondary">OFF</span>`;

            rows += `
                <tr>
                    <td>${incident.id}</td>
                    <td>
                        <a class="incident-link" href="/incident-details.html?id=${incident.id}">
                            ${incident.title}
                        </a>
                    </td>
                    <td>${incident.severity}</td>
                    <td>${incident.status}</td>
                    <td>${blamelessLabel}</td>
                    <td>
                        <div class="form-check form-switch">
                            <input class="form-check-input toggle-switch"
                                type="checkbox"
                                data-id="${incident.id}"
                                ${incident.blameless ? "checked" : ""}>
                        </div>
                    </td>
                </tr>
            `;
        });

        $("#incidentTableBody").html(rows);

        renderChart(openCount, closedCount);
    }

    function setupPagination() {

        const pageCount = Math.ceil(allIncidents.length / rowsPerPage);
        let buttons = "";

        for (let i = 1; i <= pageCount; i++) {
            buttons += `
                <li class="page-item ${i === currentPage ? "active" : ""}">
                    <a class="page-link" href="#">${i}</a>
                </li>
            `;
        }

        $("#pagination").html(buttons);
    }

    function toggleBlameless(id) {

        $.ajax({
            url: "/api/incidents/" + id + "/blameless",
            type: "PATCH",
            headers: {
                "Authorization": "Bearer " + token
            },

            success: function () {
                showModal("Blameless mode updated");
                loadIncidents();
            },

            error: function (xhr) {

                if (xhr.status === 403) {
                    showModal("Only managers can toggle blameless mode");
                }

                if (xhr.status === 404) {
                    showModal("Incident not found");
                }
            }
        });
    }

    function showModal(message) {
        $("#modalMessage").text(message);
        const modal = new bootstrap.Modal(document.getElementById("feedbackModal"));
        modal.show();
    }

    function renderChart(openCount, closedCount) {

        const ctx = document.getElementById("incidentChart").getContext("2d");

        if (incidentChart) {
            incidentChart.destroy();
        }

        incidentChart = new Chart(ctx, {
            type: "pie",
            data: {
                labels: ["Open", "Closed"],
                datasets: [{
                    data: [openCount, closedCount],
                    backgroundColor: ["#ffc107", "#28a745"]
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: "bottom"
                    }
                }
            }
        });
    }

});