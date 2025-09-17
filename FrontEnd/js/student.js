// Print ID Card button handler
$(document).on("click", "#printIdCardBtn", function () {
    // Fill printable card with current profile data
    $("#printProfilePhoto").attr("src", $("#profilePhoto").attr("src"));
    $("#printProfileQr").attr("src", $("#profileQr").attr("src"));
    $("#printName").text($("#profileName").val());
    $("#printId").text($("#profileId").val());
    $("#printEmail").text($("#profileEmail").val());
    $("#printTelephone").text($("#profileTelephone").val());
    $("#printAddress").text($("#profileAddress").val());
    $("#printGuardianName").text($("#profileGuardianName").val());
    $("#printGuardianTelephone").text($("#profileGuardianTelephone").val());
    $("#printGuardianEmail").text($("#profileGuardianEmail").val());

    // Print only the ID card
    var printContents = document.getElementById("printableIdCard").innerHTML;
    var originalContents = document.body.innerHTML;
    document.body.innerHTML = printContents;
    window.print();
    document.body.innerHTML = originalContents;
    location.reload(); // reload to restore event handlers
});
let token = null;

// Check authentication on page load
window.onload = async () => {
    try {
        const cookie = await cookieStore.get("token");
        if (cookie && cookie.value) {
            token = cookie.value; // store token globally
            console.log("User is authenticated", token);

            // Fetch students AFTER token is available
            fetchAndRenderStudents();
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
    // Show modal on Add Student button click
    $("#addStudentBtn").on("click", function () {
        $("#addStudentModal").modal("show");
    });

    // Handle form submission with all fields and photo
    $("#addStudentForm").on("submit", function (e) {
        e.preventDefault();

        // Collect all fields
        var name = $("#studentName").val();
        var email = $("#studentEmail").val();
        var telephone = $("#studentPhone").val();
        var address = $("#studentAddress").val();
        var guardianName = $("#guardianName").val();
        var guardianTelephone = $("#guardianTelephone").val();
        var guardianEmail = $("#guardianEmail").val();

        var photoFile = $("#studentPhoto")[0].files[0];

        var studentData = {
            name: name,
            email: email,
            telephone: telephone,
            address: address,
            guardianName: guardianName,
            guardianTelephone: guardianTelephone,
            guardianEmail: guardianEmail,
        };

        var formData = new FormData();
        formData.append("student", JSON.stringify(studentData)); // ✅ important: name matches @RequestPart("student")

        if (photoFile) {
            formData.append("photo", photoFile);
        }

        $.ajax({
            url: "http://localhost:8080/api/students/register",
            headers: { Authorization: "Bearer " + token },
            method: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                console.log("Student added successfully:", response);
                $("#addStudentModal").modal("hide");
                $("#addStudentForm")[0].reset();
                fetchAndRenderStudents();
            },
            error: function (xhr, status, error) {
                console.error("Failed to add student", xhr.status, xhr.responseText);
                alert("Error " + xhr.status + ": " + xhr.responseText);
            },
        });
    });
});

