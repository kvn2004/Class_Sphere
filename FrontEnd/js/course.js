let token = null;

// Check authentication on page load
window.onload = async () => {
    try {
        const cookie = await cookieStore.get("token");
        if (cookie && cookie.value) {
            token = cookie.value; // store token globally
            console.log("User is authenticated", token);


            fetchAndRenderCourses();
            fetchSubjectsAndTeachers();
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


    // Show modal for add
    $('#addCourseBtn').on('click', function () {
        $('#courseForm')[0].reset();
        $('#courseId').val('');
        $('#courseModalLabel').text('Add New Course');
        $('#courseModal').modal('show');
    });

    // Save (add/update) course
    $('#courseForm').on('submit', function (e) {
        e.preventDefault();
        var id = $('#courseId').val();
        var data = {
            courseId: id,
            title: $('#courseTitle').val(),
            subjectId: $('#courseSubject').val(),
            teacherId: $('#courseTeacher').val(),
            defaultHallId: $('#courseHall').val(),
            effectiveDate: $('#effectiveDate').val(),           // yyyy-MM-dd
            startMonth: $('#classStartMonth').val() ,    // convert month → yyyy-MM-01
            monthlyFee: $('#courseFee').val(),
            active: $('#courseStatus').val() === 'active'
        };


        var isUpdate = !!id && $('#courseModalLabel').text().toLowerCase().includes('edit');
        var method = isUpdate ? 'PUT' : 'POST';
        var url = isUpdate ? 'http://localhost:8080/api/courses/update' : 'http://localhost:8080/api/courses/save';
        $.ajax({
            url: url,
            method: method,
            contentType: 'application/json',
            data: JSON.stringify(data),
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (res) {
                $('#courseModal').modal('hide');
                fetchAndRenderCourses();
                Swal.fire('Success', isUpdate ? 'Course updated!' : 'Course added!', 'success');
            },
            error: function () {
                Swal.fire('Error', 'Failed to save course.', 'error');
            }
        });
    });


    // Edit course
    $(document).on('click', '.edit-course-btn', function () {
        var course = $(this).data('course');
        if (typeof course === 'string') course = JSON.parse(course);
        $('#courseId').val(course.courseId || course.id || '');
        $('#courseTitle').val(course.title || '');
        $('#courseSubject').val(course.subjectId || '');
        $('#courseTeacher').val(course.teacherId || '');
        $('#courseHall').val(course.hallId || '');
        $('#effectiveDate').val(course.effectiveDate ? course.effectiveDate.substring(0, 10) : '');
        $('#classStartMonth').val(course.classStartMonth || '');
        $('#courseFee').val(course.fee || '');
        $('#courseStatus').val(course.status || 'active');
        $('#courseModalLabel').text('Edit Course');
        $('#courseModal').modal('show');
    });

    // Delete course
    $(document).on('click', '.delete-course-btn', function () {
        var id = $(this).data('id');
        Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: `http://localhost:8080/api/courses/${id}`,
                    method: 'DELETE',
                    success: function () {
                        fetchAndRenderCourses();
                        Swal.fire('Deleted!', 'Course deleted.', 'success');
                    },
                    error: function () {
                        Swal.fire('Error', 'Failed to delete course.', 'error');
                    }
                });
            }
        });
    });
});

function fetchAndRenderCourses() {
    $.ajax({
        url: 'http://localhost:8080/api/courses/all',
        method: 'GET',
        dataType: 'json',
        success: function (res) {
            renderCourseCards(res.data || res);
        },
        error: function () {
            $('#coursesContainer').html('<div class="text-danger">Failed to load courses.</div>');
        }
    });
}

function renderCourseCards(courses) {
    var $container = $('#coursesContainer');
    $container.empty();
    if (!courses || courses.length === 0) {
        $container.html('<div class="text-muted">No courses found.</div>');
        return;
    }
    courses.forEach(function (course) {
        var statusClass = course.status === 'active' ? 'active' : 'inactive';
        var statusText = course.status === 'active' ? 'Active' : 'Inactive';
        var card = `
            <div class="col-md-4">
                <div class="card course-card">
                    <div class="card-body">
                        <div class="d-flex align-items-center mb-2">
                            <div class="avatar bg-success bg-opacity-10 text-success fw-bold me-3 rounded-circle d-flex align-items-center justify-content-center" style="width:48px;height:48px;font-size:1.2rem;">
                                <i class="bi bi-journal-bookmark" style="font-size:1.5rem;"></i>
                            </div>
                            <div>
                                <h5 class="card-title mb-0">${course.title}</h5>
                                <div class="text-muted small">${course.subjectName || ''}</div>
                            </div>
                            <div class="ms-auto">
                                <span class="course-status ${statusClass}">${statusText}</span>
                            </div>
                        </div>
                        <div class="mb-2 text-muted">${course.description || ''}</div>
                        <div class="mb-2 row">
                            <div class="col-6 small"><b>Teacher:</b><br>${course.teacherName || ''}</div>
                            <div class="col-3 small"><b>Students:</b><br>${course.studentCount || ''}</div>
                            <div class="col-3 small"><b>Fee:</b><br><span class="text-success">₹${course.fee || ''}/month</span></div>
                        </div>
                        <div class="d-flex gap-2 mt-2">
                            <button class="btn btn-outline-primary btn-sm flex-fill edit-course-btn" data-course='${JSON.stringify(course)}'><i class="bi bi-pencil"></i> Edit</button>
                            <button class="btn btn-outline-danger btn-sm flex-fill delete-course-btn" data-id="${course.courseId || course.id}"><i class="bi bi-trash"></i></button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        $container.append(card);
    });
}

function fetchSubjectsAndTeachers() {
    // Populate subjects dropdown
    $.ajax({
        url: 'http://localhost:8080/api/subjects/all',
        method: 'GET',
        dataType: 'json',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        success: function (res) {
            var $subject = $('#courseSubject');
            $subject.empty();
            $subject.append('<option value="">Select subject</option>');
            (res.data || res).forEach(function (subject) {
                $subject.append(`<option value="${subject.subjectId}">${subject.subjectName}</option>`);
            });
        }
    });
    // Populate teachers dropdown
    $.ajax({
        url: 'http://localhost:8080/api/teachers/all',
        method: 'GET',
        dataType: 'json',
        success: function (res) {
            var $teacher = $('#courseTeacher');
            $teacher.empty();
            $teacher.append('<option value="">Select teacher</option>');
            (res.data || res).forEach(function (teacher) {
                $teacher.append(`<option value="${teacher.id}">${teacher.name}</option>`);
            });
        }
    });
    // Populate halls dropdown
    $.ajax({
        url: 'http://localhost:8080/api/halls',
        method: 'GET',
        dataType: 'json',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        success: function (res) {
            var $hall = $('#courseHall');
            $hall.empty();
            $hall.append('<option value="">Select hall</option>');
            (res.data || res).forEach(function (hall) {
                $hall.append(`<option value="${hall.hallId}">${hall.hallName}</option>`);
            });
        }
    });
}
