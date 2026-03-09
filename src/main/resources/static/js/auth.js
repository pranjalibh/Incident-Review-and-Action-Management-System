function saveAuth(token, role) {
    localStorage.setItem("jwt", token);
    localStorage.setItem("role", role);
}

function getToken() {
    return localStorage.getItem("jwt");
}

function getRole() {
    return localStorage.getItem("role");
}

function logout() {
    localStorage.clear();
    window.location.href = "/login.html";
}

function isLoggedIn() {
    return !!getToken();
}