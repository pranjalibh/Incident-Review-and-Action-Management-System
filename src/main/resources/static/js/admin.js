$(document).ready(function () {

    protectPage(["ADMIN"]);

    const token = getToken();

    loadUsers();

    // Logout
    $("#logoutBtn").click(function () {
        logout();
    });

    // Create user
    $("#createUserBtn").click(function () {
        createUser();
    });

    function loadUsers() {

        $.ajax({

            url: "/auth/users",
            type: "GET",

            headers: {
                "Authorization": "Bearer " + token
            },

            success: function (users) {

                let rows = "";

                users.forEach(function (user) {

                    rows += `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.role}</td>
                            <td>${user.createdAt}</td>
                        </tr>
                    `;
                });

                $("#userTableBody").html(rows);
            },

            error: function (xhr) {

                if (xhr.status === 401 || xhr.status === 403) {
                    logout();
                }
            }
        });
    }

    function createUser() {

        const username = $("#username").val().trim();
        const password = $("#password").val().trim();
        const role = $("#role").val();

        // ✅ Validation (important)
        if (!username || !password) {
            showMessage("Username and password required", "danger");
            return;
        }

        $.ajax({

            url: "/auth/users",
            type: "POST",

            headers: {
                "Authorization": "Bearer " + token
            },

            contentType: "application/json",

            data: JSON.stringify({
                username: username,
                password: password,
                role: role
            }),

            success: function () {

                showMessage("User created successfully", "success");

                $("#username").val("");
                $("#password").val("");

                loadUsers();
            },

            error: function () {
                showMessage("Error creating user", "danger");
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