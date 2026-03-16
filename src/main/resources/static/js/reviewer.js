$(document).ready(function () {

    protectPage(["REVIEWER"]);

    const token = getToken();

    loadIncidents();

    // Logout
    $("#logoutBtn").click(function () {
        logout();
    });

    function loadIncidents() {

        $.ajax({

            url: "/api/admin/incidents",
            type: "GET",

            headers: {
                "Authorization": "Bearer " + token
            },

            success: function (data) {

                let rows = "";

                data.forEach(function (incident) {

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

        alert(message); // keeping simple here (no modal needed for this page)
    }

});