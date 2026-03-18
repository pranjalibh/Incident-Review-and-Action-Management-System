$(document).ready(function () {

    protectPage(["ADMIN"]);

    const token = getToken();

    let allUsers = [];
    let currentPage = 1;
    const rowsPerPage = 5;

    loadUsers();

    // Logout
    $("#logoutBtn").click(function () {
        logout();
    });

    // Create user
    $("#createUserBtn").click(function () {
        createUser();
    });

    // Pagination click
    $(document).on("click", ".page-link", function (e) {
        e.preventDefault();

        const page = parseInt($(this).text());
        currentPage = page;

        renderTable();
        setupPagination();
    });

    function loadUsers() {

        $.ajax({

            url: "/auth/users",
            type: "GET",

            headers: {
                "Authorization": "Bearer " + token
            },

            success: function (users) {

                allUsers = users;
                currentPage = 1;

                renderTable();
                setupPagination();
            },

            error: function (xhr) {

                if (xhr.status === 401 || xhr.status === 403) {
                    logout();
                }
            }
        });
    }

    function renderTable() {

        let rows = "";

        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        const paginatedItems = allUsers.slice(start, end);

        paginatedItems.forEach(function (user) {

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
    }

    function setupPagination() {

        const pageCount = Math.ceil(allUsers.length / rowsPerPage);
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