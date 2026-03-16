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
            window.location.href = "/reporter.html";
            break;
        case "REVIEWER":
            window.location.href = "/reviewer.html";
            break;
        case "MANAGER":
            window.location.href = "/manager.html";
            break;
        case "USER":
            window.location.href = "/user.html";
            break;
        default:
            window.location.href = "/login.html";
    }
}