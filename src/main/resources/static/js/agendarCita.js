$(document).ready(function() {
    // Actualizar descripción del servicio al seleccionar
    $('#servicioId').change(function() {
        var descripcion = $(this).find('option:selected').data('descripcion');
        if (descripcion) {
            $('#servicioDescripcion').html('<strong>Descripción:</strong> ' + descripcion);
        } else {
            $('#servicioDescripcion').html('');
        }

        // Si ya hay fecha seleccionada, actualizar horarios
        var fecha = $('#fechaSeleccionada').val();
        if (fecha) {
            cargarHorarios(fecha);
        }
    });

    // Cargar horarios al seleccionar fecha
    $('#fechaSeleccionada').change(function() {
        var fecha = $(this).val();
        if (fecha) {
            cargarHorarios(fecha);
            $('#fechaSeleccionInfo').hide();
        } else {
            $('#horariosContainer').html('<div class="col-12 text-center py-4"><p class="text-muted">Seleccione una fecha para ver los horarios disponibles</p></div>');
            $('#fechaSeleccionInfo').show();
        }
    });

    // Funcion para cargar horarios disponibles
    function cargarHorarios(fecha) {
        var servicioId = $('#servicioId').val();

        $.ajax({
            url: '/citas/horarios/' + fecha,
            method: 'GET',
            data: {
                servicioId: servicioId || ''
            },
            success: function(response) {
                renderizarHorarios(response);
            },
            error: function(error) {
                console.error('Error al cargar horarios:', error);
                $('#horariosContainer').html('<div class="col-12"><div class="alert alert-danger">Error al cargar horarios disponibles. Intente nuevamente.</div></div>');
            }
        });
    }

    // Renderizar los horarios en la interfaz
    function renderizarHorarios(data) {
        var html = '';
        var horas = data.horas;
        var disponibilidad = data.disponibilidad;

        if (horas.length === 0) {
            html = '<div class="col-12 text-center py-4"><p class="text-muted">No hay horarios disponibles para esta fecha</p></div>';
        } else {
            // Agrupar horarios por hora (mañana y tarde)
            var horariosMañana = [];
            var horariosTarde = [];

            horas.forEach(function(hora) {
                var hourNum = parseInt(hora.split(':')[0]);
                if (hourNum < 13) {
                    horariosMañana.push(hora);
                } else {
                    horariosTarde.push(hora);
                }
            });

            // Renderizar sección mañana
            html += '<div class="col-md-6"><h6 class="mb-3 fw-bold">Mañana</h6><div class="d-flex flex-wrap">';
            horariosMañana.forEach(function(hora) {
                var disponible = disponibilidad[hora];
                var clasesAdicionales = disponible ? 'available' : 'unavailable';
                var horaFormateada = formatearHora(hora);

                html += '<div class="time-slot ' + clasesAdicionales + '" ' +
                    (disponible ? 'onclick="seleccionarHora(\'' + hora + '\', this)"' : '') +
                    '>' + horaFormateada + '</div>';
            });
            html += '</div></div>';

            // Renderizar sección tarde
            html += '<div class="col-md-6"><h6 class="mb-3 fw-bold">Tarde</h6><div class="d-flex flex-wrap">';
            horariosTarde.forEach(function(hora) {
                var disponible = disponibilidad[hora];
                var clasesAdicionales = disponible ? 'available' : 'unavailable';
                var horaFormateada = formatearHora(hora);

                html += '<div class="time-slot ' + clasesAdicionales + '" ' +
                    (disponible ? 'onclick="seleccionarHora(\'' + hora + '\', this)"' : '') +
                    '>' + horaFormateada + '</div>';
            });
            html += '</div></div>';
        }

        $('#horariosContainer').html(html);
    }

    // Verificar si el formulario está completo para habilitar botón
    function verificarFormulario() {
        var mascota = $('#mascotaId').val();
        var servicio = $('#servicioId').val();
        var fecha = $('#fechaSeleccionada').val();
        var hora = $('#horaSeleccionada').val();

        if (mascota && servicio && fecha && hora) {
            $('#btnReservar').prop('disabled', false);
        } else {
            $('#btnReservar').prop('disabled', true);
        }
    }

    // Funciones de validación para cada campo
    $('#mascotaId, #servicioId, #fechaSeleccionada').change(function() {
        verificarFormulario();
    });

    // Exponer funciones al ámbito global
    window.seleccionarHora = function(hora, elemento) {
        // Limpiar selección anterior
        $('.time-slot').removeClass('selected');

        // Marcar nuevo elemento
        $(elemento).addClass('selected');

        // Guardar valor
        $('#horaSeleccionada').val(hora);

        // Verificar formulario
        verificarFormulario();
    };

    // Función para formatear hora en formato 12h
    window.formatearHora = function(hora24) {
        var partes = hora24.split(':');
        var hora = parseInt(partes[0]);
        var minutos = partes[1];
        var periodo = hora >= 12 ? 'PM' : 'AM';

        // Convertir a formato 12h
        if (hora > 12) hora -= 12;
        if (hora === 0) hora = 12;

        return hora + ':' + minutos + ' ' + periodo;
    };
});