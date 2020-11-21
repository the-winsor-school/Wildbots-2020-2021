package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.DrivingLibrary;

import java.util.Arrays;

@Autonomous
public class testEncoderAuton extends LinearOpMode {
    DrivingLibrary drivingLibrary;
    int drivingMode;

    boolean ranOnce = false;

    @Override
    public void runOpMode() throws InterruptedException {

        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);
        boolean ranOnce = false;

        waitForStart();
        if (opModeIsActive()) {
            if (!ranOnce) {
                int[] values = drivingLibrary.getEncoderValues();
                telemetry.log().add("Start encoder values(fl, fr, rl, rr)"+ Arrays.toString(values));
                telemetry.update();

                //positive y value to drive backwards
                drivingLibrary.bevelDrive(0, -.75f, 0);
                sleep(1000);
                drivingLibrary.brakeStop();

                values = drivingLibrary.getEncoderValues();
                telemetry.log().add("End encoder values(fl, fr, rl, rr)"+ Arrays.toString(values));
                telemetry.update();
                ranOnce = true;
            }
        }
    }
}
