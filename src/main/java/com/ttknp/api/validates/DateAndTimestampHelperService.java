package com.ttknp.api.validates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAndTimestampHelperService {
    /**
     The Formats
     dd-MM-yy	31-01-12
     dd-MM-yyyy	31-01-2012
     MM-dd-yyyy	01-31-2012
     yyyy-MM-dd	2012-01-31
     yyyy-MM-dd HH:mm:ss	2012-01-31 23:59:59
     yyyy-MM-dd HH:mm:ss.SSS	2012-01-31 23:59:59.999
     yyyy-MM-dd HH:mm:ss.SSSZ	2012-01-31 23:59:59.999+0100
     EEEEE MMMMM yyyy HH:mm:ss.SSSZ	Saturday November 2012 10:45:42.720+0100
    */
    private final static Locale LocalDefault = Locale.ENGLISH;
    private final static SimpleDateFormat FormatYYYYMMDD = getParserDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat FormatISO8601 = getParserDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); // 2017-12-31T23:59:59.999+07:00
    private final static SimpleDateFormat FormatYYYYMMDDHH24MISS = getParserDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final static SimpleDateFormat FormatYYYYMMDDHH24MISSmsec = getParserDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private final static SimpleDateFormat FormatHH24MI = getParserDateFormat("HH:mm");
    private final static SimpleDateFormat FormatHH24MISS = getParserDateFormat("HH:mm:ss");
    private final static SimpleDateFormat FormatHH24MISSmsec = getParserDateFormat("HH:mm:ss.SSS");
    private final static SimpleDateFormat FormatDdMMyyyyLocaleUS = new SimpleDateFormat("dd/MM/yyyy", Locale.US);


    // Date ex, Fri Oct 27 14:30:55 ICT 2023
    private static SimpleDateFormat getParserDateFormat(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, LocalDefault);
        dateFormat.setLenient(false);
        return dateFormat;
    }

    public static Date convertStringToDateFormatYYYYMMDD(String text) {
        try {
            return FormatYYYYMMDD.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date convertStringToDateFormatYYYYMMDDHH24MISS(String text) {
        try {
            return FormatYYYYMMDDHH24MISS.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date convertStringToDateFormatYYYYMMDDHH24MISSmsec(String text) {
        try {
            return FormatYYYYMMDDHH24MISSmsec.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date convertStringToDateFormatISO8601(String text) {
        try {
            return FormatISO8601.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date convertStringToDateFormatHH24MISS(String text) {
        try {
            return FormatHH24MISS.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date convertStringToDateFormatHH24MI(String text) {
        try {
            return FormatHH24MI.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date convertStringToDateFormatHH24MISSmsec(String text) {
        try {
            return FormatHH24MISSmsec.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date convertStringToDateFormatDdMMyyyyLocaleUS(String text) {
        try {
            return FormatDdMMyyyyLocaleUS.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date convertStringToDateCustomFormat(String text, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertDateToStringCustomFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    // *** SQL Timestamp ex, 2017-12-31 23:59:59.999
    private static java.sql.Timestamp toTimestamp(SimpleDateFormat format, String text) throws ParseException {
        return new java.sql.Timestamp(format.parse(text).getTime());
    }

    public static java.sql.Timestamp convertStringYYYYMMDDToSQLTimestamp(String text) {
        try {
            return toTimestamp(FormatYYYYMMDD,text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static java.sql.Timestamp convertStringISO8601oSQLTimestamp(String text) {
        try {
            return toTimestamp(FormatISO8601,text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static java.sql.Timestamp convertStringYYYYMMDDHH24MISSSQLTimestamp(String text) {
        try {
            return toTimestamp(FormatYYYYMMDDHH24MISS,text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static java.sql.Timestamp convertStringHH24MISSSQLTimestamp(String text) {
        try {
            return toTimestamp(FormatHH24MISS,text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static java.sql.Timestamp convertStringHH24MISSmsecSQLTimestamp(String text) {
        try {
            return toTimestamp(FormatHH24MISSmsec,text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static java.sql.Timestamp convertStringDdMMyyyyLocaleUSSQLTimestamp(String text) {
        try {
            return toTimestamp(FormatDdMMyyyyLocaleUS,text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static java.sql.Timestamp convertStringHH24MISQLTimestamp(String text) {
        try {
            return toTimestamp(FormatHH24MI,text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static java.sql.Timestamp convertStringYYYYMMDDHH24MISSmsecSQLTimestamp(String text) {
        try {
            return toTimestamp(FormatYYYYMMDDHH24MISSmsec,text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }



}
