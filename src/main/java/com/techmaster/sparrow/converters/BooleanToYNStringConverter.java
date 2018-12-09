package com.techmaster.sparrow.converters;

import javax.persistence.AttributeConverter;

/**
 * Converts a Boolean entity attribute to a single-character
 * Y/N string that will be stored in the database, and vice-versa
 *
 * @author jtough
 */
public class BooleanToYNStringConverter implements AttributeConverter<Boolean, String> {

    /**
     * This implementation will return "Y" if the parameter is Boolean.TRUE,
     * otherwise it will return "N" when the parameter is Boolean.FALSE.
     * A null input value will yield a null return value.
     * @param b Boolean
     */
    @Override
    public String convertToDatabaseColumn(Boolean b) {
        if (b == null) {
            return null;
        }
        if (b.booleanValue()) {
            return "Y";
        }
        return "N";
    }

    /**
     * This implementation will return Boolean.TRUE if the string
     * is "Y" or "y", otherwise it will ignore the value and return
     * Boolean.FALSE (it does not actually look for "N") for any
     * other non-null string. A null input value will yield a null
     * return value.
     * @param s String
     */
    @Override
    public Boolean convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        if (s.equals("Y") || s.equals("y")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
