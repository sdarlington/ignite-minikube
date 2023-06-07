package com.gridgain.ps.k8s.igniteclient;

public class Person {
    private final String name;
    private final int height;

    public Person(String name, int height) {
        this.name = name;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }
}
