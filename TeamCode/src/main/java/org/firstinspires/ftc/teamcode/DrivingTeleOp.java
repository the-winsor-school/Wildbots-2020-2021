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

@TeleOp
public class DrivingTeleOp extends LinearOpMode {
    //drive train
    DrivingLibrary drivingLibrary;
    //AutonLibrary autonLibrary;
    int drivingMode;


    public void runOpMode() throws InterruptedException {
        //set up our driving library
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);


        //autonLibrary = new AutonLibrary(drivingLibrary, this);

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

            telemetry.addData("Status", "Running");
            telemetry.addData("Brake Mode", drivingLibrary.getMode());


                telemetry.update();
            }

            //autonLibrary.targetsUltimateGoal.deactivate();
        }

        //adb connect 192.168.43.1:5555

    }