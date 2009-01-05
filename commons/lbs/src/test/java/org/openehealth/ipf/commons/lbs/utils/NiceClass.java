/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.lbs.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A few helpers for tests to ensure that the tested class behaves "nicely"
 * @author Jens Riemschneider
 */
public final class NiceClass {
    private static final Log log = LogFactory.getLog(NiceClass.class);
    
    private NiceClass() {
        throw new UnsupportedOperationException("Don't instantiate utility class");
    }
    
    /**
     * Ensures that {@link Object#toString()} contains expected strings
     * @param obj
     *          object on which to call {@code toString}
     * @param expected
     *          expected strings or objects. If objects are not strings {@link Object#toString()}
     *          will be called on them.
     */
    public static void checkToString(Object obj, Object... expected) {
        String str = obj.toString();
        assertTrue("toString does not contain class name", str.contains(obj.getClass().getSimpleName()));
        for (Object expectedObj : expected) {
            String expectedStr = expectedObj.toString();
            assertTrue("toString does not contain: " + expectedStr, str.contains(expectedStr));
        }
    }
    
    /**
     * Ensures that the class is null safe.
     * <p>
     * Test involves public constructors and methods.
     * @param obj
     *          object to test
     * @param args
     *          dummy arguments of all different parameter types that any of the methods or constructors have
     * @param optional
     *          optional dummy arguments that can also be set to {@code null} without breaking
     *          null-safety
     * @throws any thrown exception that were not expected. Never an {@link IllegalArgumentException}.
     */
    public static void checkNullSafety(Object obj, List<?> args, List<?> optional) throws Exception {
        checkConstructorsAreNullSafe(obj.getClass(), args, optional);
        checkMethodsAreNullSafe(obj, args, optional);
    }
    
    /**
     * Ensures that all methods of a class are null safe.
     * <p>
     * Test involves public methods.
     * @param obj
     *          object to test
     * @param args
     *          dummy arguments of all different parameter types that any of the methods have
     * @param optional
     *          optional dummy arguments that can also be set to {@code null} without breaking
     *          null-safety
     * @throws any thrown exception that were not expected. Never an {@link IllegalArgumentException}.
     */
    public static void checkMethodsAreNullSafe(Object obj, List<?> args, List<?> optional) throws Exception {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                checkMethodIsNullSafe(obj, method, args, optional);
            }
        }
    }
    
    /**
     * Ensures that a methods of a class is null safe.
     * @param obj
     *          object to test
     * @param args
     *          dummy arguments of all different parameter types that the methods has
     * @param optional
     *          optional dummy arguments that can also be set to {@code null} without breaking
     *          null-safety
     * @throws any thrown exception that were not expected. Never an {@link IllegalArgumentException}.
     */
    public static void checkMethodIsNullSafe(Object obj, Method method, List<?> args, List<?> optional) throws Exception {
        log.info("checked " + method);
        
        method.setAccessible(true);
        
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameterValues = getParameterValues(parameterTypes, args);
        
        for (int idx = 0; idx < parameterValues.length; ++idx) {
            if (!isOptional(method.getName(), idx, optional, parameterValues[idx])) {
                Object[] parameterValuesWithNull = Arrays.copyOf(parameterValues, parameterValues.length);
                parameterValuesWithNull[idx] = null;
    
                try {
                    method.invoke(obj, parameterValuesWithNull);
                    fail(method + " not null safe");
                } catch (InvocationTargetException e) {
                    assertEquals(method + " not null safe",
                            IllegalArgumentException.class, e.getCause().getClass());
                }
            }
        }
    }

    private static boolean isOptional(String name, int paramIdx, List<?> optional, Object parameterValue) {
        // Use identify not equality -> don't use optional.contains(parameterValue)
        for (Object optionalValue : optional) {
            if (optionalValue == parameterValue) {
                return true;
            }
            
            String paramName = name + ":" + (paramIdx + 1);
            if (paramName.endsWith(optionalValue.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ensures that all constructors of a class are null safe.
     * <p>
     * Test involves public constructors.
     * @param obj
     *          object to test
     * @param args
     *          dummy arguments of all different parameter types that any of the constructors have
     * @param optional
     *          optional dummy arguments that can also be set to {@code null} without breaking
     *          null-safety
     * @throws any thrown exception that were not expected. Never an {@link IllegalArgumentException}.
     */
    public static void checkConstructorsAreNullSafe(Class<?> clazz, List<?> args, List<?> optional) throws Exception {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (Modifier.isPublic(constructor.getModifiers())) {
                checkConstructorIsNullSafe(constructor, args, optional);
            }
        }
    }

    /**
     * Ensures that a constructors of a class is null safe.
     * @param obj
     *          object to test
     * @param args
     *          dummy arguments of all different parameter types that the constructor has
     * @param optional
     *          optional dummy arguments that can also be set to {@code null} without breaking
     *          null-safety
     * @throws any thrown exception that were not expected. Never an {@link IllegalArgumentException}.
     */
    public static void checkConstructorIsNullSafe(Constructor<?> constructor, List<?> args, List<?> optional) throws Exception {
        log.info("checked " + constructor);
        
        constructor.setAccessible(true);
        
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameterValues = getParameterValues(parameterTypes, args);
        
        for (int idx = 0; idx < parameterValues.length; ++idx) {
            if (!isOptional(constructor.getName(), idx, optional, parameterValues[idx])) {
                Object[] parameterValuesWithNull = Arrays.copyOf(parameterValues, parameterValues.length);
                parameterValuesWithNull[idx] = null;
    
                try {
                    constructor.newInstance(parameterValuesWithNull);
                    fail("Should throw an " + IllegalArgumentException.class.getSimpleName());
                } catch (InvocationTargetException e) {
                    assertEquals(IllegalArgumentException.class, e.getCause().getClass());
                }
            }
        }
    }
    
    /**
     * Ensures that a utility class (i.e. a class with only static methods) cannot be 
     * instantiated
     * @param utilityClass
     *          the class to test
     * @throws Exception
     *          any unexpected exception that occurred 
     */
    public static void checkUtilityClass(Class<?> utilityClass) throws Exception {
        Constructor<?>[] constructors = utilityClass.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
        constructors[0].setAccessible(true);
        try {
            constructors[0].newInstance(new Object[] {});
            fail("Expected Exception: " + InvocationTargetException.class.getSimpleName());
        }
        catch (InvocationTargetException e) {
            // That's expected
        }
    }

    private static Object[] getParameterValues(Class<?>[] parameterTypes, List<?> args) {
        Object[] parameterValues = new Object[parameterTypes.length];
        for (int idx = 0; idx < parameterTypes.length; ++idx) {
            Object compatibleArg = getCompatibleArg(parameterTypes[idx], args);
            if (compatibleArg == null) {
                throw new IllegalArgumentException("No compatible argument for parameter type: " + parameterTypes[idx]);
            }
            parameterValues[idx] = compatibleArg;
        }
        return parameterValues;
    }

    private static Object getCompatibleArg(Class<?> parameterClass, List<?> args) {
        for (Object arg : args) {
            if (parameterClass.isAssignableFrom(arg.getClass())) {
                return arg;
            }
        }
        return null;
    }
}
