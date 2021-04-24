package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;

@TeleOp
public class NewTeleOp extends LinearOpMode {
    DrivingLibrary drivingLibrary;
    int drivingMode;

    DcMotorEx launchMotorLeft;
    DcMotorEx launchMotorRight;
    DcMotor intakeMotor;
    Servo rightWobble;
    Servo leftWobble;

    float intakePower = .5f;
    double launchVel = 1000;
    double highGoalVel = 1250;
    double powerShotVel = 850;
    // TARGET VELOCITY FOR HIGH GOAL: 1250
    // TARGET VELOCITY FOR POWER SHOT: 800

    boolean launchMode = false;
    boolean intakeOn = false;
    boolean powerShot = false;
    int speedHeld = 0;
    int speedRange = 20;

    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingMode = 0;
        drivingLibrary.setSpeed(1);
        drivingLibrary.setMode(drivingMode);

        launchMotorLeft = hardwareMap.get(DcMotorEx.class, "launchMotorLeft");
        launchMotorRight = hardwareMap.get(DcMotorEx.class, "launchMotorRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        // rightWobble = hardwareMap.get(Servo.class, "rightWobbleGoalArm");
        // leftWobble = hardwareMap.get(Servo.class, "leftWobbleGoalArm");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            drivingLibrary.bevelDrive(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            if (gamepad1.b) {
                drivingMode++;
                drivingMode %= DrivingMode.values().length;
                drivingLibrary.setMode(drivingMode);
            }

            if(gamepad2.dpad_left) {
                powerShot = false;
                // launchVel -= 1;
                //powerShotVel -= 1;
            }
            else if(gamepad2.dpad_right) {
                powerShot = true;
                // launchVel += 1;
                //powerShotVel += 1;
            }

            telemetry.addData("Power Shot Mode", powerShot);
            telemetry.addData("launch val", launchVel);
            telemetry.update();

            if (launchMode) {
                if(powerShot) {
                    launchVel = powerShotVel;
                }
                else {
                    launchVel = highGoalVel;
                }

                launchMotorLeft.setVelocity(-launchVel);
                launchMotorRight.setVelocity(launchVel);

                if (intakeOn &&
                        launchMotorLeft.getVelocity() <= -(launchVel - speedRange) &&
                        launchMotorLeft.getVelocity() >= -(launchVel + speedRange) &&
                        launchMotorRight.getVelocity() >= (launchVel - speedRange) &&
                        launchMotorRight.getVelocity() <= (launchVel + speedRange)) {
                    speedHeld++;
                    if(speedHeld >= 5) {
                        intakeMotor.setPower(-intakePower);
                    }
                    if (gamepad2.x) {
                        intakeOn = false;
                    }
                } else if (gamepad2.a &&
                        launchMotorLeft.getVelocity() <= -(launchVel - speedRange) &&
                        launchMotorLeft.getVelocity() >= -(launchVel + speedRange) &&
                        launchMotorRight.getVelocity() >= (launchVel - speedRange) &&
                        launchMotorRight.getVelocity() <= (launchVel + speedRange)) {
                    speedHeld++;
                    if(speedHeld >= 5) {
                        intakeMotor.setPower(-intakePower);
                    }
                } else if (gamepad2.b) {
                    intakeMotor.setPower(intakePower);
                } else {
                    speedHeld = 0;
                    intakeMotor.setPower(0);
                }

                if (gamepad2.x) {
                    intakeOn = true;
                }

                if (gamepad2.dpad_down) {
                    intakeOn = false;
                    launchMode = false;
                }
            } else {
                if (gamepad2.a) {
                    intakeMotor.setPower(-intakePower);
                } else if (gamepad2.b) {
                    intakeMotor.setPower(intakePower);
                    launchMotorLeft.setVelocity(450);
                    launchMotorRight.setVelocity(-450);
                } else {
                    intakeMotor.setPower(0);
                }

                // note that these values need to be re-tested
                /*if (gamepad2.right_bumper) {
                    rightWobble.setPosition(.75);
                    leftWobble.setPosition(0);
                }
                else if(gamepad2.left_bumper) {
                    rightWobble.setPosition(0);
                    leftWobble.setPosition(.75);
                }*/

                launchMotorRight.setVelocity(0);
                launchMotorLeft.setVelocity(0);

                if (gamepad2.dpad_up) {
                    launchMode = true;
                }
            }
        }
    }
}
