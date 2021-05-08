package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.libraries.DrivingLibrary;

@Autonomous
public class NewPowerShot extends LinearOpMode {
    DrivingLibrary drivingLibrary;
    int drivingMode;

    DcMotorEx lunchLeft;
    DcMotorEx lunchRight;
    DcMotor intakeMotor;

    double lunchVel = 975;
    boolean atSpeed = false;
    int ringsLunched = 0;

    boolean ranOnce = false;

    public void turnToAngle(double angle){
        while (Math.abs(drivingLibrary.getIMUAngle() - angle) > .06) {
            if (drivingLibrary.getIMUAngle() > angle) { // check which direction we need to turn
                drivingLibrary.bevelDriveCorrect(0, 0, .5f);
            } else {
                drivingLibrary.bevelDriveCorrect(0, 0, -.5f);
            }
        }

        drivingLibrary.brakeStop();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        drivingLibrary.setMode(1);

        lunchLeft = hardwareMap.get(DcMotorEx .class, "launchMotorLeft");
        lunchRight = hardwareMap.get(DcMotorEx.class, "launchMotorRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        waitForStart();

        while(opModeIsActive() && !ranOnce) {
            turnToAngle(.08);
            //sleep(3000);

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

                } else if (lunchLeft.getVelocity() >= -(lunchVel - 60) && lunchRight.getVelocity() <= (lunchVel - 60)){
                    intakeMotor.setPower(0);

                    // registers when a ring has been launched and resets the atSpeed variable
                    if (atSpeed) {
                        sleep(1500);
                        ringsLunched++;
                        turnToAngle(-.08 * (ringsLunched - 1));
                        atSpeed = false;
                    }
                }
            }
            ranOnce = true;
        }
    }
}
