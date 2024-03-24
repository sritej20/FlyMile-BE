package ca.flymile.service;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DateHandler {
    private static final Logger logger = LoggerFactory.getLogger(DateHandler.class);
    private static final long LIMIT_DAYS = 331;
    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Getter
    static LocalDate currentDate;

    @Getter
    static LocalDate limitDate;

    static {
        updateDates();
        scheduleDailyUpdate();
    }

    private static synchronized void updateDates() {
        try {
            currentDate = LocalDate.now();
            limitDate = currentDate.plusDays(LIMIT_DAYS);
            logger.info("Dates updated: current date = {}, limit date = {}", currentDate, limitDate);
        } catch (Exception e) {
            logger.error("Issue with updateDates() in DateHandler", e);
        }
    }

    private static void scheduleDailyUpdate() {
        if (scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
        }
        Runnable updateTask = DateHandler::updateDates;

        long untilMidnight = ChronoUnit.MILLIS.between(LocalDateTime.now(), LocalDate.now().plusDays(1).atStartOfDay());
        long dayInMillis = TimeUnit.DAYS.toMillis(1);

        scheduler.scheduleAtFixedRate(updateTask, untilMidnight, dayInMillis, TimeUnit.MILLISECONDS);
    }
}
