// JavaScript para la página de Mis Citas
$(document).ready(function() {
    // Filtrar citas por mascota
    $('#mascotaFilter').change(function() {
        applyFilters();
    });

    // Filtrar citas por estado
    $('#estadoFilter').change(function() {
        applyFilters();
    });

    // Resetear filtros
    $('#resetFilters').click(function() {
        $('#mascotaFilter').val('');
        $('#estadoFilter').val('');
        applyFilters();
    });

    // Aplicar filtros
    function applyFilters() {
        var mascotaFilter = $('#mascotaFilter').val();
        var estadoFilter = $('#estadoFilter').val();

        // Contar resultados iniciales
        var totalCitas = $('.cita-item').length;

        // Aplicar filtros
        $('.cita-item').each(function() {
            var mascotaId = $(this).data('mascota');
            var estado = $(this).data('estado');

            var showByMascota = !mascotaFilter || mascotaId == mascotaFilter;
            var showByEstado = !estadoFilter || estado == estadoFilter;

            if (showByMascota && showByEstado) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });

        // Mostrar mensaje si no hay resultados
        var visibleItems = $('.cita-item:visible').length;
        if (visibleItems === 0 && totalCitas > 0) {
            if ($('#noResultsMessage').length === 0) {
                $('#citasList').append('<div id="noResultsMessage" class="alert alert-info mt-3"><i class="fas fa-info-circle me-2"></i> No se encontraron citas con los filtros seleccionados</div>');
            }
        } else {
            $('#noResultsMessage').remove();
        }

        // Mostrar contador de resultados
        if (mascotaFilter || estadoFilter) {
            if ($('#resultsCounter').length === 0) {
                $('#citasList').prepend('<div id="resultsCounter" class="mb-3 text-muted"><small>Mostrando ' + visibleItems + ' de ' + totalCitas + ' citas</small></div>');
            } else {
                $('#resultsCounter small').text('Mostrando ' + visibleItems + ' de ' + totalCitas + ' citas');
            }
        } else {
            $('#resultsCounter').remove();
        }
    }
});