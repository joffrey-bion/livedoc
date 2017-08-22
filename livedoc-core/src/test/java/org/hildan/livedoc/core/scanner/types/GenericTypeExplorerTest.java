package org.hildan.livedoc.core.scanner.types;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.reflect.TypeToken;
import static org.junit.Assert.assertEquals;

public class GenericTypeExplorerTest {

    private interface MyInterface {
    }

    private static class Custom {
    }

    @SuppressWarnings("unused")
    private static class Custom1P<T> {
    }

    @SuppressWarnings("unused")
    private static class Custom2P<T, U> {
    }

    @SuppressWarnings("unused")
    private static class Custom3P<T, U, V> {
    }

    @Test(expected = IllegalArgumentException.class)
    public void getClassesInDeclaration_failsOnNull() throws NoSuchMethodException {
        GenericTypeExplorer.getClassesInDeclaration(null);
    }

    @Test
    public void getClassesInDeclaration_simpleString() throws NoSuchMethodException {
        check(String.class, String.class);
    }

    @Test
    public void getClassesInDeclaration_simpleArray() throws NoSuchMethodException {
        check(Integer[].class, Integer.class);
    }

    @Test
    public void getClassesInDeclaration_simpleCustom() throws NoSuchMethodException {
        check(Custom.class, Custom.class);
    }

    @Test
    public void getClassesInDeclaration_simpleInterface() throws NoSuchMethodException {
        check(MyInterface.class, MyInterface.class);
    }

    @Test
    public void getClassesInDeclaration_simpleCustomArray() throws NoSuchMethodException {
        check(Custom[].class, Custom.class);
    }

    @Test
    public void getClassesInDeclaration_rawList() throws NoSuchMethodException {
        check(List.class, List.class);
    }

    @Test
    public void getClassesInDeclaration_simpleList() throws NoSuchMethodException {
        Type listOfIntegers = new TypeToken<List<Integer>>(){}.getType();
        check(listOfIntegers, List.class, Integer.class);
    }

    @Test
    public void getClassesInDeclaration_listOfInterface() throws NoSuchMethodException {
        Type listOfInterface = new TypeToken<List<MyInterface>>(){}.getType();
        check(listOfInterface, List.class, MyInterface.class);
    }

    @Test
    public void getClassesInDeclaration_listWildcard() throws NoSuchMethodException {
        Type listOfWildcard = new TypeToken<List<?>>(){}.getType();
        check(listOfWildcard, List.class);
    }

    @Test
    public void getClassesInDeclaration_nestedList() throws NoSuchMethodException {
        Type nestedList = new TypeToken<List<Set<Integer>>>(){}.getType();
        check(nestedList, List.class, Set.class, Integer.class);
    }

    @Test
    public void getClassesInDeclaration_nestedWildcard() throws NoSuchMethodException {
        Type nestedWildcard = new TypeToken<List<Set<?>>>(){}.getType();
        check(nestedWildcard, List.class, Set.class);
    }

    @Test
    public void getClassesInDeclaration_rawMap() throws NoSuchMethodException {
        check(Map.class, Map.class);
    }

    @Test
    public void getClassesInDeclaration_simpleMap() throws NoSuchMethodException {
        Type simpleMap = new TypeToken<Map<String, Custom>>(){}.getType();
        check(simpleMap, Map.class, String.class, Custom.class);
    }

    @Test
    public void getClassesInDeclaration_mapWildcardKey() throws NoSuchMethodException {
        Type mapWildcardKey = new TypeToken<Map<?, Custom>>(){}.getType();
        check(mapWildcardKey, Map.class, Custom.class);
    }

    @Test
    public void getClassesInDeclaration_mapWildcardValue() throws NoSuchMethodException {
        Type mapWildcardValue = new TypeToken<Map<String, ?>>(){}.getType();
        check(mapWildcardValue, Map.class, String.class);
    }

