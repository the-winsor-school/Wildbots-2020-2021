package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.DrivingLibrary;

import java.util.Arrays;

@Autonomous
public class EncodersWithNewWheels extends LinearOpMode {
    DrivingLibrary drivingLibrary;
    int drivingMode;

    //change starting position based on where it is
    int xPos=0;
    int yPos =0;

    @Override
    public void runOpMode() throws InterruptedException {
        //default
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);
        boolean ranOnce = false;
        telemetry.addData("status", "initialized");
        telemetry.update();

        drivingLibrary.resetEncoderValues();
        drivingLibrary.setEncoders(12);

        int[] values = drivingLibrary.getEncoderValues();
        drivingLibrary.setRecordEncoderTable();
        //variables stand for veria(ble) fun
        double deltaX;
        double deltaY;
        double xPos=0;
        double yPos=0;

        waitForStart();
        if (opModeIsActive()) {
            if (!ranOnce) {
                drivingLibrary.bevelDrive(0, -.75f, 0);

                values=drivingLibrary.getEncoderValues();
                //get the deltas which you get in the library
                deltaX= drivingLibrary.getDeltaX();
                xPos+=deltaX;
                deltaY=drivingLibrary.getDeltaY();
                yPos+=deltaY;
                values=drivingLibrary.getEncoderValues();
                //add telemetry data
                telemetry.addData("xpos:", xPos);
                telemetry.addData("yPos", yPos);
                telemetry.addData("encoder values:", Arrays.toString(values));
                telemetry.update();

            }
            drivingLibrary.brakeStop();
        }
    }

}
