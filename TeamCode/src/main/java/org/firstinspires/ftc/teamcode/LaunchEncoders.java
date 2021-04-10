package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.libraries.DrivingLibrary;

@TeleOp
public class LaunchEncoders extends LinearOpMode {
    DrivingLibrary drivingLibrary;

    DcMotorEx launchMotorLeft;
    DcMotorEx launchMotorRight;
    DcMotor intakeMotor;

    double launchTest = .5;
    double intakePower = .5;
    double launchPower = .8;
    double targetV = 1000;
    PIDFCoefficients leftCoefficients;
    PIDFCoefficients rightCoefficients;

    boolean ranOnce = false;

    @Override
    public void runOpMode() {
        launchMotorLeft = hardwareMap.get(DcMotorEx.class, "launchMotorLeft");
        launchMotorRight = hardwareMap.get(DcMotorEx.class, "launchMotorRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();

        waitForStart();

        while (opModeIsActive()) {
            // intake control
            if(gamepad2.a) {
                intakeMotor.setPower(intakePower);
            } else if(gamepad2.b) {
                intakeMotor.setPower(-intakePower);
                launchMotorLeft.setPower(-.25f);
                launchMotorRight.setPower(-.25f);
            }
            else {
                intakeMotor.setPower(0);
            }

            // launching controls
            if (gamepad2.x) {
                launchMotorLeft.setVelocity(-targetV);
                launchMotorRight.setVelocity(targetV);
                //launchMotorLeft.setPower(launchPower);
                //launchMotorRight.setPower(launchPower);
            }
            else if (gamepad2.y) { // this is here for testing values
                /*launchMotor1.setPower(gamepad2.right_trigger); // might need to become negative
                launchMotor2.setPower(gamepad2.left_trigger);*/

                launchMotorLeft.setPower(launchTest);
                launchMotorRight.setPower(launchTest);
            }
            else {
                launchMotorLeft.setPower(0);
                launchMotorRight.setPower(0);
            }

            if (gamepad2.dpad_up) {
                targetV++;
            }
            else if (gamepad2.dpad_down) {
                targetV--;
            }

            //telemetry.addData("launch power", launchTest);
            telemetry.addData("target velocity", targetV);

            telemetry.addData("Left Motor Velocity", -launchMotorLeft.getVelocity());
            telemetry.addData("Right Motor Velocity", launchMotorRight.getVelocity());
            leftCoefficients = launchMotorLeft.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
            rightCoefficients = launchMotorLeft.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
            telemetry.addData("left PID coefficients", leftCoefficients);
            telemetry.addData("right PID coefficients", rightCoefficients);
            telemetry.update();
            //launchMotorLeft.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
            //launchMotorRight.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
