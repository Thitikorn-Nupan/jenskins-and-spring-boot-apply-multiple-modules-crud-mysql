package com.ttknp.api.validates;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateHelperService {
    /**
    Work Too
    public static boolean isEnglishLanguage(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }
        for (char c : text.toCharArray()) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
            if (!(block == Character.UnicodeBlock.BASIC_LATIN ||
                    block == Character.UnicodeBlock.LATIN_1_SUPPLEMENT ||
                    block == Character.UnicodeBlock.GENERAL_PUNCTUATION || // Common punctuation
                    block == Character.UnicodeBlock.SPACING_MODIFIER_LETTERS || // For some specific English characters
                    Character.isWhitespace(c))) { // Allow whitespace
                return false;
            }
        }
        return true;
    }*/
    // *** String Check Language
    public static boolean isEnglishLanguage(String text) {
        String regex = "^[A-Za-z0-9\\s!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isThaiLanguage(String text) {
        String regex = "^[\\u0E00-\\u0E7F0-9\\s!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public static boolean isEmail(String string) {
        String regex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }


    // *** String Check Numeric
    public static boolean isNumeric(String text) {
        // Explanation:
        // ^          - Start of the string (anchor for full string match)
        // [-+]?      - Optional sign (either - or +)
        // (?:        - Non-capturing group for the main number part
        //    \\d+    - One or more digits (for integer part)
        //    (?:\\.\\d*)? - Optional fractional part: a dot followed by zero or more digits
        //    |       - OR
        //    \\.\\d+ - A dot followed by one or more digits (for numbers like .5)
        // )
        // (?:[Ee][-+]?\\d+)? - Optional exponent part: 'e' or 'E', optional sign, one or more digits
        // $          - End of the string (anchor for full string match)
        String regex = "^[-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[Ee][-+]?\\d+)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }


    public static boolean isNotEmptyObject(Object object) {
        String objectString = object.toString();
        return isNotEmptyString(objectString);
    }

    // *** String(s) Check Empty
    public static boolean isNotEmptyString(String text) {
        if (text != null && !text.isEmpty()) {
            text = text.trim();
            return !text.isEmpty();
        }
        return false;
    }

    public static boolean isNotEmptyStringIgnoreSpace(String text) {
        if (text != null && !text.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmptyOnceArrayAndValueStringThenExit(String[] arrayText) {
        if (arrayText != null && arrayText.length > 0) {
            for (String text : arrayText) {
                if (!isNotEmptyString(text)) { // All of elements have to has a value
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static HashMap<String,Integer> isNotEmptyArrayAndValueString(String[] arrayText) {
        HashMap<String,Integer> hashMap = new HashMap<>();
        int totalEmpty = 0;
        if (arrayText != null && arrayText.length > 0) {
            for (String text : arrayText) {
                if (!isNotEmptyString(text)) {
                    totalEmpty++;
                }
            }
        }
        hashMap.put("totalEmpty", totalEmpty);
        return hashMap;
    }


    public static boolean isStringEqual(String text,String compareText) {
        return compareText.equals(text);
    }


    // ** List And Array Generic Check Empty
    public static <T> boolean isNotEmptyList(List<T> list) {
        return (list != null && !list.isEmpty());
    }

    public static <T> boolean isNotEmptyArray(T[] array) {
        return (array != null && array.length > 0);
    }


    // *** Check Types
    public static boolean isBooleanType(Object object) {
        if (object == null) {
            return false;
        }
        if (!isNotEmptyString(object.toString())) {
            return false;
        }
        try {
            return object instanceof Boolean;
        } catch (IllegalArgumentException | NullPointerException e ) {
            return false;
        }
    }

    public static boolean isNumericType(Object object) {
        if (object == null) {
            return false;
        }
        String string = object.toString();
        if (!isNotEmptyString(string)) {
            return false;
        }
        try {
            Double.parseDouble(string); // Float , Double , Int ,.... can verify is number or not
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
