package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Wheeeeee extends LinearOpMode {
    RevBlinkinLedDriver LEDStrip;
    RevBlinkinLedDriver.BlinkinPattern pattern;


    public void runOpMode() throws InterruptedException {
        LEDStrip = hardwareMap.get(RevBlinkinLedDriver.class, "LIGHTSOURCE AAAH");
        pattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE;

        waitForStart();

        while(opModeIsActive()) {
            if(gamepad1.a) {
                pattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_WITH_GLITTER;
            }
            LEDStrip.setPattern(pattern);
        }
    }
}
