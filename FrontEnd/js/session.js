let token = null;

// Check authentication on page load
window.onload = async () => {
    try {
        const cookie = await cookieStore.get("token");
        if (cookie && cookie.value) {
            token = cookie.value; // store token globally
            console.log("User is authenticated", token);

            // Fetch TEACHERS AFTER token is available
            fetchHalls();
            fetchCourses();
            fetchRecentSessions();
            renderWeeklySchedule();
        } else {
            console.log("User is not authenticated");
            window.location.href = "../index.html";
        }
    } catch (err) {
        console.error("CookieStore not supported:", err);
        window.location.href = "../index.html";
    }
};
$(document).ready(function () {


    // Show modal for scheduling session
    $('#scheduleSessionBtn').on('click', function () {
        $('#sessionForm')[0].reset();
        $('#sessionModal').modal('show');
    });

    // Handle form submit
    $('#sessionForm').on('submit', function (e) {
        e.preventDefault();
        var data = {
            courseId: $('#sessionCourse').val(),
            sessionDate: $('#sessionDate').val(),
            startTime: $('#sessionStartTime').val(),
            endTime: $('#sessionEndTime').val(),
            hallId: $('#sessionHall').val(),
            notes: $('#sessionNotes').val()
        };
        $.ajax({
            url: 'http://localhost:8080/api/sessions/save',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function () {
                $('#sessionModal').modal('hide');
                fetchRecentSessions();
                Swal.fire('Success', 'Session scheduled!', 'success');
            },
            error: function () {
                Swal.fire('Error', 'Failed to schedule session.', 'error');
            }
        });
    });
});

function fetchHalls() {
    $.ajax({
        url: 'http://localhost:8080/api/halls',
        method: 'GET',
        dataType: 'json',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        success: function (res) {
            var $hall = $('#sessionHall');
            $hall.empty();
            $hall.append('<option value="">Select hall</option>');
            (res.data || res).forEach(function (hall) {
                $hall.append(`<option value="${hall.hallId}">${hall.hallName}${hall.hallType ? ' (' + hall.hallType + ')' : ''}</option>`);
            });
        }
    });
}

function fetchCourses() {
    $.ajax({
        url: 'http://localhost:8080/api/courses/all',
        method: 'GET',
        dataType: 'json',
        success: function (res) {
            var $course = $('#sessionCourse');
            $course.empty();
            $course.append('<option value="">Select course</option>');
            (res.data || res).forEach(function (course) {
                $course.append(`<option value="${course.courseId}">${course.title}</option>`);
            });
        }
    });
}

function fetchRecentSessions() {
    $.ajax({
        url: 'http://localhost:8080/api/sessions/this-week',
        method: 'GET',
        dataType: 'json',
        success: function (res) {
            renderRecentSessions(res.data || res);
        },
        error: function () {
            $('#recentSessions').html('<div class="text-danger">Failed to load sessions.</div>');
        }
    });
}

function renderRecentSessions(sessions) {
    var $container = $('#recentSessions');
    $container.empty();
    if (!sessions || sessions.length === 0) {
        $container.html('<div class="text-muted">No sessions found.</div>');
        return;
    }
    sessions.forEach(function (session) {
        var statusClass = session.status === 'completed' ? 'secondary' : (session.status === 'ongoing' ? 'success' : 'primary');
        var statusText = session.status ? session.status.charAt(0).toUpperCase() + session.status.slice(1) : '';
        var card = `
            <div class="mb-3 p-3 bg-light rounded-3 d-flex flex-column flex-md-row align-items-md-center justify-content-between">
                <div>
                    <div class="fw-bold fs-5 mb-1">${session.courseId || ''}</div>
                    <div class="text-muted mb-1">${session.teacherName || ''}</div>
                    <div class="d-flex flex-wrap gap-3 small text-muted">
                        <span><i class="bi bi-calendar"></i> ${session.sessionDate || session.date || ''}</span>
                        <span><i class="bi bi-clock"></i> ${session.startTime || ''} - ${session.endTime || ''}</span>
                        <span><i class="bi bi-geo-alt"></i> ${session.hallName || ''}${session.hallType ? ' (' + session.hallType + ')' : ''}</span>
                        <span><i class="bi bi-people"></i> ${session.studentCount || ''}</span>
                    </div>
                </div>
                <div class="d-flex align-items-center gap-2 mt-2 mt-md-0">
                    <span class="badge bg-${statusClass} text-capitalize">${statusText}</span>
                    <button class="btn btn-outline-secondary btn-sm view-session-btn" data-session='${JSON.stringify(session)}'>View</button>
                </div>
            </div>
        `;
        $container.append(card);
    });
}

