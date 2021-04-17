package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;

@Autonomous
public class WobbleGoalOnly extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line
    int numRings;

    Rev2mDistanceSensor distTop;
    Servo leftWobble;
    Servo rightWobble;
    DcMotor launchMotorLeft;
    DcMotor launchMotorRight;
    DcMotor intakeMotor;

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "initialized");
        telemetry.update();

        distTop = hardwareMap.get(Rev2mDistanceSensor.class, "distTop");
        leftWobble = hardwareMap.get(Servo.class, "leftWobble");
        rightWobble = hardwareMap.get(Servo.class, "rightWobble");
        launchMotorLeft = hardwareMap.get(DcMotor.class, "launchMotor1");
        launchMotorRight = hardwareMap.get(DcMotor.class, "launchMotor2");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");


        autonLibrary = new AutonLibrary(drivingLibrary, this);
        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        if (opModeIsActive()) {
            numRings = autonLibrary.getStackHeight(distTop);
            rightWobble.setPosition(.75);
            leftWobble.setPosition(0);
            sleep(1000);
            //moves left
            drivingLibrary.bevelDriveCorrect(.5f, 0, 0);
            sleep(2000);
            drivingLibrary.brakeStop();
            //corrects angle
            while (Math.abs(drivingLibrary.getIMUAngle() - 0) > .05) {
                if (drivingLibrary.getIMUAngle() > 0) { // check which direction we need to turn
                    drivingLibrary.bevelDriveCorrect(0, 0, .1f);
                } else {
                    drivingLibrary.bevelDriveCorrect(0, 0, -.1f);

                    ranOnce = true;
                }
            }
            //moves forward to park line
            /*drivingLibrary.bevelDrive(0, -.5f, 0);
            sleep(4100);
            drivingLibrary.brakeStop();*/

            //moves forward to park line odo
            drivingLibrary.resetEncoderValues();

            while(drivingLibrary.getDistTravelled() < 65) {
                drivingLibrary.bevelDrive(0, -.5f, 0);
            }

            drivingLibrary.brakeStop();


            switch (numRings) {
                case 0:
                    //move backwards
                    /*drivingLibrary.bevelDrive(0, .5f, 0);
                    sleep(1000);
                    drivingLibrary.brakeStop();*/

                    // moves backwards odo
                    drivingLibrary.resetEncoderValues();

                    while(drivingLibrary.getDistTravelled() > -12) {
                        drivingLibrary.bevelDrive(0, .5f, 0);
                    }

                    drivingLibrary.brakeStop();


                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    drivingLibrary.bevelDriveCorrect(-.5f, 0, 0);
                    sleep(1500);
                    drivingLibrary.brakeStop();

                    // drive forwards
                    /*drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(1750);
                    drivingLibrary.brakeStop();*/

                    // drive forwards odo
                    drivingLibrary.resetEncoderValues();

                    while(drivingLibrary.getDistTravelled() < 12) {
                        drivingLibrary.bevelDrive(0, -.5f, 0);
                    }

                    drivingLibrary.brakeStop();

                    break;
                case 1:
                    //go forwards
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(1500);
                    // go right
                    drivingLibrary.bevelDriveCorrect(-.5f, 0, 0);
                    sleep(2500);
                    drivingLibrary.brakeStop();
                    drivingLibrary.bevelDrive(0, .5f, 0);
                    sleep(1300);
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    break;
                case 4:
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(3500);
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    drivingLibrary.brakeStop();
                    //move backwards
                    drivingLibrary.bevelDrive(0, .5f, 0);
                    sleep(3000);
                    drivingLibrary.brakeStop();
                    drivingLibrary.bevelDriveCorrect(-.5f, 0, 0); // only put this in case 4 rings
                    sleep(1000);
                    drivingLibrary.brakeStop();
                    break;
            }

            while (Math.abs(drivingLibrary.getIMUAngle() - Math.PI / 2) > .1) {
                if (drivingLibrary.getIMUAngle() > Math.PI / 2) { // check which direction we need to turn
                    drivingLibrary.bevelDriveCorrect(0, 0, .5f);
                } else {
                    drivingLibrary.bevelDriveCorrect(0, 0, -.5f);
                }
            }
            drivingLibrary.brakeStop();
        }
    }
}