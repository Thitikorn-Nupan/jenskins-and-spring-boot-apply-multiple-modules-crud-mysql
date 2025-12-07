package com.ttknp.api.validates;

import java.util.UUID;

public class UtilityStringHelper {

    public static String replaceAll(String string, String find, String replace) {
        if (ValidateHelperService.isNotEmptyString(string) && ValidateHelperService.isNotEmptyString(find) && ValidateHelperService.isNotEmptyStringIgnoreSpace(replace)) {
            int indexScan = 0;
            int indexStart = 0;
            int length = find.length();
            StringBuilder result = new StringBuilder();

            while ((indexScan = string.indexOf(find, indexStart)) != -1) {
                result.append(string.substring(indexStart, indexScan));
                result.append(replace);
                indexStart = indexScan + length;
            }
            result.append(string.substring(indexStart));
            return result.toString();
        }
        return null;
    }

    public static String toUpperCase(String string) {
        if (ValidateHelperService.isNotEmptyString(string)) {
            return string.toUpperCase();
        }
        return null;
    }

    public static String toLowerCase(String string) {
        if (ValidateHelperService.isNotEmptyString(string)) {
            return string.toLowerCase();
        }
        return null;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
