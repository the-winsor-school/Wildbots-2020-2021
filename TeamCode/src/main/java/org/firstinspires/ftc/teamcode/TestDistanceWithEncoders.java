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
        telemetry.addData("status", "initilized");
        telemetry.update();
        drivingLibrary.resetEncoderValues();
        drivingLibrary.setEncoders(12);
        drivingLibrary.setRunMode(true);
        int[] values = drivingLibrary.getEncoderValues();
        drivingLibrary.setRecordEncoderTable();

        double deltaX;
        double deltaY;
        double xPos=0;
        double yPos=0;

        waitForStart();
        while(opModeIsActive() && drivingLibrary.motorsBusy()){
            drivingLibrary.bevelDrive(0, -.75f, 0);

            values=drivingLibrary.getEncoderValues();

            deltaX= drivingLibrary.getDeltaX();
            xPos+=deltaX;
            deltaY=drivingLibrary.getDeltaY();
            yPos+=deltaY;
            values=drivingLibrary.getEncoderValues();
            telemetry.addData("xpos:", xPos);
            telemetry.addData("yPos", yPos);
            telemetry.addData("encoder values:", Arrays.toString(values));
            telemetry.update();
        }
        drivingLibrary.brakeStop();


    }
}
