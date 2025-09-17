let token = null;

// Check authentication on page load
window.onload = async () => {
    try {
        const cookie = await cookieStore.get("token");
        if (cookie && cookie.value) {
            token = cookie.value; // store token globally
            console.log("User is authenticated", token);

            // Fetch TEACHERS AFTER token is available
            fetchAndRenderHalls();
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
    $('#addHallBtn').on('click', function () {
        $('#hallForm')[0].reset();
        $('#hallId').val('');
        $('#hallModalLabel').text('Add Hall');
        $('#hallModal').modal('show');
    });

    // Save (add/update) hall
    $('#hallForm').on('submit', function (e) {
        e.preventDefault();
        var id = $('#hallId').val();
        var data = {
            hallId: id,
            hallName: $('#hallName').val(),
            hallType: $('#hallType').val(),
            capacity: $('#hallCapacity').val()
        };
        var isUpdate = !!id && $('#hallModalLabel').text().toLowerCase().includes('edit');
        var method = isUpdate ? 'PUT' : 'POST';
        var url = isUpdate ? 'http://localhost:8080/api/halls/update' : 'http://localhost:8080/api/halls/save';
        if (!isUpdate) delete data.hallId;
        $.ajax({
            url: url,
            method: method,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (res) {
                $('#hallModal').modal('hide');
                fetchAndRenderHalls();
                Swal.fire('Success', isUpdate ? 'Hall updated!' : 'Hall added!', 'success');
            },
            error: function () {
                Swal.fire('Error', 'Failed to save hall.', 'error');
            }
        });
    });

    // Edit hall
    $(document).on('click', '.edit-hall-btn', function () {
        var hall = $(this).data('hall');
        if (typeof hall === 'string') hall = JSON.parse(hall);
        $('#hallId').val(hall.hallId || '');
        $('#hallName').val(hall.hallName || '');
        $('#hallType').val(hall.hallType || '');
        $('#hallCapacity').val(hall.capacity || '');
        $('#hallModalLabel').text('Edit Hall');
        $('#hallModal').modal('show');
    });

    // Delete hall
    $(document).on('click', '.delete-hall-btn', function () {
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
                    url: `http://localhost:8080/api/halls/${id}`,
                    method: 'DELETE',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    },
                    success: function () {
                        fetchAndRenderHalls();
                        Swal.fire('Deleted!', 'Hall deleted.', 'success');
                    },
                    error: function () {
                        Swal.fire('Error', 'Failed to delete hall.', 'error');
                    }
                });
            }
        });
    });
});

function fetchAndRenderHalls() {
    $.ajax({
        url: 'http://localhost:8080/api/halls',
        method: 'GET',
        dataType: 'json',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        success: function (res) {
            renderHallCards(res.data || res);
        },
        error: function () {
            $('#hallsContainer').html('<div class="text-danger">Failed to load halls.</div>');
        }
    });
}

function renderHallCards(halls) {
    var $container = $('#hallsContainer');
    $container.empty();
    if (!halls || halls.length === 0) {
        $container.html('<div class="text-muted">No halls found.</div>');
        return;
    }
    halls.forEach(function (hall) {
        var card = `
            <div class="col-md-4">
                <div class="card hall-card">
                    <div class="card-body">
                        <h5 class="card-title">${hall.hallName}</h5>
                        <p class="card-text"><b>Type:</b> ${hall.hallType}</p>
                        <p class="card-text"><b>Capacity:</b> ${hall.capacity}</p>
                        <div class="d-flex gap-2">
                            <button class="btn btn-outline-primary btn-sm edit-hall-btn" data-hall='${JSON.stringify(hall)}'>Edit</button>
                            <button class="btn btn-outline-danger btn-sm delete-hall-btn" data-id="${hall.hallId}">Delete</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        $container.append(card);
    });
}
