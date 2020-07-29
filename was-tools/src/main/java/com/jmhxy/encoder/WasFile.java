package com.jmhxy.encoder;

public class WasFile {
    public int id;
    public int size;
    protected int offset;
    protected int space;
    public String name;
    public WdfFile parent;

    public String toString() {
        return this.name == null ? Integer.toHexString(this.id).toUpperCase() : this.name;
    }
}