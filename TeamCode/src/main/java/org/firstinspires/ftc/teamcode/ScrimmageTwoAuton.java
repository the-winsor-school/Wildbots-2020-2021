package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;

@Autonomous
public class ScrimmageTwoAuton extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line
    int numRings;

    Rev2mDistanceSensor distTop;
    Rev2mDistanceSensor distBot;
    Servo leftWobble;
    Servo rightWobble;

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "initialized");
        telemetry.update();

        distTop = hardwareMap.get(Rev2mDistanceSensor.class, "distTop");
        distBot = hardwareMap.get(Rev2mDistanceSensor.class, "distBot");
        leftWobble = hardwareMap.get(Servo.class, "leftWobble");
        rightWobble = hardwareMap.get(Servo.class, "rightWobble");


        AutonLibrary autonLibrary = null;
        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        if (opModeIsActive()) if (!ranOnce) {
            numRings = autonLibrary.getStackHeight(distTop, distBot);
            leftWobble.setPosition(-1);
            rightWobble.setPosition(1);
            //moves left
            drivingLibrary.bevelDrive(-.5f, 0, 0);
            sleep(1000);
            drivingLibrary.brakeStop();
            //moves forward to park line
            drivingLibrary.bevelDrive(0, -.5f, 0);
            sleep(4000);
            drivingLibrary.brakeStop();


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
                    leftWobble.setPosition(-0.3);
                    rightWobble.setPosition(0.3);
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
                    sleep(4000);
                    drivingLibrary.brakeStop();
                    //move right
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                    sleep(2000);
                    drivingLibrary.brakeStop();
                    //move backwards
                    drivingLibrary.bevelDrive(0, .5f, 0);
                    sleep(4000);
                    drivingLibrary.brakeStop();
                    break;

            }
            while (Math.abs(drivingLibrary.getIMUAngle() - 0) > .05) {
                if (drivingLibrary.getIMUAngle() > 0) { // check which direction we need to turn
                    drivingLibrary.bevelDrive(0, 0, .1f);
                } else {
                    drivingLibrary.bevelDrive(0, 0, -.1f);

                    ranOnce = true;
                }

            }
        }
    }
}
