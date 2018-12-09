package com.techmaster.sparrow.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    public static boolean isSpecialChar(String obj ) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(obj);
        return m.find();
    }

}
