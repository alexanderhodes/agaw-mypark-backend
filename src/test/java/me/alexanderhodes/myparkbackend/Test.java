package me.alexanderhodes.myparkbackend;

import java.util.Date;

public class Test {

    public static void main (String[] args) {
        System.out.println(new Date());
        // drei Stunden
        System.out.println(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3));
        System.out.println(new Date(System.currentTimeMillis() + 864000000));
    }

}
