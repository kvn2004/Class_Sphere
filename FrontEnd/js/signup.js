// Password visibility toggle
document
    .getElementById("registerPasswordToggle")
    .addEventListener("click", function () {
        togglePasswordVisibility("registerPassword", "registerPasswordToggle");
    });

function togglePasswordVisibility(inputId, toggleId) {
    const input = document.getElementById(inputId);
    const toggle = document.getElementById(toggleId);

    if (input.type === "password") {
        input.type = "text";
        toggle.classList.remove("fa-eye");
        toggle.classList.add("fa-eye-slash");
    } else {
        input.type = "password";
        toggle.classList.remove("fa-eye-slash");
        toggle.classList.add("fa-eye");
    }
}

$("#registerForm").submit(function (e) {
    e.preventDefault();
    const details = {
        name: $("#fullName").val(),
        username: $("#registerUsername").val(),
        password: $("#registerPassword").val(),
        role: $("#userRole").val(),
    };
    console.log(`User Details: ${JSON.stringify(details)}`);
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/auth/register",
        data: JSON.stringify(details),
        dataType: "json",
        contentType: "application/json",
        success: function (response) {
            if (response.status === 200) {
                showAlert(
                    "signUpAlert",
                    response.message,
                    "success"
                );
                setTimeout(() => {
                    window.location.href = "../pages/signin_page.html";
                }, 1500);
            } else {
                showAlert("signUpAlert", response.message, "danger");
                console.log(response.message);
            }
        },
    });
});

// Utility Functions
function showAlert(containerId, message, type) {
    const alertContainer = document.getElementById(containerId);
    alertContainer.innerHTML = `
                <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                    <i class="fas ${type === "success"
            ? "fa-check-circle"
            : "fa-exclamation-circle"
        } me-2"></i>
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            `;
}

// Form validation on input
document.querySelectorAll("input, select").forEach((input) => {
    input.addEventListener("blur", function () {
        validateField(this);
    });
});

function validateField(field) {
    const value = field.value.trim();

    // Remove existing validation classes
    field.classList.remove("is-valid", "is-invalid");

    if (field.hasAttribute("required") && !value) {
        field.classList.add("is-invalid");
        return false;
    }

    // Specific validations
    if (field.type === "password" && value && value.length < 6) {
        field.classList.add("is-invalid");
        return false;
    }

    if (value) {
        field.classList.add("is-valid");
    }

    return true;
}

// Auto-hide alerts after 5 seconds
document.addEventListener("DOMContentLoaded", function () {
    setInterval(() => {
        document.querySelectorAll(".alert").forEach((alert) => {
            if (alert.classList.contains("show")) {
                setTimeout(() => {
                    const closeBtn = alert.querySelector(".btn-close");
                    if (closeBtn) closeBtn.click();
                }, 5000);
            }
        });
    }, 1000);
});
