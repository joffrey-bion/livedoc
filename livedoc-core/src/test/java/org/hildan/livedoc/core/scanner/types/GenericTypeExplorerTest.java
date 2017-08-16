package org.hildan.livedoc.core.scanner.types;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GenericTypeExplorerTest {

    interface MyInterface {
    }

    static class Custom {
    }

    static class Custom1P<T> {
    }

    static class Custom2P<T, U> {
    }

    static class Custom3P<T, U, V> {
    }

    @Test(expected = IllegalArgumentException.class)
    public void getClassesInDeclaration_failsOnNull() throws NoSuchMethodException {
        GenericTypeExplorer.getClassesInDeclaration(null);
    }

    public String simpleString() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_simpleString() throws NoSuchMethodException {
        check("simpleString", String.class);
    }

    public Integer[] simpleArray() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_simpleArray() throws NoSuchMethodException {
        check("simpleArray", Integer.class);
    }

    public Custom simpleCustom() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_simpleCustom() throws NoSuchMethodException {
        check("simpleCustom", Custom.class);
    }

    public MyInterface simpleInterface() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_simpleInterface() throws NoSuchMethodException {
        check("simpleInterface", MyInterface.class);
    }

    public Custom[] simpleCustomArray() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_simpleCustomArray() throws NoSuchMethodException {
        check("simpleCustomArray", Custom.class);
    }

    public List rawList() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_rawList() throws NoSuchMethodException {
        check("rawList", List.class);
    }

    public List<Integer> simpleList() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_simpleList() throws NoSuchMethodException {
        check("simpleList", List.class, Integer.class);
    }

    public List<MyInterface> listOfInterface() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_listOfInterface() throws NoSuchMethodException {
        check("listOfInterface", List.class, MyInterface.class);
    }

    public List<?> listWildcard() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_listWildcard() throws NoSuchMethodException {
        check("listWildcard", List.class);
    }

    public List<Set<Integer>> nestedList() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_nestedList() throws NoSuchMethodException {
        check("nestedList", List.class, Set.class, Integer.class);
    }

    public List<Set<?>> nestedWildcard() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_nestedWildcard() throws NoSuchMethodException {
        check("nestedWildcard", List.class, Set.class);
    }

    public Map rawMap() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_rawMap() throws NoSuchMethodException {
        check("rawMap", Map.class);
    }

    public Map<String, Custom> simpleMap() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_simpleMap() throws NoSuchMethodException {
        check("simpleMap", Map.class, String.class, Custom.class);
    }

    public Map<?, Custom> mapWildcardKey() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_mapWildcardKey() throws NoSuchMethodException {
        check("mapWildcardKey", Map.class, Custom.class);
    }

    public Map<String, ?> mapWildcardValue() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_mapWildcardValue() throws NoSuchMethodException {
        check("mapWildcardValue", Map.class, String.class);
    }

    public Map<List<String>, Custom> nestedMapKey() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_nestedMapKey() throws NoSuchMethodException {
        check("nestedMapKey", Map.class, List.class, String.class, Custom.class);
    }

    public Map<String, Set<Custom>> nestedMapValue() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_nestedMapValue() throws NoSuchMethodException {
        check("nestedMapValue", Map.class, String.class, Set.class, Custom.class);
    }

    public Custom1P<String> customGeneric1P() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_customGeneric1P() throws NoSuchMethodException {
        check("customGeneric1P", Custom1P.class, String.class);
    }

    public Custom2P<String, Integer> customGeneric2P() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_customGeneric2P() throws NoSuchMethodException {
        check("customGeneric2P", Custom2P.class, String.class, Integer.class);
    }

    public Custom1P<MyInterface> customGeneric1PInterface() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_customGeneric1PInterface() throws NoSuchMethodException {
        check("customGeneric1PInterface", Custom1P.class, MyInterface.class);
    }

    public Custom1P<?> customGeneric1PWildcard() {
        return null;
    }

    @Test
    public void getClassesInDeclaration_customGeneric1PWildcard() throws NoSuchMethodException {
        check("customGeneric1PWildcard", Custom1P.class);
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

        Set<Class<?>> candidates = GenericTypeExplorer.getClassesInDeclaration(genReturnType);

        for (Class<?> c : expected) {
            assertTrue("Should contain class " + c.getSimpleName(), candidates.contains(c));
        }
    }
}
