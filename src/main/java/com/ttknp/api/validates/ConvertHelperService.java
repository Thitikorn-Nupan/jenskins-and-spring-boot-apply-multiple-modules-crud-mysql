package com.ttknp.api.validates;

import java.util.List;

public class ConvertHelperService {

    public static String integerToString(Integer integer) {
        if (integer == null) {
            return null;
        }
        return Integer.toString(integer);
    }

    public static Integer stringToInteger(String string) {
        if (string == null) {
            return null;
        }
        return Integer.valueOf(string);
    }

    public static Double stringToDouble(String string) {
        if (string == null) {
            return null;
        }
        return Double.valueOf(string);
    }

    public static Float stringToFloat(String string) {
        if (string == null) {
            return null;
        }
        return Float.parseFloat(string);
    }

    public static <E> E getListFirstElement(List<E> list) {
        if (ValidateHelperService.isNotEmptyList(list)) {
            return list.get(0);
        }
        return null;
    }

    public static <E> E getListLastElement(List<E> list) {
        if (ValidateHelperService.isNotEmptyList(list)) {
            return list.get(list.size() - 1);
        }
        return null;
    }


}
