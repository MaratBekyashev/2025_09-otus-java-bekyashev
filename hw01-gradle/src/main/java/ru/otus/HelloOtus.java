package ru.otus;

import com.google.common.collect.ImmutableList;

@SuppressWarnings({"java:S106", "java:S4738"})
public class HelloOtus {

    public static void main(String[] args) {
        ImmutableList<String> list = ImmutableList.of("Hello", "Guava", "from", "Otus");
        System.out.println(String.join(" ", list));
    }
}
