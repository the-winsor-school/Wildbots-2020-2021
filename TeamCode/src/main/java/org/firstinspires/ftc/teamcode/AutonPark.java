package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.DrivingLibrary;

@Autonomous(name = "Scrimmage Auton Park")
public class AutonPark extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    //variables:
    private static final long crashIntoWall = 500; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 4000; //Time to get to line

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

        if (opModeIsActive()) {
            if (!ranOnce) {

                // litreally drive in a straight line forwards
                drivingLibrary.bevelDrive(0, -.5f, 0);
                sleep(8500);
                drivingLibrary.brakeStop();

                drivingLibrary.bevelDrive(0, .5f, 0);
                sleep(3000);
                drivingLibrary.brakeStop();

                // adjust angle back to 0
                while(Math.abs(drivingLibrary.getIMUAngle() - 0) > .05) {
                    if (drivingLibrary.getIMUAngle() > 0) { // check which direction we need to turn
                        drivingLibrary.bevelDrive(0, 0, .1f);
                    }
                    else {
                        drivingLibrary.bevelDrive(0, 0, -.1f);
                    }
                }
                ranOnce = true;
            }
        }
    }
}
