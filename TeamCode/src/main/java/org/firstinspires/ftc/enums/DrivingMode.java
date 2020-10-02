package org.firstinspires.ftc.enums;

public enum DrivingMode {
    FLOAT_STOP("Float"),
    BRAKE_STOP("Brake");

    String stringValue;

    DrivingMode(String s) {
        stringValue = s;
    }

    public String getStringValue() {
        return stringValue;
    }
}
