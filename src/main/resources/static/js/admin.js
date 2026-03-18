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
                            <td>
                               <button class="btn btn-danger btn-sm delete-user-btn"
                               data-id="${user.id}">
                               Delete
                               </button>
                            </td>
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

    $(document).on("click", ".delete-user-btn", function () {

        const id = $(this).data("id");

        if (!confirm("Are you sure you want to delete this user?")) {
            return;
        }

        deleteUser(id);
    });

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

    function deleteUser(id) {

        $.ajax({
            url: "/auth/users/" + id,
            type: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            },

            success: function () {
                showMessage("User deleted successfully", "success");
                loadUsers();
            },

            error: function (xhr) {

                if (xhr.status === 403) {
                    showMessage("Only admins can delete users", "danger");
                } else if (xhr.status === 404) {
                    showMessage("User not found", "warning");
                } else {
                    showMessage("Error deleting user", "danger");
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