package org.springframework.yarn;

import java.lang.reflect.Field;

/**
 * Testing utilities.
 * 
 * @author Janne Valkealahti
 * 
 */
public abstract class TestUtils {

    @SuppressWarnings("unchecked")
    public static <T> T readField(String name, Object target) throws Exception {
        Field field = null;
        Class<?> clazz = target.getClass();
        do {
            try {
                field = clazz.getDeclaredField(name);
            } catch (Exception ex) {
            }

            clazz = clazz.getSuperclass();
        } while (field == null && !clazz.equals(Object.class));

        if (field == null)
            throw new IllegalArgumentException("Cannot find field '" + name + "' in the class hierarchy of "
                    + target.getClass());
        field.setAccessible(true);
        return (T) field.get(target);
    }

}
