package org.hildan.livedoc.core.merger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.readers.combined.DocMerger;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocMergerTest {

    private DocMerger merger;

    @Before
    public void setUp() {
        merger = new DocMerger(new FieldPropertyScanner());
    }

    @Test
    public void merge_primitives() {
        assertEquals(Integer.valueOf(5), merger.merge(5, 0, 0));
        assertEquals(Integer.valueOf(5), merger.merge(0, 5, 0));
        assertEquals(Integer.valueOf(5), merger.merge(3, 5, 0));
    }

    @Test
    public void merge_plainObject_isOverridden() {
        NotMergeable base = new NotMergeable(5, null);
        NotMergeable override = new NotMergeable(0, "five");
        NotMergeable merged = merger.merge(base, override);
        assertEquals(override.num, merged.num);
        assertEquals(override.data, merged.data);
    }

    @Test
    public void merge_mergeable_isMerged() {
        MergeableObj base = new MergeableObj(5, null);
        MergeableObj override = new MergeableObj(0, "five");
        MergeableObj merged = merger.merge(base, override);
        assertEquals(5, merged.num);
        assertEquals("five", merged.data);
    }

    @Test
    public void mergeList_addsMissingElementsBothWays() {
        NotMergeable item1 = new NotMergeable(1, "one");
        NotMergeable item2 = new NotMergeable(2, "two");
        List<NotMergeable> listEmpty = Collections.emptyList();
        List<NotMergeable> list1 = Collections.singletonList(item1);
        List<NotMergeable> list2 = Collections.singletonList(item2);
        List<NotMergeable> list1and2 = Arrays.asList(item1, item2);
        List<NotMergeable> list2and1 = Arrays.asList(item2, item1);

        assertEquals(list1, merger.mergeList(list1, listEmpty, o -> o.num));
        assertEquals(list1, merger.mergeList(listEmpty, list1, o -> o.num));

        assertEquals(list2and1, merger.mergeList(list1, list2, o -> o.num));
        assertEquals(list1and2, merger.mergeList(list2, list1, o -> o.num));
    }

    @Test
    public void mergeList_notMergeable_isReplaced() {
        NotMergeable item1data1 = new NotMergeable(1, "one", 0);
        NotMergeable item1data2 = new NotMergeable(1, null, 5);

        List<NotMergeable> list1 = Collections.singletonList(item1data1);
        List<NotMergeable> list2 = Collections.singletonList(item1data2);

        assertEquals(list2, merger.mergeList(list1, list2, o -> o.num));
    }

    @Test
    public void mergeList_mergeable_isMerged() {
        MergeableObj item1data1 = new MergeableObj(1, "one", 0);
        MergeableObj item1data2 = new MergeableObj(1, null, 5);
        MergeableObj item1merged = new MergeableObj(1, "one", 5);

        List<MergeableObj> list1 = Collections.singletonList(item1data1);
        List<MergeableObj> list2 = Collections.singletonList(item1data2);
        List<MergeableObj> expectedMergedList = Collections.singletonList(item1merged);

        assertEquals(expectedMergedList, merger.mergeList(list1, list2, o -> o.num));
    }

    @Test
    public void mergeList_mergeable_mixedElements() {
        MergeableObj item1 = new MergeableObj(1, "one");

        MergeableObj item2 = new MergeableObj(2, "two");
        MergeableObj item2noData = new MergeableObj(2, null);
        MergeableObj item2merged = new MergeableObj(2, "two");

        MergeableObj item3 = new MergeableObj(3, "three", 42);
        MergeableObj item3modified = new MergeableObj(3, "three++", 0);
        MergeableObj item3merged = new MergeableObj(3, "three++", 42);

        List<MergeableObj> list1 = Arrays.asList(item1, item2, item3);
        List<MergeableObj> list2 = Arrays.asList(item1, item3modified, item2noData);

        List<MergeableObj> expected = Arrays.asList(item1, item3merged, item2merged);

        assertEquals(expected, merger.mergeList(list1, list2, o -> o.num));
    }
}
