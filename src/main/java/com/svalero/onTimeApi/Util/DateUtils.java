package com.svalero.onTimeApi.Util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final String FORMAT_DATE_PATTERN = "dd-MM-yyyy";

    /**
     * Convierte un String a un LocalDate en base a un patron
     * @param dateStr: Fecha desde un String
     * @return: LocalDate
     */
    public static LocalDate parseLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(FORMAT_DATE_PATTERN));
    }

    /**
     * Convierte un LocalDate a un String según patrón
     * @param localDate
     * @return String
     */
    public static String formatLocalDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(FORMAT_DATE_PATTERN));
    }

    /**
     * Convierte un LocalDate a Date
     * @param localDate
     * @return Date
     */
    public static java.util.Date toDate(LocalDate localDate) {
        return java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**+
     * Convierte un Date a LocalDate
     * @param date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Convierte un sql Date a LocalDate
     * @param date sql
     * @return LocalDate
     */
    public static LocalDate toLocalDate(java.sql.Date date) {
        return toLocalDate(toUtilDate(date));
    }

    /**
     * Convierte un LocalDate a Date Sql
     * @param localDate
     * @return Date Sql
     */
    public static java.sql.Date toSqlDate(LocalDate localDate) {
        return new java.sql.Date(toDate(localDate).getTime());
    }

    /**
     * Convierte un Date a Date sql
     * @param date
     * @return date sql
     */
    public static java.sql.Date toSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * Convierte un Date sql a date java
     * @param date
     * @return date java
     */
    public static java.util.Date toUtilDate(java.sql.Date date) {
        return new java.util.Date(date.getTime());
    }

}
