package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.libraries.DrivingLibrary;

@Autonomous
public class AutonLunch extends LinearOpMode {
    private DrivingLibrary drivingLibrary;

    // variables
    double lunchVel = 825;
    boolean atSpeed = false;
    int ringsLunched = 0;



    int drivingMode;
    DcMotorEx lunchLeft;
    DcMotorEx lunchRight;
    //Servo intakeHelp;
    DcMotor intakeMotor;


    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);

        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);

        lunchLeft = hardwareMap.get(DcMotorEx.class, "launchMotorLeft");
        lunchRight = hardwareMap.get(DcMotorEx.class, "launchMotorRight");
        //intakeHelp = hardwareMap.get(Servo.class, "intakeHelp");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        telemetry.addData("GUESS WHAT?!?", "INITIALIZED AHHH");
        telemetry.update();

        boolean ranOnce = false;

        waitForStart();

        while (opModeIsActive() && !ranOnce) {
            //move left
            drivingLibrary.bevelDrive(-.5f, 0, 0);
            sleep(2000);
            drivingLibrary.brakeStop();
            //corrects angle
            while (Math.abs(drivingLibrary.getIMUAngle() - 0) > .05) {
                if (drivingLibrary.getIMUAngle() > 0) { // check which direction we need to turn
                    drivingLibrary.bevelDrive(0, 0, .1f);
                } else {
                    drivingLibrary.bevelDrive(0, 0, -.1f);

                }

            }

            drivingLibrary.bevelDrive(0, .5f, 0);
            sleep(3750);
            drivingLibrary.brakeStop();

            //move right
            drivingLibrary.bevelDrive(.5f, 0, 0);
            sleep(2375);
            drivingLibrary.brakeStop();

            // runs until 3 rings have been launched
            while (ringsLunched < 4) {
                // turns on launcher motors
                lunchLeft.setVelocity(-lunchVel);
                lunchRight.setVelocity(lunchVel);

                // launches when motors are at a high enough velocity
                if (lunchLeft.getVelocity() <= -(lunchVel - 20) &&
                        /*lunchLeft.getVelocity() >= -(lunchVel + 20) &&*/
                        lunchRight.getVelocity() >= (lunchVel - 20) /*&&*/
                        /*lunchRight.getVelocity() <= (lunchVel + 20)*/) {

                    intakeMotor.setPower(-0.5);
                    atSpeed = true;

                } else {
                    intakeMotor.setPower(0);

                    // registers when a ring has been launched and resets the atSpeed variable
                    if (atSpeed) {
                        ringsLunched++;
                        atSpeed = false;
                    }
                }
            }

            drivingLibrary.bevelDrive(0, .5f, 0);
            sleep(375);
            drivingLibrary.brakeStop();

            ranOnce = true;
        }
    }
}
