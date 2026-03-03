function protectPage(requiredRoles = []) {

    const token = localStorage.getItem("jwt");
    const role = localStorage.getItem("role");

    if (!token) {
        window.location.href = "/login.html";
        return;
    }

    if (requiredRoles.length > 0 && !requiredRoles.includes(role)) {
        alert("Access denied");
        window.location.href = "/login.html";
    }
}

function redirectAfterLogin(role) {

    switch(role) {
        case "ADMIN":
            window.location.href = "/admin.html";
            break;
        case "REPORTER":
            window.location.href = "/incidents.html";
            break;
        case "REVIEWER":
            window.location.href = "/review.html";
            break;
        default:
            window.location.href = "/login.html";
    }
}