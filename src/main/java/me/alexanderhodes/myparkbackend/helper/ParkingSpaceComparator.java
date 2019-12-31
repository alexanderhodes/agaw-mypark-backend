package me.alexanderhodes.myparkbackend.helper;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;

import java.util.Comparator;

public class ParkingSpaceComparator implements Comparator<ParkingSpace> {

    @Override
    public int compare(ParkingSpace o1, ParkingSpace o2) {
        int o1number = parseInt(o1.getNumber());
        int o2number = parseInt(o2.getNumber());

        return o1number - o2number;
    }

    private int parseInt (String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
