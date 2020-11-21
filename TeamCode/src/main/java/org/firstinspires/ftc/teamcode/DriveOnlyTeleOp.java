package org.firstinspires.ftc.teamcode;

//imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;

import java.util.Arrays;


//class name
@TeleOp(name  = "TeleOp Mode", group = "Finished")
public class DriveOnlyTeleOp extends LinearOpMode {
    //drive train
    DrivingLibrary drivingLibrary;
    int drivingMode;
    int ranOnce = 0;



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

        while (opModeIsActive()) {
            if (ranOnce==0) {
                //gamepad1.b = b button on 1st controller
                if (gamepad1.b) {
                    drivingMode++;
                    drivingMode %= DrivingMode.values().length;
                    drivingLibrary.setMode(drivingMode);
                }
                drivingLibrary.resetEncoderValues();

                //sends text to driver phone
                telemetry.addData("Status", "Running");
                telemetry.addData("Brake Mode", drivingLibrary.getMode());

                int[] values = drivingLibrary.getEncoderValues();
                telemetry.log().add("Start encoder values(fl, fr, rl, rr)" + Arrays.toString(values));
                telemetry.update();

            //sends text to driver phone
            telemetry.addData("Status", "Running");

            telemetry.addData("Brake Mode", drivingLibrary.getMode());
            drivingLibrary.printEncoderValues();
                //drivingLibrary.bevelDrive(0, -.75f, 0);
                //sleep(1600);
                drivingLibrary.setEncoders(1);
                while(opModeIsActive() && drivingLibrary.motorsBusy()==true){

                }

                drivingLibrary.brakeStop();
                //drivingLibrary.printEncoderValues();

                values = drivingLibrary.getEncoderValues();
                telemetry.log().add("End encoder values(fl, fr, rl, rr)" + Arrays.toString(values));
                telemetry.update();
                ranOnce=1;
            }







        }
    }
}
