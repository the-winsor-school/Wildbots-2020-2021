package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.DrivingLibrary;

@Autonomous
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

        //drive for (crashIntoWall) speed
        if (opModeIsActive()) {
            if (!ranOnce) {
                drivingLibrary.drive(0, .5f, 0);
                sleep(crashIntoWall);
                drivingLibrary.brakeStop();
                drivingLibrary.drive(.5f, 0, 0);
                sleep(parkLine);
                drivingLibrary.brakeStop();
                ranOnce = true;
            }
        }
    }
}