    @Test
    public void getClassesInDeclaration_nestedMapKey() throws NoSuchMethodException {
        Type nestedMapKey = new TypeToken<Map<List<String>, Custom>>(){}.getType();
        check(nestedMapKey, Map.class, List.class, String.class, Custom.class);
    }

    @Test
    public void getClassesInDeclaration_nestedMapValue() throws NoSuchMethodException {
        Type nestedMapValue = new TypeToken<Map<String, Set<Custom>>>(){}.getType();
        check(nestedMapValue, Map.class, String.class, Set.class, Custom.class);
    }

    @Test
    public void getClassesInDeclaration_customGeneric1P() throws NoSuchMethodException {
        Type customGeneric1P = new TypeToken<Custom1P<String>>(){}.getType();
        check(customGeneric1P, Custom1P.class, String.class);
    }

    @Test
    public void getClassesInDeclaration_customGeneric2P() throws NoSuchMethodException {
        Type customGeneric2P = new TypeToken<Custom2P<String, Integer>>(){}.getType();
        check(customGeneric2P, Custom2P.class, String.class, Integer.class);
    }

    @Test
    public void getClassesInDeclaration_customGeneric1PInterface() throws NoSuchMethodException {
        Type customGeneric1PInterface = new TypeToken<Custom1P<MyInterface>>(){}.getType();
        check(customGeneric1PInterface, Custom1P.class, MyInterface.class);
    }

    @Test
    public void getClassesInDeclaration_customGeneric1PWildcard() throws NoSuchMethodException {
        Type customGeneric1PWildcard = new TypeToken<Custom1P<?>>(){}.getType();
        check(customGeneric1PWildcard, Custom1P.class);
    }

    public <T> Custom1P<T> customGeneric1PVariable() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_customGeneric1PVariable() throws NoSuchMethodException {
        check("customGeneric1PVariable", Custom1P.class);
    }

    public <T extends Custom> Custom1P<T> customGeneric1PBoundVariable() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_customGeneric1PBoundVariable() throws NoSuchMethodException {
        check("customGeneric1PBoundVariable", Custom1P.class, Custom.class);
    }

    public Custom1P<List<String>> customGenericNestedList() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_customGenericNestedList() throws NoSuchMethodException {
        check("customGenericNestedList", Custom1P.class, List.class, String.class);
    }

    public <T> T[] genericArray() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_genericArray() throws NoSuchMethodException {
        check("genericArray"); // no types expected
    }

    public <T extends Custom> T[] boundGenericArray() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_boundGenericArray() throws NoSuchMethodException {
        check("boundGenericArray", Custom.class);
    }

    public <T> List<T[]> listGenericArray() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_listGenericArray() throws NoSuchMethodException {
        check("listGenericArray", List.class);
    }

    public <T extends Custom> List<T[]> listBoundGenericArray() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_listBoundGenericArray() throws NoSuchMethodException {
        check("listBoundGenericArray", List.class, Custom.class);
    }

    public Map<List<String>, Custom2P<Set<Integer>, Custom3P<Map<Custom2P<Long, Boolean>, Custom>, Float, Short>>>
    complex() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_complex() throws NoSuchMethodException {
        check("complex", List.class, String.class, Set.class, Integer.class, Map.class, Custom2P.class, Custom3P.class,
                Long.class, Boolean.class, Custom.class, Float.class, Short.class);
    }

    private static void check(String methodName, Class<?>... expected) throws NoSuchMethodException {
        Method testMethod = GenericTypeExplorerTest.class.getMethod(methodName);
        Type genReturnType = testMethod.getGenericReturnType();
        check(genReturnType, expected);
    }

    private static void check(Type typeDeclaration, Class<?>... expectedClasses) {
        Set<Class<?>> actual = GenericTypeExplorer.getClassesInDeclaration(typeDeclaration);
        Set<Class<?>> expected = new HashSet<>(Arrays.asList(expectedClasses));
        assertEquals(expected, actual);
    }
}
