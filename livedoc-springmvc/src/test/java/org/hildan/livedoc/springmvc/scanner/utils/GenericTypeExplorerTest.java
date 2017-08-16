package org.hildan.livedoc.springmvc.scanner.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.Reflections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GenericTypeExplorerTest {

    private static Reflections reflections;

    @BeforeClass
    public static void setUp() throws Exception {
        reflections = new Reflections();
    }

    interface MyInterface {
    }

    static class MyImpl1 implements MyInterface {
    }

    static class MyImpl2 implements MyInterface {
    }

    static class Custom {
    }

    static class Custom1P<T> {
    }

    static class Custom2P<T, U> {
    }

    static class Custom3P<T, U, V> {
    }

    public String simpleString() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_simpleString() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.singletonList(String.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("simpleString", expectedPresent, expectedAbsent);
    }

    public Integer[] simpleArray() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_simpleArray() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.singletonList(Integer.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("simpleArray", expectedPresent, expectedAbsent);
    }

    public Custom simpleCustom() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_simpleCustom() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.singletonList(Custom.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("simpleCustom", expectedPresent, expectedAbsent);
    }

    public MyInterface simpleInterface() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_simpleInterface() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(MyImpl1.class, MyImpl2.class);
        List<Class<?>> expectedAbsent = Collections.singletonList(MyInterface.class);
        check("simpleInterface", expectedPresent, expectedAbsent);
    }

    public Custom[] simpleCustomArray() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_simpleCustomArray() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.singletonList(Custom.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("simpleCustomArray", expectedPresent, expectedAbsent);
    }

    public List rawList() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_rawList() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.singletonList(List.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("rawList", expectedPresent, expectedAbsent);
    }

    public List<Integer> simpleList() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_simpleList() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(List.class, Integer.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("simpleList", expectedPresent, expectedAbsent);
    }

    public List<MyInterface> listOfInterface() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_listOfInterface() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(List.class, MyImpl1.class, MyImpl2.class);
        List<Class<?>> expectedAbsent = Collections.singletonList(MyInterface.class);
        check("listOfInterface", expectedPresent, expectedAbsent);
    }

    public List<?> listWildcard() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_listWildcard() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(List.class, Void.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("listWildcard", expectedPresent, expectedAbsent);
    }

    public List<Set<Integer>> nestedList() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_nestedList() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(List.class, Set.class, Integer.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("nestedList", expectedPresent, expectedAbsent);
    }

    public List<Set<?>> nestedWildcard() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_nestedWildcard() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(List.class, Set.class, Void.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("nestedWildcard", expectedPresent, expectedAbsent);
    }

    public Map rawMap() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_rawMap() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.singletonList(Map.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("rawMap", expectedPresent, expectedAbsent);
    }

    public Map<String, Custom> simpleMap() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_simpleMap() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Map.class, String.class, Custom.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("simpleMap", expectedPresent, expectedAbsent);
    }

    public Map<?, Custom> mapWildcardKey() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_mapWildcardKey() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Map.class, Void.class, Custom.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("mapWildcardKey", expectedPresent, expectedAbsent);
    }

    public Map<String, ?> mapWildcardValue() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_mapWildcardValue() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Map.class, String.class, Void.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("mapWildcardValue", expectedPresent, expectedAbsent);
    }

    public Map<List<String>, Custom> nestedMapKey() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_nestedMapKey() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Map.class, List.class, String.class, Custom.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("nestedMapKey", expectedPresent, expectedAbsent);
    }

    public Map<String, Set<Custom>> nestedMapValue() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_nestedMapValue() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Map.class, String.class, Set.class, Custom.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("nestedMapValue", expectedPresent, expectedAbsent);
    }

    public Custom1P<String> customGeneric1P() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_customGeneric1P() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Custom1P.class, String.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("customGeneric1P", expectedPresent, expectedAbsent);
    }

    public Custom2P<String, Integer> customGeneric2P() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_customGeneric2P() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Custom2P.class, String.class, Integer.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("customGeneric2P", expectedPresent, expectedAbsent);
    }

    public Custom1P<MyInterface> customGeneric1PInterface() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_customGeneric1PInterface() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Custom1P.class, MyImpl1.class, MyImpl2.class);
        List<Class<?>> expectedAbsent = Collections.singletonList(MyInterface.class);
        check("customGeneric1PInterface", expectedPresent, expectedAbsent);
    }

    public Custom1P<?> customGeneric1PWildcard() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_customGeneric1PWildcard() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Custom1P.class, Void.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("customGeneric1PWildcard", expectedPresent, expectedAbsent);
    }

    public <T> Custom1P<T> customGeneric1PVariable() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_customGeneric1PVariable() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.singletonList(Custom1P.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("customGeneric1PVariable", expectedPresent, expectedAbsent);
    }

    public <T extends Custom> Custom1P<T> customGeneric1PBoundVariable() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_customGeneric1PBoundVariable() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Custom1P.class, Custom.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("customGeneric1PBoundVariable", expectedPresent, expectedAbsent);
    }

    public Custom1P<List<String>> customGenericNestedList() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_customGenericNestedList() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(Custom1P.class, List.class, String.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("customGenericNestedList", expectedPresent, expectedAbsent);
    }

    public <T> T[] genericArray() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_genericArray() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.emptyList();
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("genericArray", expectedPresent, expectedAbsent);
    }

    public <T extends Custom> T[] boundGenericArray() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_boundGenericArray() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.singletonList(Custom.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("boundGenericArray", expectedPresent, expectedAbsent);
    }

    public <T> List<T[]> listGenericArray() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_listGenericArray() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Collections.singletonList(List.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("listGenericArray", expectedPresent, expectedAbsent);
    }

    public <T extends Custom> List<T[]> listBoundGenericArray() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_listBoundGenericArray() throws NoSuchMethodException {
        List<Class<?>> expectedPresent = Arrays.asList(List.class, Custom.class);
        List<Class<?>> expectedAbsent = Collections.emptyList();
        check("listBoundGenericArray", expectedPresent, expectedAbsent);
    }

    public Map<List<String>, Custom2P<Set<Integer>, Custom3P<Map<Custom2P<Long, Boolean>, Custom>, Float, Short>>>
    complex() {
        return null;
    }

    @Test
    public void getTypesInDeclaration_complex() throws NoSuchMethodException {

        Set<Class<?>> expectedPresent = new HashSet<>();
        expectedPresent.add(List.class);
        expectedPresent.add(String.class);
        expectedPresent.add(Set.class);
        expectedPresent.add(Integer.class);
        expectedPresent.add(Map.class);
        expectedPresent.add(Custom2P.class);
        expectedPresent.add(Custom3P.class);
        expectedPresent.add(Long.class);
        expectedPresent.add(Boolean.class);
        expectedPresent.add(Custom.class);
        expectedPresent.add(Float.class);
        expectedPresent.add(Short.class);

        Set<Class<?>> expectedAbsent = new HashSet<>();

        check("complex", expectedPresent, expectedAbsent);
    }

    private static void check(String methodName, Collection<Class<?>> expectedPresent,
            Collection<Class<?>> expectedAbsent) throws NoSuchMethodException {

        Class<?> thisTestClass = GenericTypeExplorerTest.class;
        Method testMethod = thisTestClass.getMethod(methodName);
        Type genReturnType = testMethod.getGenericReturnType();

        Set<Class<?>> candidates = GenericTypeExplorer.getTypesInDeclaration(genReturnType, reflections);

        for (Class<?> c : expectedPresent) {
            assertTrue("Should contain class " + c.getSimpleName(), candidates.contains(c));
        }
        for (Class<?> c : expectedAbsent) {
            assertFalse("Should not contain class " + c.getSimpleName(), candidates.contains(c));
        }
    }

}
