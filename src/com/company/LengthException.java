package com.company;

public class LengthException extends Exception {

    private String lengthSent;

    public LengthException() {
    }

    public String getLengthSet() {
        return this.lengthSent;
    }

    public String toString() {
        String returnString;

        returnString = this.lengthSent + " is an invalid length!\n";

        return returnString;
    }
}