$(document).ready(function () {

    protectPage(["REPORTER","ADMIN","MANAGER","REVIEWER","USER"]);

    const token = getToken();
    let incidentData = null;

    const params = new URLSearchParams(window.location.search);
    const incidentId = params.get("id");

    loadIncident();
    loadTimeline();
    loadUsers();
    loadActions();

    // Buttons
    $("#backBtn").click(function () {
        window.history.back();
    });

    $("#closeBtn").click(function () {
        closeIncident();
    });

    $("#saveTimelineBtn").click(function () {
        addTimeline();
    });

    $("#saveActionBtn").click(function () {
        createAction();
    });

    // Checkbox event delegation
    $(document).on("change", ".complete-checkbox", function () {
        const id = $(this).data("id");
        completeAction(id);
    });

    /* ================= FUNCTIONS ================= */

    function loadIncident() {
        $.ajax({
            url: "/api/incidents/" + incidentId,
            type: "GET",
            headers: { "Authorization": "Bearer " + token },

            success: function (incident) {
                incidentData = incident;
                $("#incidentTitle").text(incident.title);
                $("#severity").text(incident.severity);
                $("#status").text(incident.status);
            }
        });
    }

    function loadTimeline() {
        $.ajax({
            url: "/api/incidents/" + incidentId + "/timeline",
            type: "GET",
            headers: { "Authorization": "Bearer " + token },

            success: function (events) {

                let items = "";

                events.forEach(function (event) {
                    items += `
                        <div class="timeline-item">
                            <div class="timeline-dot"></div>
                            <div class="timeline-content">
                                <div class="timeline-time">${event.eventTime}</div>
                                <div>${event.description}</div>
                            </div>
                        </div>
                    `;
                });

                $("#timelineList").html(items);
            }
        });
    }

    function addTimeline() {

        const payload = {
            description: $("#description").val(),
            eventTime: $("#eventTime").val()
        };

        $.ajax({
            url: "/api/incidents/" + incidentId + "/timeline",
            type: "POST",
            headers: { "Authorization": "Bearer " + token },
            contentType: "application/json",
            data: JSON.stringify(payload),

            success: function () {
                loadTimeline();
                hideModal("timelineModal");
            }
        });
    }

    function loadUsers() {
        $.ajax({
            url: "/auth/users",
            type: "GET",
            headers: { "Authorization": "Bearer " + token },

            success: function (users) {

                let options = "<option value=''>Select User</option>";

                users.forEach(function (user) {
                    options += `<option value="${user.username}">${user.username}</option>`;
                });

                $("#assignedUser").html(options);
            }
        });
    }

    function loadActions() {
        $.ajax({
            url: "/api/incidents/" + incidentId + "/actions",
            type: "GET",
            headers: { "Authorization": "Bearer " + token },

            success: function (actions) {

                let rows = "";

                actions.forEach(function (action) {

                    rows += `
                        <li class="list-group-item d-flex justify-content-between">

                           <div class="d-flex align-items-center gap-2 flex-wrap">

                               <input type="checkbox"
                                   class="complete-checkbox"
                                   data-id="${action.id}"
                                   ${action.completed ? "checked" : ""}>

                               <span class="action-text">${action.description}</span>

                               ${incidentData && !incidentData.blameless
                               ? `
                                   <span class="assigned-inline">
                                       👤 ${action.assignedTo?.username || "Unassigned"}
                                   </span>
                                 `
                               : ""}

                           </div>

                            <span class="badge bg-warning">
                                Due: ${action.dueDate}
                            </span>

                        </li>
                    `;
                });

                $("#actionList").html(rows);
            }
        });
    }

    function createAction() {

        const payload = {
            description: $("#actionDescription").val(),
            assignedTo: $("#assignedUser").val(),
            dueDate: $("#dueDate").val()
        };

        $.ajax({
            url: "/api/incidents/" + incidentId + "/actions",
            type: "POST",
            headers: { "Authorization": "Bearer " + token },
            contentType: "application/json",
            data: JSON.stringify(payload),

            success: function () {
                loadActions();
                hideModal("actionModal");
            },

            error: function () {
                showModal("Due date should be within SLA");
            }
        });
    }

    function completeAction(id) {
        $.ajax({
            url: "/api/incidents/actions/" + id + "/complete",
            type: "PUT",
            headers: { "Authorization": "Bearer " + token },

            success: function () {
                loadActions();
            }
        });
    }

    function closeIncident() {
        $.ajax({
            url: "/api/incidents/" + incidentId + "/close",
            type: "PATCH",
            headers: { "Authorization": "Bearer " + token },

            success: function () {
                showModal("Incident closed successfully");
                loadIncident();
            },

            error: function (xhr) {
                showModal(xhr.responseText);
            }
        });
    }

    function showModal(message) {
        $("#modalMessage").text(message);
        new bootstrap.Modal(document.getElementById("feedbackModal")).show();
    }

    function hideModal(id) {
        const modal = bootstrap.Modal.getInstance(document.getElementById(id));
        modal.hide();
    }

});