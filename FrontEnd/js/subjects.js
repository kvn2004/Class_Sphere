let token = null;

// Check authentication on page load
window.onload = async () => {
    try {
        const cookie = await cookieStore.get("token");
        if (cookie && cookie.value) {
            token = cookie.value; // store token globally
            console.log("User is authenticated", token);

            // Fetch TEACHERS AFTER token is available
            fetchAndRenderSubjects();
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
    $("#addSubjectBtn").on("click", function () {
        $("#subjectId").attr("type", "hidden");
        $("#subjectForm")[0].reset();
        $("#subjectId").val("");
        $("#subjectModalLabel").text("Add Subject");
        $("#subjectModal").modal("show");
    });

    // Save (add/update) subject
    $("#subjectForm").on("submit", function (e) {
        e.preventDefault();
        var id = $("#subjectId").val();
        var name = $("#subjectName").val();
        var gradeLevel = $("#gradeLevel").val();
        var data = { subjectId: id, subjectName: name, gradeLevel: gradeLevel };
        var isUpdate =
            !!id && $("#subjectModalLabel").text().toLowerCase().includes("update");
        var method = isUpdate ? "PUT" : "POST";
        var url = isUpdate
            ? "http://localhost:8080/api/subjects/update"
            : "http://localhost:8080/api/subjects";
        $.ajax({
            url: url,
            method: method,
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (res) {
                $("#subjectModal").modal("hide");
                fetchAndRenderSubjects();
                Swal.fire(
                    "Success",
                    isUpdate ? "Subject updated!" : "Subject added!",
                    "success"
                );
            },
            error: function () {
                Swal.fire("Error", "Failed to save subject.", "error");
            },
        });
    });

    // Edit subject
    $(document).on("click", ".edit-subject-btn", function () {
        $("#subjectId").attr("type", "text");
        // Unhide the label and input if needed
        $("#subjectLabel").show && $("#subjectLabel").show();
        var subject = $(this).data("subject");
        if (typeof subject === "string") subject = JSON.parse(subject);
        // Use the same keys as in renderSubjectCards
        $("#subjectId").val(subject.subjectId || "");
        $("#subjectName").val(subject.subjectName || "");
        $("#gradeLevel").val(subject.gradeLevel || "");
        $("#subjectModalLabel").text("Update Subject");
        $("#subjectModal").modal("show");
    });

    // Delete subject
    $(document).on("click", ".delete-subject-btn", function () {
        var id = $(this).data("id");
        Swal.fire({
            title: "Are you sure?",
            text: "You won't be able to revert this!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Yes, delete it!",
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: `http://localhost:8080/api/subjects/${id}`,
                    method: "DELETE",
                    success: function () {
                        fetchAndRenderSubjects();
                        Swal.fire("Deleted!", "Subject deleted.", "success");
                    },
                    error: function () {
                        Swal.fire("Error", "Failed to delete subject.", "error");
                    },
                });
            }
        });
    });
});

function fetchAndRenderSubjects() {
    $.ajax({
        url: "http://localhost:8080/api/subjects/all",
        method: "GET",
        dataType: "json",
        headers: {
            Authorization: `Bearer ${token}`,
        },
        success: function (res) {
            renderSubjectCards(res.data);
        },
        error: function () {
            $("#subjectsContainer").html(
                '<div class="text-danger">Failed to load subjects.</div>'
            );
        },
    });
}

function renderSubjectCards(subjects) {
    var $container = $("#subjectsContainer");
    $container.empty();
    if (!subjects || subjects.length === 0) {
        $container.html('<div class="text-muted">No subjects found.</div>');
        return;
    }
    subjects.forEach(function (subject) {
        var card = `
            <div class="col-md-4">
                <div class="card subject-card">
                    <div class="card-body">
                        <h2 class="card-title"><b>${subject.subjectName
            }</b></h2>
                        <p class="card-text"><b>Code:</b> ${subject.subjectId
            }</p>
                        <p class="card-text"><b>Grade:</b> ${subject.gradeLevel
            }</p>
                        <div class="d-flex gap-2">
                            <button class="btn btn-outline-primary btn-sm edit-subject-btn" data-subject='${JSON.stringify(
                subject
            )}'>Edit</button>
                            <button class="btn btn-outline-danger btn-sm delete-subject-btn" data-id="${subject.id
            }">Delete</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        $container.append(card);
    });
}
