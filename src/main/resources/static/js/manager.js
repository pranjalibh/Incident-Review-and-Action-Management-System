$(document).ready(function () {

    protectPage(["MANAGER"]);

    const token = getToken();

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

    function loadIncidents() {

        $.ajax({
            url: "/api/incidents",
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
            },

            error: function (xhr) {
                if (xhr.status === 401 || xhr.status === 403) {
                    showModal("Session expired. Please login again.");
                    logout();
                }
            }
        });
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

});