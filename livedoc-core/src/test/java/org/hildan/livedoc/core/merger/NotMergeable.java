package org.hildan.livedoc.core.merger;

import java.util.Objects;

class NotMergeable {
    int num;
    String data;
    int data2;

    NotMergeable(int num, String data) {
        this.data = data;
        this.num = num;
    }

    NotMergeable(int num, String data, int data2) {
        this.num = num;
        this.data = data;
        this.data2 = data2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotMergeable that = (NotMergeable) o;
        return num == that.num && data2 == that.data2 && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, data, data2);
    }

    @Override
    public String toString() {
        return "NotMergeable{" + "num=" + num + ", data='" + data + '\'' + ", data2=" + data2 + '}';
    }
}
