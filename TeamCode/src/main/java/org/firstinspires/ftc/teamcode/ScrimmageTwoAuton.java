package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;


public class ScrimmageTwoAuton extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line
    int numRings;

    Rev2mDistanceSensor distTop;
    Rev2mDistanceSensor distBot;

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "initialized");
        telemetry.update();

        distTop = hardwareMap.get(Rev2mDistanceSensor.class, "topSensor");
        distBot = hardwareMap.get(Rev2mDistanceSensor.class, "bottomSensor");

        AutonLibrary autonLibrary = null;
        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        if (opModeIsActive()) {
            if (!ranOnce) {
                //moves left
                drivingLibrary.bevelDrive(-.5f, -.5f, 0);
                sleep(2000);
                //moves forward to park line
                drivingLibrary.bevelDrive(0, -.5f, 0);
                sleep(8500);
                drivingLibrary.brakeStop();

                numRings = autonLibrary.getStackHeight(distTop, distBot);

                switch (numRings) {
                    case 0:
                        //move right
                        drivingLibrary.bevelDrive(.5f, 0, 0);
                        sleep(2000);
                        drivingLibrary.brakeStop();
                        //move backwards
                        drivingLibrary.bevelDrive(0, .5f, 0);
                        sleep(2000);
                        drivingLibrary.brakeStop();
                        break;
                    case 1:
                        // go right
                        drivingLibrary.bevelDrive(.5f, 0, 0);
                        sleep(2000);
                        drivingLibrary.brakeStop();
                        //go forwards
                        drivingLibrary.bevelDrive(0, -.5f, 0);
                        sleep(3000);
                        drivingLibrary.brakeStop();
                        break;
                    case 4:
                        drivingLibrary.bevelDrive(0, -.5f, 0);
                        sleep(8500);
                        drivingLibrary.brakeStop();
                        //move right
                        drivingLibrary.bevelDrive(.5f, 0, 0);
                        sleep(2000);
                        drivingLibrary.brakeStop();
                        //move backwards
                        drivingLibrary.bevelDrive(0, .5f, 0);
                        sleep(8500);
                        drivingLibrary.brakeStop();
                        break;
                }

                ranOnce = true;
            }

        }
    }
}
