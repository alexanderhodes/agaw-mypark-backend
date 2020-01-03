package me.alexanderhodes.myparkbackend;

import org.apache.tomcat.jni.Local;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

public class Test {

    public static void main (String[] args) {
        System.out.println(new Date());
        // drei Stunden
        System.out.println(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3));
        System.out.println(new Date(System.currentTimeMillis() + 864000000));

        int number = 4;
        for (int i = 0; i < number; i++) {
            System.out.println(UUID.randomUUID().toString());
        }

        int x = 10;
        for (int i = 0; i < x; i++) {
            System.out.println("insert into mp_parkingspace (id, number, f_parking_space_status) VALUES ('" +
                    UUID.randomUUID().toString() + "', '" + (78 + i) + "', '40aaea05-317c-4ba1-96a2-474f4dd73b1b');");
        }

        LocalTime localTime = LocalTime.parse("10:00");
        System.out.println(localTime);
    }

}
