$(document).ready(function () {

    protectPage(["REVIEWER"]);

    const token = getToken();

    let allIncidents = [];
    let currentPage = 1;
    const rowsPerPage = 5;

    loadIncidents();

    // Logout
    $("#logoutBtn").click(function () {
        logout();
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
                    showMessage("Session expired. Please login again.");
                    logout();
                }
            }
        });
    }

    function renderTable() {

        let rows = "";

        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        const paginatedItems = allIncidents.slice(start, end);

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
                </tr>
            `;
        });

        $("#incidentTableBody").html(rows);
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

    function showMessage(message) {
        alert(message);
    }

});