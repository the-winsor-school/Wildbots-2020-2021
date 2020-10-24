package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;

import java.util.Arrays;

//class name
@TeleOp(name  = "Spinny Motor", group = "Finished")
public class DriveLaunchTeleOp extends LinearOpMode {
    //drive train
    DrivingLibrary drivingLibrary;
    int drivingMode;
    DcMotor intake;


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

        intake = hardwareMap.get(DcMotor.class, "intake");
        waitForStart();

        while (opModeIsActive()) {
            //gamepad1.b = b button on 1st controller
            if (gamepad1.b) {
                drivingMode++;
                drivingMode %= DrivingMode.values().length;
                drivingLibrary.setMode(drivingMode);
            }

            drivingLibrary.bevelDrive(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

            //sends text to driver phone
            telemetry.addData("Status", "Running");
            telemetry.addData("Brake Mode", drivingLibrary.getMode());
            drivingLibrary.printEncoderValues();

            if (gamepad1.x) {
                int[] values = drivingLibrary.getEncoderValues();
                telemetry.log().add("Encoder values(fl, fr, rl, rr)" + Arrays.toString(values));
            }
            //intake is the spinny taking in the ring and flinging it

            //if button b on second controller is pressed, go full power
            if (gamepad2.b) {
                intake.setPower(1);
            }
            //if anything else, the spinny does not run
            else {

                intake.setPower(0);

            }

        }

        telemetry.update();




    }
}






