package org.yuanrui;

import java.util.HashMap;
import java.util.Map;

public class InternTest {


    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<>();
        //map.put(1, 12306);
        System.out.println(map.putIfAbsent(1, 90807));
    }
}
