package ru.otus;

import com.google.common.collect.ImmutableList;

public class MainClass {

    public static void main(String[] args) {
        ImmutableList<String> list = ImmutableList.of("Hello", "Guava", "from", "Otus");
        System.out.println(String.join(" ", list));
    }
}