// Add the modal HTML to the page if not present
if ($('#viewSessionModal').length === 0) {
    $('body').append(`
    <div class="modal fade" id="viewSessionModal" tabindex="-1" aria-labelledby="viewSessionModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="viewSessionModalLabel">Session Details</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form>
              <div class="mb-3">
                <label class="form-label">Session ID</label>
                <input type="text" class="form-control" id="viewSessionId" readonly>
              </div>
              <div class="mb-3">
                <label class="form-label">Course</label>
                <select class="form-select" id="viewSessionCourse" required>
                  <option value="">Select course</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">Date</label>
                <input type="date" class="form-control" id="viewSessionDate">
              </div>
              <div class="row mb-3">
                <div class="col">
                  <label class="form-label">Start Time</label>
                  <input type="time" class="form-control" id="viewSessionStartTime">
                </div>
                <div class="col">
                  <label class="form-label">End Time</label>
                  <input type="time" class="form-control" id="viewSessionEndTime">
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label">Hall</label>
                <select class="form-select" id="viewSessionHall" required>
                  <option value="">Select hall</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">Notes</label>
                <input type="text" class="form-control" id="viewSessionNotes">
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-danger" id="deleteSessionBtn">Delete</button>
            <button type="button" class="btn btn-primary" id="updateSessionBtn">Update</button>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>
    `);
}

// Helper to populate course dropdown in modal
function populateViewSessionCourseDropdown(selectedCourseId) {
    $.ajax({
        url: 'http://localhost:8080/api/courses/all',
        method: 'GET',
        dataType: 'json',
        success: function (res) {
            var $course = $('#viewSessionCourse');
            $course.empty();
            $course.append('<option value="">Select course</option>');
            (res.data || res).forEach(function (course) {
                $course.append(`<option value="${course.courseId}"${course.courseId === selectedCourseId ? ' selected' : ''}>${course.title}</option>`);
            });
        }
    });
}
// Helper to populate hall dropdown in modal
function populateViewSessionHallDropdown(selectedHallId) {
    $.ajax({
        url: 'http://localhost:8080/api/halls',
        method: 'GET',
        dataType: 'json',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        success: function (res) {
            var $hall = $('#viewSessionHall');
            $hall.empty();
            $hall.append('<option value="">Select hall</option>');
            (res.data || res).forEach(function (hall) {
                $hall.append(`<option value="${hall.hallId}"${hall.hallId === selectedHallId ? ' selected' : ''}>${hall.hallName}${hall.hallType ? ' (' + hall.hallType + ')' : ''}</option>`);
            });
        }
    });
}

// Modal for session details
$(document).on('click', '.view-session-btn', function () {
    var session = $(this).data('session');
    if (typeof session === 'string') session = JSON.parse(session);
    // Populate modal fields
    $('#viewSessionModalLabel').text('Session Details');
    $('#viewSessionId').val(session.sessionId || session.id || '');
    populateViewSessionCourseDropdown(session.courseId || '');
    $('#viewSessionDate').val(session.sessionDate || session.date || '');
    $('#viewSessionStartTime').val(session.startTime || '');
    $('#viewSessionEndTime').val(session.endTime || '');
    populateViewSessionHallDropdown(session.hallId || '');
    $('#viewSessionNotes').val(session.notes || '');
    $('#viewSessionModal').data('sessionId', session.sessionId || session.id);
    $('#viewSessionModal').modal('show');
});

// Delete session
$('#deleteSessionBtn').on('click', function () {
    var sessionId = $('#viewSessionModal').data('sessionId');
    if (!sessionId) return;
    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/api/sessions/${sessionId}`,
                method: 'DELETE',
                success: function () {
                    $('#viewSessionModal').modal('hide');
                    fetchRecentSessions();
                    Swal.fire('Deleted!', 'Session deleted.', 'success');
                },
                error: function () {
                    Swal.fire('Error', 'Failed to delete session.', 'error');
                }
            });
        }
    });
});

// Update session
$('#updateSessionBtn').on('click', function () {
    var sessionId = $('#viewSessionModal').data('sessionId');
    var data = {
        sessionId: sessionId,
        courseId: $('#viewSessionCourse').val(),
        sessionDate: $('#viewSessionDate').val(),
        startTime: $('#viewSessionStartTime').val(),
        endTime: $('#viewSessionEndTime').val(),
        hallId: $('#viewSessionHall').val(),
        notes: $('#viewSessionNotes').val()
    };
    $.ajax({
        url: 'http://localhost:8080/api/sessions/update',
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function () {
            $('#viewSessionModal').modal('hide');
            fetchRecentSessions();
            Swal.fire('Success', 'Session updated!', 'success');
        },
        error: function () {
            Swal.fire('Error', 'Failed to update session.', 'error');
        }
    });
});

function renderWeeklySchedule() {
    // Example: render current week days
    var today = new Date();
    var start = new Date(today);
    start.setDate(today.getDate() - today.getDay()); // Sunday
    var $container = $('#weeklySchedule');
    $container.empty();
    for (var i = 0; i < 7; i++) {
        var d = new Date(start);
        d.setDate(start.getDate() + i);
        var dayName = d.toLocaleDateString('en-US', { weekday: 'short' });
        var dayNum = d.getDate();
        var isActive = d.toDateString() === today.toDateString();
        $container.append(`<div class="day${isActive ? ' active' : ''}">${dayName}<br><span class="small">${dayNum}</span></div>`);
    }
}
