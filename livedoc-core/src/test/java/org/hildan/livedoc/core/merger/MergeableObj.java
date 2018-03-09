package org.hildan.livedoc.core.merger;

import java.util.Objects;

class MergeableObj implements Mergeable<MergeableObj> {
    int num;
    String data;
    int data2;

    MergeableObj() {
    }

    MergeableObj(int num, String data) {
        this.num = num;
        this.data = data;
    }

    MergeableObj(int num, String data, int data2) {
        this.num = num;
        this.data = data;
        this.data2 = data2;
    }

    @Override
    public MergeableObj merge(MergeableObj override, DocMerger merger) {
        return merger.mergeProperties(this, override, new MergeableObj());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MergeableObj that = (MergeableObj) o;
        return num == that.num && data2 == that.data2 && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, data, data2);
    }

    @Override
    public String toString() {
        return "MergeableObj{" + "num=" + num + ", data='" + data + '\'' + ", data2=" + data2 + '}';
    }
}
