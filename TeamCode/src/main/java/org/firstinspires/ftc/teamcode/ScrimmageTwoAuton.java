package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous
public class ScrimmageTwoAuton extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line
    int numRings;

    Rev2mDistanceSensor distTop;
    Rev2mDistanceSensor distBot;
    Servo leftWobble;
    Servo rightWobble;
    DcMotor launchMotor;
    DcMotor intakeMotor;

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "initialized");
        telemetry.update();

        distBot = hardwareMap.get(Rev2mDistanceSensor.class, "distTop");
        distTop = hardwareMap.get(Rev2mDistanceSensor.class, "distBot");
        leftWobble = hardwareMap.get(Servo.class, "leftWobble");
        rightWobble = hardwareMap.get(Servo.class, "rightWobble");
        launchMotor = hardwareMap.get(DcMotor.class, "launchMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");


        autonLibrary = new AutonLibrary(drivingLibrary, this);
        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        if (opModeIsActive()) {
            numRings = 0;
            //numRings = autonLibrary.getStackHeight(distTop);
            leftWobble.setPosition(-1);
            rightWobble.setPosition(1);
            sleep(500);
            //moves left
            drivingLibrary.bevelDrive(.5f, 0, 0);
            sleep(2000);
            drivingLibrary.brakeStop();
            //corrects angle
            while (Math.abs(drivingLibrary.getIMUAngle() - 0) > .05) {
                if (drivingLibrary.getIMUAngle() > 0) { // check which direction we need to turn
                    drivingLibrary.bevelDrive(0, 0, .1f);
                } else {
                    drivingLibrary.bevelDrive(0, 0, -.1f);

                    ranOnce = true;
                }

            }
            //moves forward to park line
            drivingLibrary.bevelDrive(0, -.5f, 0);
            sleep(4000);
            drivingLibrary.brakeStop();


            switch (numRings) {
                case 0:
                    //move backwards
                    drivingLibrary.bevelDrive(0, .5f, 0);
                    sleep(1000);
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    drivingLibrary.bevelDrive(-.5f, 0, 0);
                    sleep(1500);
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(1500);
                    drivingLibrary.brakeStop();
                    break;
                case 1:
                    //go forwards
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(1500);
                    // go right
                    drivingLibrary.bevelDrive(-.5f, 0, 0);
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
                    drivingLibrary.bevelDrive(-.5f, 0, 0); // only put this in case 4 rings
                    sleep(1000);
                    drivingLibrary.brakeStop();
                    break;
            }

            while (Math.abs(drivingLibrary.getIMUAngle() - Math.PI / 2) > .1) {
                if (drivingLibrary.getIMUAngle() > Math.PI / 2) { // check which direction we need to turn
                    drivingLibrary.bevelDrive(0, 0, .5f);
                } else {
                    drivingLibrary.bevelDrive(0, 0, -.5f);
                }
            }
            drivingLibrary.brakeStop();

            while (distBot.getDistance(DistanceUnit.CM) > 65) {
                drivingLibrary.bevelDrive(.5f, 0, 0);
            }
            drivingLibrary.brakeStop();

            while (distTop.getDistance(DistanceUnit.CM) < 80) {
                drivingLibrary.bevelDrive(0, .5f, 0);
            }
            drivingLibrary.brakeStop();

            drivingLibrary.spinToAngle(-0.03591903);
            launchMotor.setPower(1);
            sleep(500);
            intakeMotor.setPower(0.5);
            sleep(500);
            launchMotor.setPower(0);
            intakeMotor.setPower(0);
            drivingLibrary.spinToAngle(-0.09314792);
            launchMotor.setPower(1);
            sleep(500);
            intakeMotor.setPower(.5);
            sleep(500);
            launchMotor.setPower(0);
            intakeMotor.setPower(0);
            drivingLibrary.spinToAngle(0.1648029);
            launchMotor.setPower(1);
            sleep(500);
            launchMotor.setPower(0);
            intakeMotor.setPower(0);

            drivingLibrary.bevelDrive(0, -.5f, 0);
            sleep(3000);
        }
    }
}