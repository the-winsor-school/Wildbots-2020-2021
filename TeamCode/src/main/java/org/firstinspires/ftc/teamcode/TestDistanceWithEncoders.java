package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.DrivingLibrary;

import java.util.Arrays;

@Autonomous
public class TestDistanceWithEncoders extends LinearOpMode {
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

        drivingLibrary.resetEncoderValues();
        drivingLibrary.setEncoders(12);
        drivingLibrary.setRunMode(true);

        waitForStart();
        while(opModeIsActive() && drivingLibrary.motorsBusy()){
            drivingLibrary.bevelDrive(0, -.75f, 0);
        }
        drivingLibrary.brakeStop();
    }
}