// Fetch students from backend
function fetchAndRenderStudents() {
    if (!token) return; // make sure token exists

    $.ajax({
        url: "http://localhost:8080/api/students/all",
        headers: {
            Authorization: "Bearer " + token,
        },
        method: "GET",
        dataType: "json",
        success: function (response) {
            renderStudentCards(response.data); // response.data contains students
        },
        error: function () {
            $("#studentsContainer").html(
                '<div class="text-danger">Failed to load students.</div>'
            );
        },
    });
    // End of file

    function renderStudentCards(students) {
        var $container = $("#studentsContainer");
        $container.empty();

        if (!students || students.length === 0) {
            $container.html('<div class="text-muted">No students found.</div>');
            return;
        }

        students.forEach(function (student, idx) {
            var initials = getInitials(student.name);
            var statusBadge =
                student.status === "active"
                    ? '<span class="badge bg-success">active</span>'
                    : '<span class="badge bg-secondary">inactive</span>';

            var courses = (student.courses || [])
                .map(
                    (course) =>
                        `<span class="badge bg-light text-dark border">${course}</span>`
                )
                .join(" ");

            var card = `
            <div class="col-md-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body">
                        <div class="d-flex align-items-center mb-2">
                            <div class="avatar bg-success bg-opacity-10 text-success fw-bold me-3 rounded-circle d-flex align-items-center justify-content-center" style="width:48px;height:48px;font-size:1.2rem;">
                                ${initials}
                            </div>
                            <div>
                                <h5 class="card-title mb-0">${student.name}</h5>
                            </div>
                            <div class="ms-auto">
                                <i class="bi bi-qr-code fs-4"></i>
                            </div>
                        </div>
                        <div class="mb-2">
                            <i class="bi bi-envelope"></i> ${student.email}<br>
                            <i class="bi bi-telephone"></i> ${student.telephone || student.phone
                }
                        </div>
                        <div class="mb-2">
                            <strong>Guardian</strong><br>
                            <i class="bi bi-person"></i> ${student.guardianName
                }<br>
                            <i class="bi bi-telephone"></i> ${student.guardianTelephone || student.guardianPhone
                }
                        </div>
                        <div class="mb-2">
                            <strong>Enrolled Courses</strong><br>
                            ${courses}
                        </div>
                        <div class="d-flex gap-2">
                            <button class="btn btn-outline-primary btn-sm flex-fill view-profile-btn" data-idx="${idx}">View Profile</button>
                            <button class="btn btn-outline-secondary btn-sm flex-fill">Generate QR</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
            $container.append(card);
        });

        // Attach click event for View Profile buttons
        $(".view-profile-btn")
            .off("click")
            .on("click", function () {
                var idx = $(this).data("idx");
                var student = students[idx];
                openStudentProfileModal(student);
            });
    }

    // Open and populate the student profile modal
    function openStudentProfileModal(student) {
        $("#profileId").val(student.id || "");
        $("#profileName").val(student.name || "");
        $("#profileEmail").val(student.email || "");
        $("#profileTelephone").val(student.telephone || "");
        $("#profileAddress").val(student.address || "");
        $("#profileGuardianName").val(student.guardianName || "");
        $("#profileGuardianTelephone").val(student.guardianTelephone || "");
        $("#profileGuardianEmail").val(student.guardianEmail || "");

         // Profile photo
        if (student.photoPath) {
          $("#profilePhoto").attr("src", "http://localhost:8080/" + student.photoPath);
        } else {
          $("#profilePhoto").attr("src", "https://via.placeholder.com/96?text=Photo");
        }

        // QR code
        if (student.qrCodePath) {
          $("#profileQr").attr("src", "http://localhost:8080/" + student.qrCodePath);
        } else {
          $("#profileQr").attr("src", "https://via.placeholder.com/96?text=QR");
        }

        // Store student id for update/delete
        $("#studentProfileForm").data("studentId", student.id);
        $("#studentProfileModal").modal("show");
    }

    // Update and Delete button handlers
    $(document).on("submit", "#studentProfileForm", function (e) {
        e.preventDefault();
        var studentId = $(this).data("studentId");
        var updatedData = {
            id: studentId,
            name: $("#profileName").val(),
            email: $("#profileEmail").val(),
            telephone: $("#profileTelephone").val(),
            address: $("#profileAddress").val(),
            guardianName: $("#profileGuardianName").val(),
            guardianTelephone: $("#profileGuardianTelephone").val(),
            guardianEmail: $("#profileGuardianEmail").val(),
        };
        var updatephotoFile = $("#studentPhoto")[0].files[0];
        var updateformData = new FormData();
        updateformData.append("student", JSON.stringify(updatedData));

        if (updatephotoFile) {
            updateformData.append("photo", updatephotoFile);
        }

        // TODO: Send AJAX PUT/PATCH to update student
        // Send AJAX with FormData
        $.ajax({
            url: "http://localhost:8080/api/students/update",
            type: "PUT",
            headers: {
                Authorization: "Bearer " + token,
            },
            processData: false,
            contentType: false,
            data: updateformData,
            success: function (response) {
                alert("Student " + studentId + " updated successfully.");
                $("#studentProfileModal").modal("hide");
                // Optionally refresh the student list
                location.reload();
            },
            error: function (xhr, status, error) {
                alert("Error updating student " + studentId + ": " + error);
            },
        });
    });

    $(document).on("click", "#deleteStudentBtn", function () {
        var studentId = $("#studentProfileForm").data("studentId");
        Swal.fire({
            title: 'Are you sure?',
            text: 'You won\'t be able to revert this!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: "http://localhost:8080/api/students/" + studentId,
                    type: "DELETE",
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                    success: function (response) {
                        Swal.fire('Deleted!', 'Student ' + studentId + ' deleted successfully.', 'success');
                        $("#studentProfileModal").modal("hide");
                        // Optionally refresh the student list
                        location.reload();
                    },
                    error: function (xhr, status, error) {
                        console.error("Error deleting student:", xhr.responseText);
                        Swal.fire('Error', 'Error deleting student ' + studentId + ': ' + error, 'error');
                    }
                });
            }
        });
    });
}

function getInitials(name) {
    if (!name) return "";
    const parts = name.trim().split(" ");
    return parts.length === 1
        ? parts[0][0].toUpperCase()
        : (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
}
