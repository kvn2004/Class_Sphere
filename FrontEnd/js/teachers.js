let token = null;

// Check authentication on page load
window.onload = async () => {
    try {
        const cookie = await cookieStore.get("token");
        if (cookie && cookie.value) {
            token = cookie.value; // store token globally
            console.log("User is authenticated", token);

            // Fetch TEACHERS AFTER token is available
            fetchAndRenderTeachers();
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
    // Show modal on Add Teacher button click
    $("#addTeacherBtn").on("click", function () {
        $("#addTeacherModal").modal("show");
    });

    // Fetch teachers from backend and render cards
    fetchAndRenderTeachers();

    // Handle add teacher form submission 
    $("#addTeacherForm").on("submit", function (e) {
        var teacherData = {
            name: $("#teacherName").val(),
            email: $("#teacherEmail").val(),
            phone: $("#teacherPhone").val(),
            address: $("#teacherAddress").val(),
            subjects: $("#teacherSubjects").val()
        }
        var teacherPhoto = $("#teacherPhoto")[0].files[0];

        var formData = new FormData();
        formData.append("teacher", JSON.stringify(teacherData));
        formData.append("photo", teacherPhoto);



        e.preventDefault();
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/teachers/register",
            data: formData,
            headers: {
                Authorization: "Bearer " + token,
            },
            processData: false,
            contentType: false,
            success: function (response) {
                // Handle success
                console.log("Teacher added successfully:", response);
                fetchAndRenderTeachers(); // Refresh the teacher list

            },
            error: function (xhr, status, error) {
                console.error("Error adding teacher:", error);
            }
        });
        this.reset();
    });

    // View profile button handler
    $(document).on("click", ".view-profile-btn", function () {
        const idx = $(this).data("idx");
        const teacher =
            $(this).data("teacher") ||
            (window._teachersList && window._teachersList[idx]);
        openTeacherProfileModal(teacher);
    });

    // Print button handler
    $(document).on("click", "#printTeacherIdCardBtn", function () {
        $("#printTeacherProfilePhoto").attr(
            "src",
            $("#teacherProfilePhoto").attr("src")
        );
        $("#printTeacherProfileQr").attr("src", $("#teacherProfileQr").attr("src"));
        $("#printTeacherName").text($("#profileTeacherName").val());
        $("#printTeacherId").text($("#profileTeacherId").val());
        $("#printTeacherEmail").text($("#profileTeacherEmail").val());
        $("#printTeacherPhone").text($("#profileTeacherPhone").val());
        $("#printTeacherAddress").text($("#profileTeacherAddress").val());
        var printContents = document.getElementById(
            "printableTeacherIdCard"
        ).innerHTML;
        var originalContents = document.body.innerHTML;
        document.body.innerHTML = printContents;
        window.print();
        document.body.innerHTML = originalContents;
        location.reload();
    });

    // Update and Delete button handlers (demo)
    $(document).on("submit", "#teacherProfileForm", function (e) {
        var teacherUpdateData = {
            id: $("#profileTeacherId").val(),
            name: $("#profileTeacherName").val(),
            email: $("#profileTeacherEmail").val(),
            phone: $("#profileTeacherPhone").val(),
            address: $("#profileTeacherAddress").val(),
            subjects: $("#profileTeacherSubjects").val()
        }
        var teacherPhoto = $("#teacherPhoto")[0].files[0];

        var formUpdateData = new FormData();
        formUpdateData.append("teacher", JSON.stringify(teacherUpdateData));
        formUpdateData.append("photo", teacherPhoto);



        e.preventDefault();
        $.ajax({
            type: "PUT",
            url: "http://localhost:8080/api/teachers/update",
            data: formUpdateData,
            headers: {
                Authorization: "Bearer " + token,
            },
            processData: false,
            contentType: false,
            success: function (response) {
                // Handle success
                console.log("Teacher updated successfully:", response);
                fetchAndRenderTeachers();
                $("#teacherProfileModal").modal("hide");

            },
            error: function (xhr, status, error) {
                console.error("Error updating teacher:", error);
            }
        });
    });
    $(document).on("click", "#deleteTeacherBtn", function () {
        if (confirm("Are you sure you want to delete this teacher?")) {
            alert("Delete teacher (implement AJAX call)");
            $("#teacherProfileModal").modal("hide");
        }
    });
});

function fetchAndRenderTeachers() {
    $.ajax({
        url: "http://localhost:8080/api/teachers/all",
        method: "GET",
        dataType: "json",
        headers: {
            Authorization: "Bearer " + token,
        },
        success: function (res) {
            window._teachersList = res.data;
            renderTeacherCards(res.data);
        },
        error: function () {
            $("#teachersContainer").html(
                '<div class="text-danger">Failed to load teachers.</div>'
            );
        },
    });
}

function renderTeacherCards(teachers) {
    var $container = $("#teachersContainer");
    $container.empty();
    if (!teachers || teachers.length === 0) {
        $container.html('<div class="text-muted">No teachers found.</div>');
        return;
    }
    teachers.forEach(function (teacher, idx) {
        var initials = getInitials(teacher.name);
        var statusBadge =
            teacher.status === "active"
                ? '<span class="badge bg-success">active</span>'
                : '<span class="badge bg-secondary">inactive</span>';
        var subjects = (teacher.subjects || [])
            .map(function (subj) {
                return (
                    '<span class="badge bg-light text-dark border">' + subj + "</span>"
                );
            })
            .join(" ");
        var card = `
            <div class="col-md-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body">
                        <div class="d-flex align-items-center mb-2">
                            <div class="avatar bg-success bg-opacity-10 text-success fw-bold me-3 rounded-circle d-flex align-items-center justify-content-center" style="width:48px;height:48px;font-size:1.2rem;">${initials}</div>
                            <div>
                                <h5 class="card-title mb-0">${teacher.name}</h5>
                                
                            </div>
                        </div>
                        <div class="mb-2">
                            <i class="bi bi-envelope"></i> ${teacher.email}<br>
                            <i class="bi bi-telephone"></i> ${teacher.phone}
                        </div>
                        <div class="mb-2">
                            <strong>Subjects</strong><br>
                            ${subjects}
                        </div>
                        <div class="d-flex gap-2 mt-2">
                            <button class="btn btn-outline-primary btn-sm flex-fill view-profile-btn" data-idx="${idx}">View Profile</button>
                            <button class="btn btn-outline-secondary btn-sm flex-fill">Assign Course</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        $container.append(card);
    });
}

function getInitials(name) {
    if (!name) return "";
    var parts = name.trim().split(" ");
    if (parts.length === 1) return parts[0][0].toUpperCase();
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
}

function openTeacherProfileModal(teacher) {
    $("#profileTeacherId").val(teacher.id || "");
    $("#profileTeacherName").val(teacher.name || "");
    $("#profileTeacherEmail").val(teacher.email || "");
    $("#profileTeacherPhone").val(teacher.phone || "");
    $("#profileTeacherAddress").val(teacher.address || "");
    $("#teacherProfilePhoto").attr(
        "src",
        teacher.photoPath || "https://via.placeholder.com/96?text=Photo"
    );
    $("#teacherProfileQr").attr(
        "src",
        teacher.qrCodePath || "https://via.placeholder.com/96?text=QR"
    );
    $("#teacherProfileForm").data("teacherId", teacher.id);
    $("#teacherProfileModal").modal("show");
}
