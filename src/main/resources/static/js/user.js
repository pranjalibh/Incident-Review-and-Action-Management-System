$(document).ready(function () {

    protectPage(["REPORTER", "USER"]);

    const token = getToken();

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

                let rows = "";

                actions.forEach(function (action) {

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

});