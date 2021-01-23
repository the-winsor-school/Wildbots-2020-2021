package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.libraries.DrivingLibrary;

@Autonomous
public class ActonColorSensPark extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    //variables:
    private static final long crashIntoWall = 500; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 4000; //Time to get to line
    ColorSensor color_sensor;

    @Override
    public void internalPostInitLoop() {
        super.internalPostInitLoop();

        color_sensor = hardwareMap.colorSensor.get("color");
    }

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "BAAAAAAAH initialized");
        telemetry.update();

        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        //drive for (crashIntoWall) speed
        if (opModeIsActive()) {
            if (!ranOnce) {
                //moving towards left blue line of starting box
                while (isGray()) {
                    drivingLibrary.drive(0, .5f, 0);
                }

                /*keeps driving for 300 ms (test for the real number of milliseconds), so that the
                robot will have crossed over the blue line, making it possible for th robot to
                later sense the white line */

                sleep(300);
                //moving towards the parking line until it hits the blue line
                //blue line is (part of wobble goal box)
                while (isGray()) {
                    drivingLibrary.drive(.5f, 0, 0);
                }
                //driving through the blue line
                while (!isGray()) {
                    drivingLibrary.drive(.5f, 0, 0);
                }
                //driving until the color sensor sees the parking line (white)
                while (isGray()) {
                    drivingLibrary.drive(.5f, 0, 0);
                }

                stop();
            }
        }
    }

    //defining what the color sensor sees as gray (darker than white and blue)
    //!!!test whether blue is darker than gray
    private boolean isGray() {
        //prints out the colors that the color sensor is seeing
        telemetry.addData("red", color_sensor.red());
        telemetry.addData("green", color_sensor.green());
        telemetry.addData("blue", color_sensor.blue());
        telemetry.addData("alpha", color_sensor.alpha());
        telemetry.update();
        //brightness = alpha
        int alpha = color_sensor.alpha();

        //returns true or false depending on brightness
        //TEST whether 50 is the right brightness
        return alpha < 50;


    }
}
