package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled
public class NoDistanceSensorTest extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line

    Servo leftWobble;
    Servo rightWobble;
    //DcMotor launchMotor;
    //DcMotor intakeMotor;

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "initialized");
        telemetry.update();

        leftWobble = hardwareMap.get(Servo.class, "leftWobble");
        rightWobble = hardwareMap.get(Servo.class, "rightWobble");


        autonLibrary = new AutonLibrary(drivingLibrary, this);
        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("rightWobblePosition", rightWobble.getPosition());
            telemetry.addData("leftWobblePosition", leftWobble.getPosition());
            telemetry.update();
            leftWobble.setPosition(-0.5);
            rightWobble.setPosition(0.92);
            //both down
            sleep(1000);
            telemetry.addData("rightWobblePosition", rightWobble.getPosition());
            telemetry.addData("leftWobblePosition", leftWobble.getPosition());
            telemetry.update();
            //moves left
            drivingLibrary.bevelDrive(.5f, 0, 0);
            sleep(2000);
            drivingLibrary.brakeStop();
            //corrects angle
            while (Math.abs(drivingLibrary.getIMUAngle() - 0) > .1) {
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
            //parked

//                case 0:
//                    //move backwards
//                    drivingLibrary.bevelDrive(0, .7f, 0);
//                    sleep(1000 * 5/7);
//                    drivingLibrary.brakeStop();
//                    leftWobble.setPosition(1);
//                    rightWobble.setPosition(-1);
//                    drivingLibrary.brakeStop();
//                    drivingLibrary.bevelDrive(0, .7f, 0);
//                    sleep(3800 * 5/7);
//                    drivingLibrary.brakeStop();
//                    drivingLibrary.bevelDrive(-.7f, 0, 0);
//                    sleep(3000 * 5/7);
//                    drivingLibrary.brakeStop();
//                    leftWobble.setPosition(-.5);
//                    rightWobble.setPosition(0.92);
//                    sleep(500);
//                    drivingLibrary.brakeStop();
//                    drivingLibrary.bevelDrive(.5f, 0, 0);
//                    sleep(3000);
//                    drivingLibrary.bevelDrive(0, -.5f, 0);
//                    sleep(4000);
//                    drivingLibrary.brakeStop();
//                    //corrects angle
//                    while (Math.abs(drivingLibrary.getIMUAngle() - 0) > .05) {
//                        if (drivingLibrary.getIMUAngle() > 0) { // check which direction we need to turn
//                            drivingLibrary.bevelDrive(0, 0, .1f);
//                        } else {
//                            drivingLibrary.bevelDrive(0, 0, -.1f);
//
//                            ranOnce = true;
//                        }
//
//                    }
//                    drivingLibrary.brakeStop();
//                    leftWobble.setPosition(1.0);
//                    rightWobble.setPosition(-1.0);
//                    drivingLibrary.brakeStop();
//                    break;


//                case 1:
//                    //go forwards
//                    drivingLibrary.bevelDrive(0, -.5f, 0);
//                    sleep(1500);
//                    // go right
//                    drivingLibrary.bevelDrive(-.5f, 0, 0);
//                    sleep(2500);
//                    drivingLibrary.brakeStop();
//                    leftWobble.setPosition(1);
//                    rightWobble.setPosition(-1);
//                    //drops off wobble goal at square B
//                    sleep(500);
//                    drivingLibrary.bevelDrive(-.7f, 0, 0);
//                    sleep(2000 * 5/7);
//                    drivingLibrary.bevelDrive(0,.7f, 0);
//                    sleep(6000 * 5/7);
//                    drivingLibrary.bevelDrive(.7f, 0, 0);
//                    sleep(1000 * 5/7);
//                    leftWobble.setPosition(-.5);
//                    rightWobble.setPosition(0.92);
//                    //picks up second wobble goal
//                    sleep(500);
//                    drivingLibrary.bevelDrive(.5f, 0, 0);
//                    sleep(1000);
//                    drivingLibrary.bevelDrive(0, -.5f, 0);
//                    sleep(5500);
//                    drivingLibrary.bevelDrive(0,.7f, 0);
//                    sleep(1500 * 5/7);
//                    drivingLibrary.brakeStop();
//                    leftWobble.setPosition(1);
//                    rightWobble.setPosition(-1);
//                    break;

                //case 4:
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(3500);
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    drivingLibrary.brakeStop();
                    //move backwards
                    drivingLibrary.bevelDrive(0, .8f, 0);
                    sleep(7500 * 5/8);
                    drivingLibrary.brakeStop();
                    drivingLibrary.bevelDrive(-.8f, 0f, 0);
                    sleep(2800 * 5/8);
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(-0.5);
                    rightWobble.setPosition(0.92);
                    sleep(500);
                    drivingLibrary.brakeStop();
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                    sleep(3000);
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
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(7500);
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    drivingLibrary.brakeStop();
                    //move backwards
                    drivingLibrary.bevelDrive(0, .8f, 0);
                    sleep(2500);
                    drivingLibrary.brakeStop();
//                    break;
            //}
        }
    }
}
