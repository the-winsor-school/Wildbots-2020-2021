package org.firstinspires.ftc.teamcode;

import java.util.*;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


    @TeleOp(name  = "TeleOp Controller Mode", group = "Finished")
    public class TeleOpMode extends LinearOpMode {
        //drive train
        DrivingLibrary drivingLibrary;
        AutonLibrary autonLibrary;
        int drivingMode;
        VuforiaLocalizer vuforia;

        //DcMotor launchMotor;
        //DcMotor intakeMotor;

        public void runOpMode() throws InterruptedException {
            //set up our driving library
            drivingLibrary = new DrivingLibrary(this);
            drivingLibrary.setSpeed(1);
            drivingMode = 0;
            drivingLibrary.setMode(drivingMode);

            autonLibrary = new AutonLibrary(drivingLibrary, this);

            //launchMotor = hardwareMap.get(DcMotor.class, "launchMotor");
            //intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

            telemetry.addData("Status", "Initialized");
            telemetry.update();

            waitForStart();

            while (opModeIsActive()) {
                if (gamepad1.b) {
                    drivingMode++;
                    drivingMode %= DrivingMode.values().length;
                    drivingLibrary.setMode(drivingMode);
                }

                //strafe/driving
                drivingLibrary.drive(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

                telemetry.addData("Status", "Running");
                telemetry.addData("Brake Mode", drivingLibrary.getMode());


                /*if (gamepad2.a) {
                    intakeMotor.setPower(1);
                }
                if (gamepad2.right_bumper) {
                    launchMotor.setPower(1);
                }
                if (gamepad1.x) {
                    autonLibrary.lineUpWithGoal();
                }*/

                telemetry.addData( "Status:", "Running");

                telemetry.update();

            }
        }
    }