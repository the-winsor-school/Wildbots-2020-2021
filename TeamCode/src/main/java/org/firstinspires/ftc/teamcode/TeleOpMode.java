package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

import java.util.ArrayList;

@TeleOp(name  = "Scrimmage Teleop", group = "Finished")
public class TeleOpMode extends LinearOpMode {
    //drive train
    DrivingLibrary drivingLibrary;
    //AutonLibrary autonLibrary;
    int drivingMode;
    VuforiaLocalizer vuforia;

    boolean toGoal;
    float[] speeds;

    float intakePower = 0.5f;
    float launchPower = .8f;
    float launchTest = .5f;
    boolean intakeOn;

    DcMotor launchMotorLeft;
    DcMotor launchMotorRight;
    DcMotor intakeMotor;
    Servo rightWobble;
    Servo leftWobble;


    ArrayList<VuforiaTrackable> allTrackables = new ArrayList<>();

    public void runOpMode() throws InterruptedException {
        //set up our driving library
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);


        //autonLibrary = new AutonLibrary(drivingLibrary, this);

        launchMotorLeft = hardwareMap.get(DcMotor.class, "launchMotor1");
        launchMotorRight = hardwareMap.get(DcMotor.class, "launchMotor2");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        rightWobble = hardwareMap.get(Servo.class, "rightWobbleGoalArm");
        leftWobble = hardwareMap.get(Servo.class, "leftWobbleGoalArm");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //autonLibrary.targetsUltimateGoal.activate();

        waitForStart();

        while (opModeIsActive()) {
            //driving
            drivingLibrary.bevelDrive(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            // switching braking modes
            if (gamepad1.b) {
                drivingMode++;
                drivingMode %= DrivingMode.values().length;
                drivingLibrary.setMode(drivingMode);
            }

            if (gamepad1.a) {
                if (Math.abs(drivingLibrary.getIMUAngle() + .11) > .05) {
                    if (drivingLibrary.getIMUAngle() > -.11) { // check which direction we need to turn
                        drivingLibrary.bevelDrive(0, 0, .5f);
                    } else {
                        drivingLibrary.bevelDrive(0, 0, -.5f);
                    }
                }
                drivingLibrary.brakeStop();
            }

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
                launchMotorLeft.setPower(launchPower);
                launchMotorRight.setPower(launchPower);
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

            if(gamepad2.dpad_up) {
                if(launchTest < 1.001) {
                    launchTest += .001;
                }
            }
            if(gamepad2.dpad_down) {
                if(launchTest > .001) {
                    launchTest -= .001;
                }
            }

            telemetry.addData("launch power", launchTest);
            telemetry.addData("robot angle", drivingLibrary.getIMUAngle());


            if (gamepad2.right_bumper) {
                rightWobble.setPosition(.75);
                leftWobble.setPosition(0);
            }
            if(gamepad2.left_bumper) {
                rightWobble.setPosition(0);
                leftWobble.setPosition(.75);
            }


            telemetry.addData("Status", "Running");
            telemetry.addData("Brake Mode", drivingLibrary.getMode());


            telemetry.update();
        }

        //autonLibrary.targetsUltimateGoal.deactivate();
    }

    //adb connect 192.168.43.1:5555

}
