// Apply saved theme on load
$(document).ready(function () {

    if (localStorage.getItem("darkMode") === "true") {
        $("body").addClass("dark-mode");
    }

    $("#darkModeToggle").click(function () {

        $("body").toggleClass("dark-mode");

        const isDark = $("body").hasClass("dark-mode");
        localStorage.setItem("darkMode", isDark);
    });

});