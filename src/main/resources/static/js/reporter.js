$(document).ready(function () {

    protectPage(["REPORTER"]);

    const token = getToken();

    // Logout
    $("#logoutBtn").click(function () {
        logout();
    });

    // Create incident
    $("#createBtn").click(function () {
        createIncident();
    });

    function createIncident() {

        const title = $("#title").val().trim();
        const description = $("#description").val().trim();
        const severity = $("#severity").val();
        const blameless = $("#blameless").is(":checked");

        // ✅ Validation (important for marks)
        if (!title || !description || !severity) {
            showMessage("Please fill all fields", "danger");
            return;
        }

        const data = {
            title: title,
            description: description,
            severity: severity,
            blameless: blameless
        };

        $.ajax({

            url: "/api/incidents",
            type: "POST",

            headers: {
                "Authorization": "Bearer " + token
            },

            contentType: "application/json",
            data: JSON.stringify(data),

            success: function (response) {

                showMessage(
                    `Incident created successfully (ID: ${response.id})`,
                    "success"
                );

                setTimeout(function () {
                    window.location.href = "/reporter.html";
                }, 1500);
            },

            error: function (xhr) {

                if (xhr.status === 400) {
                    showMessage("Invalid incident details", "danger");
                }
                else if (xhr.status === 401 || xhr.status === 403) {
                    logout();
                }
                else {
                    showMessage("Failed to create incident", "danger");
                }
            }
        });
    }

    function showMessage(message, type) {

        $("#message").html(`
            <div class="alert alert-${type}">
                ${message}
            </div>
        `);
    }

});