package net.asodev.islandutils.util;

public class Maths {

    public static Integer getRandomInteger(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


}
