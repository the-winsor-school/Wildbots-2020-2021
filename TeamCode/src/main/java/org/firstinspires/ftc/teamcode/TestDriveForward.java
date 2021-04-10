package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.libraries.DrivingLibrary;

@TeleOp
public class TestDriveForward extends LinearOpMode {
    DrivingLibrary drivingLibrary;

    boolean ranOnce = false;


    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1);
        drivingLibrary.setMode(1);

        //FtcDashboard dashboard = FtcDashboard.getInstance();
        //telemetry = dashboard.getTelemetry();

        waitForStart();

        while (opModeIsActive() && !ranOnce) {
            drivingLibrary.resetEncoderValues();

            double leftVal = drivingLibrary.getEncoderValues()[0];
            double rightVal = drivingLibrary.getEncoderValues()[1];

            double avgDist = (100 * Math.PI / 25.4) * (leftVal + rightVal) / 2 / 8192;

            telemetry.addData("distance", avgDist);
            telemetry.update();

            while (avgDist < 12) {
                drivingLibrary.bevelDrive(0, -.5f, 0);
                leftVal = drivingLibrary.getEncoderValues()[0];
                rightVal = drivingLibrary.getEncoderValues()[1];
                avgDist = (100 * Math.PI / 25.4) * (leftVal + rightVal) / 2 / 8192;
                telemetry.addData("Left Encoder Value", leftVal);
                telemetry.addData("Right Encoder Value", rightVal);
                //telemetry.addData("Distance Travelled", avgDist);
                telemetry.update();
            }

            drivingLibrary.brakeStop();
            ranOnce = true;
        }
    }
}
