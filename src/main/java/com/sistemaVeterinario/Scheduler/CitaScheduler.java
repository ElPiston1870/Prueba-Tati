package com.sistemaVeterinario.Scheduler;

import com.sistemaVeterinario.service.CitaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CitaScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CitaScheduler.class);

    @Autowired
    private CitaService citaActualizacionService;

    /**
     * Tarea programada que se ejecuta cada 30 minutos para actualizar
     * el estado de las citas pasadas a "Completada"
     */
    @Scheduled(fixedRate = 30 * 60 * 1000) // 30 minutos en milisegundos
    // O con cron (ejecutar cada 30 minutos): @Scheduled(cron = "0 0/30 * * * *")
    public void actualizarEstadoCitas() {
        logger.info("Iniciando actualización de estado de citas...");

        int citasActualizadas = citaActualizacionService.actualizarCitasPasadas();

        logger.info("Actualización completada. {} citas actualizadas a estado Completada.", citasActualizadas);
    }
}