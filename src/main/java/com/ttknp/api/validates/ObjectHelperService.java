package com.ttknp.api.validates;

import java.util.Arrays;
import java.util.List;

public class ObjectHelperService {

    public static List<Object> objectsToObjectLists(Object... objects) {  // *** Object... ex, 1,"1",true,...
        return Arrays.asList(objects);
    }

    public static <T> List<T> objectsToLists(Object[] objects) {
        return (List<T>) Arrays.stream(objects).toList();
    }

    public static <T> Object[] listsToObjects(List<T> list) {
        if (ValidateHelperService.isNotEmptyList(list)) {
            return list.toArray();
        }
        return null;
    }

    // if textOfEnum is not in enumClass will throw error if it success you can check equal as Status.APPROVED == ObjectHelperService.convertStringToExistEnum(Status.class,"APPROVED"))
    public static Enum stringToExistEnum(Class enumClass , String textOfEnum) {
        return Enum.valueOf(enumClass,textOfEnum);
    }
}
