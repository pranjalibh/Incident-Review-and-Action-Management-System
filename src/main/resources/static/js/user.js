$(document).ready(function () {

    protectPage(["REPORTER", "USER"]);

    const token = getToken();
    let completionChart;
    let slaChart;

    let allActions = [];
    let currentPage = 1;
    const rowsPerPage = 5;

    loadMyActions();

    // Logout
    $("#logoutBtn").click(function () {
        logout();
    });

    function loadMyActions() {

        $.ajax({

            url: "/api/incidents/my/actions",
            type: "GET",

            headers: {
                "Authorization": "Bearer " + token
            },

            success: function (actions) {

                allActions = actions;
                currentPage = 1;

                renderTable();
                setupPagination();
            },

            error: function (xhr) {

                if (xhr.status === 401 || xhr.status === 403) {
                    showMessage("Session expired. Please login again.");
                    logout();
                }
            }
        });
    }

    function showMessage(message) {
        alert(message); // keeping simple for this page
    }

    function renderCompletionChart(completed, open) {

        const ctx = document.getElementById("completionChart").getContext("2d");

        if (completionChart) {
            completionChart.destroy();
        }

        completionChart = new Chart(ctx, {
            type: "pie",
            data: {
                labels: ["Completed", "Open"],
                datasets: [{
                    data: [completed, open],
                    backgroundColor: ["#28a745", "#ffc107"]
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

    function renderSlaChart(onTime, overdue) {

        const ctx = document.getElementById("slaChart");
        const message = $("#slaMessage");

        // No open items → show message instead of chart
        if (onTime === 0 && overdue === 0) {

            if (slaChart) {
                slaChart.destroy();
            }

            ctx.style.display = "none";
            message.show();

            return;
        }

        // Otherwise show chart
        ctx.style.display = "block";
        message.hide();

        if (slaChart) {
            slaChart.destroy();
        }

        slaChart = new Chart(ctx.getContext("2d"), {
            type: "pie",
            data: {
                labels: ["On Time", "Overdue"],
                datasets: [{
                    data: [onTime, overdue],
                    backgroundColor: ["#007bff", "#dc3545"]
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

    function renderTable() {

        let rows = "";

        let completedCount = 0;
        let openCount = 0;
        let onTimeCount = 0;
        let overdueCount = 0;

        const now = new Date();

        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        const paginatedItems = allActions.slice(start, end);

        allActions.forEach(function (action) {
            const dueDate = new Date(action.dueDate);

            if (action.completed) {
                completedCount++;
            } else {
                openCount++;

                if (dueDate >= now) {
                    onTimeCount++;
                } else {
                    overdueCount++;
                }
            }
        });

        paginatedItems.forEach(function (action) {

            const statusLabel = action.completed
                ? `<span class="badge bg-success">Completed</span>`
                : `<span class="badge bg-warning text-dark">Open</span>`;

            rows += `
                <tr>
                    <td>${action.id}</td>
                    <td>${action.description}</td>
                    <td>
                        <a class="incident-link"
                           href="/incident-details.html?id=${action.incident.id}">
                            ${action.incident.title}
                        </a>
                    </td>
                    <td>${action.dueDate}</td>
                    <td>${statusLabel}</td>
                </tr>
            `;
        });

        $("#actionTableBody").html(rows);

        renderCompletionChart(completedCount, openCount);
        renderSlaChart(onTimeCount, overdueCount);
    }

    function setupPagination() {

        const pageCount = Math.ceil(allActions.length / rowsPerPage);
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

    $(document).on("click", ".page-link", function (e) {
        e.preventDefault();

        const page = parseInt($(this).text());
        currentPage = page;

        renderTable();
        setupPagination();
    });

});