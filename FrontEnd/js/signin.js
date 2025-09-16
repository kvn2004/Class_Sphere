     // Password visibility toggle
        document.getElementById('loginPasswordToggle').addEventListener('click', function() {
            togglePasswordVisibility('loginPassword', 'loginPasswordToggle');
        });

        function togglePasswordVisibility(inputId, toggleId) {
            const input = document.getElementById(inputId);
            const toggle = document.getElementById(toggleId);
            
            if (input.type === 'password') {
                input.type = 'text';
                toggle.classList.remove('fa-eye');
                toggle.classList.add('fa-eye-slash');
            } else {
                input.type = 'password';
                toggle.classList.remove('fa-eye-slash');
                toggle.classList.add('fa-eye');
            }
        }

       
       
      $("#loginForm").submit(function(e) {
        e.preventDefault();
        const credentials = {
            username: $("#loginUsername").val(),
            password: $("#loginPassword").val()
        };
        console.log(credentials);
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/auth/login",
            data: JSON.stringify(credentials),
            dataType: "json",
            contentType: "application/json",
            xhrFields: {
                withCredentials: true
            },
            success: function(response) {
               cookieStore.set('token', response.token);
               console.log(response.token);

               // Redirect to dashboard after 1 second
               if (response.token) {
                   setTimeout(() => window.location.href = '../pages/dashboard.html', 1000);
               }

            },
            error: function(xhr) {
                alert('Login failed: ' + (xhr.responseJSON.message || 'Unknown error'));
            }
        });
    });


        // Form validation on input
        document.querySelectorAll('input').forEach(input => {
            input.addEventListener('blur', function() {
                validateField(this);
            });
        });

        function validateField(field) {
            const value = field.value.trim();
            
            // Remove existing validation classes
            field.classList.remove('is-valid', 'is-invalid');
            
            if (field.hasAttribute('required') && !value) {
                field.classList.add('is-invalid');
                return false;
            }
            
            // Specific validations
            if (field.type === 'password' && value && value.length < 6) {
                field.classList.add('is-invalid');
                return false;
            }
            
            if (value) {
                field.classList.add('is-valid');
            }
            
            return true;
        }

        // Auto-hide alerts after 5 seconds
        document.addEventListener('DOMContentLoaded', function() {
            setInterval(() => {
                document.querySelectorAll('.alert').forEach(alert => {
                    if (alert.classList.contains('show')) {
                        setTimeout(() => {
                            const closeBtn = alert.querySelector('.btn-close');
                            if (closeBtn) closeBtn.click();
                        }, 5000);
                    }
                });
            }, 1000);
        });