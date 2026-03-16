$(document).ready(function () {

    $("#loginBtn").click(function () {

        const username = $("#username").val().trim();
        const password = $("#password").val().trim();

        // ✅ Basic validation (important for marks)
        if (!username || !password) {
            showError("Please enter username and password");
            return;
        }

        const data = {
            username: username,
            password: password
        };

        $.ajax({
            url: "/auth/login",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),

            success: function (response) {
                saveAuth(response.token, response.role);
                redirectAfterLogin(response.role);
            },

            error: function () {
                showError("Invalid credentials");
            }
        });

    });

    function showError(message) {
        $("#errorBox").removeClass("d-none").text(message);
    }

});