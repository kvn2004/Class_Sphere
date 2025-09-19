let token = null;
let isAuthenticated = false;
let isDomReady = false;
let selectedCourseId = null;
let sessionIdToCourseId = {};

/* ---------------- Utility: Try Load Attendance ---------------- */
function tryLoadAttendanceRecords() {
    if (isAuthenticated && isDomReady) {
        loadAttendanceRecords();
    }
}

/* ---------------- Load Recent Attendance ---------------- */
function loadAttendanceRecords() {
    $.ajax({
        url: 'http://localhost:8080/api/attendance/recent?limit=10',
        method: 'GET',
        dataType: 'json',
        headers: { 'Authorization': `Bearer ${token}` },
        success: function(records) {
            renderAttendanceTable(records);
        },
        error: function(xhr, status, error) {
            const tbody = $('#attendanceTable tbody');
            tbody.empty().append('<tr><td colspan="8" class="text-danger">Error loading attendance records.</td></tr>');
            console.error('Error fetching attendance records:', status, error);
        }
    });
}

/* ---------------- Render Attendance Table ---------------- */
function renderAttendanceTable(records) {
    const tbody = $("#attendanceTable tbody");
    tbody.empty();

    if (!records || records.length === 0) {
        tbody.append("<tr><td colspan='8' class='text-center text-muted'>No attendance records found.</td></tr>");
        return;
    }

    records.forEach(record => {
        let status = (record.status || '').toLowerCase();
        let badgeClass = 'badge-status badge-present';
        let badgeText = 'present';

        if (status === 'absent') {
            badgeClass = 'badge-status badge-absent';
            badgeText = 'absent';
        } else if (status === 'late') {
            badgeClass = 'badge-status badge-late';
            badgeText = 'late';
        }

        let date = '', time = '';
        if (record.markedAt) {
            const dt = new Date(record.markedAt);
            date = dt.toLocaleDateString();
            time = dt.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        }

        tbody.append(`
            <tr>
                <td>${record.studentName || record.studentId || '-'}</td>
                <td>${record.sessionId || '-'}</td>
                <td>${date}</td>
                <td>${time}</td>
                <td><span class="${badgeClass}">${badgeText}</span></td>
                <td>-</td>
                <td>-</td>
                <td><button class="btn btn-outline-secondary btn-sm edit-btn">Edit</button></td>
            </tr>
        `);
    });
}

/* ---------------- Filter Attendance ---------------- */
$('#searchInput, #statusFilter, #dateFilter').on('input change', function () {
    const studentId = $('#searchInput').val().trim();
    const status = $('#statusFilter').val();
    const date = $('#dateFilter').val();

    let queryParams = [];
    if (studentId) queryParams.push("studentId=" + encodeURIComponent(studentId));
    if (status) queryParams.push("status=" + encodeURIComponent(status.toUpperCase()));
    if (date) {
        queryParams.push("startDate=" + date + "T00:00:00");
        queryParams.push("endDate=" + date + "T23:59:59");
    }

    let url = "http://localhost:8080/api/attendance/filter";
    if (queryParams.length > 0) url += "?" + queryParams.join("&");

    $.ajax({
        url: url,
        type: "GET",
        headers: { "Authorization": "Bearer " + token },
        success: function (response) {
            renderAttendanceTable(response.data);
        },
        error: function (xhr) {
            console.error("Error fetching filtered attendance:", xhr);
            $("#attendanceTable tbody").empty().append('<tr><td colspan="8" class="text-danger">Error fetching records.</td></tr>');
        }
    });
});

/* ---------------- Load Sessions ---------------- */
function loadSessionsForDropdown() {
    const select = $('#sessionSelect');
    select.empty().append('<option value="">Select session</option>');
    sessionIdToCourseId = {};

    $.ajax({
        url: 'http://localhost:8080/api/sessions/today',
        method: 'GET',
        dataType: 'json',
        headers: { 'Authorization': `Bearer ${token}` },
        success: function(response) {
            if (response?.data?.length > 0) {
                response.data.forEach(sess => {
                    const display = `${sess.courseTitle} - ${sess.sessionDate} ${sess.startTime.substring(0,5)}`;
                    select.append(`<option value="${sess.sessionId}">${display}</option>`);
                    sessionIdToCourseId[sess.sessionId] = sess.courseId;
                });
            } else {
                select.append('<option disabled>No sessions found</option>');
            }
        },
        error: function() {
            select.append('<option disabled>Error loading sessions</option>');
        }
    });
}

/* ---------------- Load Students for Session ---------------- */
function loadStudentsForSession(sessionId) {
    const courseId = sessionIdToCourseId[sessionId] || null;
    selectedCourseId = courseId;
    const list = $('#studentsList');
    list.empty();

    if (!courseId) {
        list.append('<div class="text-muted">No course found for this session.</div>');
        return;
    }

    $.ajax({
        url: `http://localhost:8080/api/enrollments/course/${courseId}`,
        method: 'GET',
        dataType: 'json',
        headers: { 'Authorization': `Bearer ${token}` },
        success: function(response) {
            if (response?.data?.length > 0) {
                response.data.forEach(stu => {
                    list.append(`
                        <div class="student-row mb-2">
                            <div>
                                <div class="student-name">${stu.studentName}</div>
                                <div class="student-code">${stu.studentId}</div>
                            </div>
                            <select class="form-select form-select-sm status-select" data-student-id="${stu.studentId}">
                                <option value="">Status</option>
                                <option value="present">Present</option>
                                <option value="absent">Absent</option>
                                <option value="late">Late</option>
                            </select>
                        </div>
                    `);
                });
            } else {
                list.append('<div class="text-muted">No students enrolled for this course.</div>');
            }
        },
        error: function(xhr, status, error) {
            console.error('Error loading students:', status, error);
            list.append('<div class="text-danger">Error loading students.</div>');
        }
    });
}

/* ---------------- Page Load ---------------- */
window.onload = async () => {
    try {
        const cookie = await cookieStore.get("token");
        if (cookie?.value) {
            token = cookie.value;
            isAuthenticated = true;
            tryLoadAttendanceRecords();
        } else {
            window.location.href = "../index.html";
        }
    } catch (err) {
        console.error("CookieStore not supported:", err);
        window.location.href = "../index.html";
    }
};

/* ---------------- Document Ready ---------------- */
$(document).ready(function() {
    isDomReady = true;
    tryLoadAttendanceRecords();

    // Dummy stats
    $('#presentToday').text('156');
    $('#absentToday').text('12');
    $('#lateArrivals').text('8');
    $('#totalSessions').text('24');

    // Mark Attendance Modal
    $('#markAttendanceBtn').on('click', function() {
        $('#markAttendanceModal').modal('show');
        loadSessionsForDropdown();
        $('#studentsList').empty();
    });

    $('#sessionSelect').on('change', function() {
        const sessionId = $(this).val();
        loadStudentsForSession(sessionId);
    });

    $('#saveAttendanceBtn').on('click', function() {
        Swal.fire('Attendance Saved!', '', 'success');
        $('#markAttendanceModal').modal('hide');
    });

    // Edit Attendance
    $(document).on('click', '.edit-btn', function() {
        Swal.fire('Edit attendance not implemented yet.');
    });
});
