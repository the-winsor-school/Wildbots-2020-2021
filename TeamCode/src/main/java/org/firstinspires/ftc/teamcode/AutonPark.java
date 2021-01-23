package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.DrivingLibrary;

@Autonomous
public class AutonPark extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "BAMMMMM initialized");
        telemetry.update();

        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        //drive for (crashIntoWall) speed
        if (opModeIsActive()) {
            if (!ranOnce) {
                drivingLibrary.drive(0, crashIntoWall, 0);
                sleep(500);
                drivingLibrary.brakeStop();
                ranOnce = true;
            }

        }
    }
}
