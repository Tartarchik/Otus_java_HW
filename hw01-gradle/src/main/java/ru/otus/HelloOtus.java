package ru.otus;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloOtus {

    public static void main(String[] args) {

        List<String> str = new ArrayList<>(Arrays.asList("Hello", "Otus"));

        String result = Joiner.on("").join(str);

        System.out.println(result);

    }
}
