(function () {
    'use strict'

    var forms = document.querySelectorAll('.needs-validation')
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }

                // Validar que al menos un rol esté seleccionado
                const roleCheckboxes = document.querySelectorAll('input[name="rolesIds"]');
                let roleSelected = false;
                roleCheckboxes.forEach(checkbox => {
                    if (checkbox.checked) {
                        roleSelected = true;
                    }
                });

                if (!roleSelected) {
                    event.preventDefault();
                    event.stopPropagation();
                    const errorElement = document.querySelector('.error-message[th\\:if="${errorRoles}"]');
                    if (errorElement) {
                        errorElement.textContent = 'Debe seleccionar al menos un rol';
                        errorElement.style.display = 'block';
                    } else {
                        // Crear un elemento de error si no existe
                        const roles = document.querySelector('.roles-container');
                        const errorMsg = document.createElement('div');
                        errorMsg.className = 'error-message';
                        errorMsg.textContent = 'Debe seleccionar al menos un rol';
                        roles.parentNode.insertBefore(errorMsg, roles.nextSibling);
                    }
                }

                form.classList.add('was-validated')
            }, false)
        })

    // Mostrar feedback visual inmediato al interactuar con los campos
    const formInputs = document.querySelectorAll('.form-control, .form-select, .form-check-input');
    formInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.checkValidity()) {
                this.classList.add('is-valid');
                this.classList.remove('is-invalid');
            } else {
                this.classList.add('is-invalid');
                this.classList.remove('is-valid');
            }
        });
    });

    // Validación en tiempo real para el campo de teléfono
    const telefonoInput = document.getElementById('telefono');
    if (telefonoInput) {
        telefonoInput.addEventListener('input', function() {
            // Permitir solo dígitos
            this.value = this.value.replace(/[^0-9]/g, '');

            // Limitar a 10 dígitos
            if (this.value.length > 10) {
                this.value = this.value.slice(0, 10);
            }

            // Validar que comience con 3
            if (this.value.length > 0 && this.value[0] !== '3') {
                this.classList.add('is-invalid');
                const errorMsg = this.nextElementSibling || document.createElement('div');
                errorMsg.className = 'error-message';
                errorMsg.textContent = 'El teléfono debe comenzar con 3';
                if (!this.nextElementSibling) {
                    this.parentNode.insertBefore(errorMsg, this.nextSibling);
                }
            } else if (this.value.length === 10) {
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
                if (this.nextElementSibling && this.nextElementSibling.className === 'error-message') {
                    this.nextElementSibling.remove();
                }
            }
        });
    }

    // Validación para la contraseña
    const contrasenaInput = document.getElementById('contrasena');
    if (contrasenaInput) {
        contrasenaInput.addEventListener('input', function() {
            // Solo validar si hay contenido (puede estar vacío en modo edición)
            if (this.value.length > 0) {
                const hasUpperCase = /[A-Z]/.test(this.value);
                const hasLowerCase = /[a-z]/.test(this.value);
                const hasNumbers = /[0-9]/.test(this.value);
                const isLongEnough = this.value.length >= 8;

                if (hasUpperCase && hasLowerCase && hasNumbers && isLongEnough) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                } else {
                    this.classList.add('is-invalid');
                }
            }
        });
    }
})();