package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

@TeleOp
public class DriveOnlyForReal extends LinearOpMode {
    DrivingLibrary drivingLibrary;
    int drivingMode;
    int leftEncoder;
    int rightEncoder;

    public void runOpMode() throws InterruptedException {
        //set up our driving library
        drivingLibrary = new DrivingLibrary(this);
        //max speed ((1 / distance of joy stick from 0)) x the % of motor powers)
        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);
        //telemetry prints to the driver phone
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            //gamepad1.b = b button on 1st controller
            if (gamepad1.b) {
                drivingMode++;
                drivingMode %= DrivingMode.values().length;
                drivingLibrary.setMode(drivingMode);
            }

            leftEncoder = drivingLibrary.rightFront.getCurrentPosition();
            rightEncoder = drivingLibrary.rightRear.getCurrentPosition();
            //sends text to driver phone
            telemetry.addData("Status", "Running");
            telemetry.addData("Left", leftEncoder);
            telemetry.addData("Right", rightEncoder);
            telemetry.addData("Brake Mode", drivingLibrary.getMode());
            telemetry.update();

            drivingLibrary.bevelDrive(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        }
    }


}
