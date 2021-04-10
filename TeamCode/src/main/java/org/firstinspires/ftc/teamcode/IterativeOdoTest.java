package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.libraries.DrivingLibrary;

@TeleOp
public class IterativeOdoTest extends OpMode {
    DrivingLibrary drivingLibrary;
    FtcDashboard dashboard;

    double leftTicks;
    double rightTicks;

    double leftDist;
    double rightDist;
    double avgDist;

    final double MM_PER_INCH = 25.4;
    final double TICKS_PER_ROTATION = 8192;

    // runs once on initialization
    @Override
    public void init() {
        dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();
        telemetry.addLine("BAMMMM INITIALIZED");
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1);
        drivingLibrary.setMode(1);
    }

    // runs repeatedly after initialization but before play
    @Override
    public void init_loop() {

    }

    // runs once on play
    @Override
    public void start() {

    }

    // runs repeatedly after play but before stop
    @Override
    public void loop() {
        drivingLibrary.bevelDrive(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        leftTicks = drivingLibrary.getEncoderValues()[0];
        rightTicks = drivingLibrary.getEncoderValues()[1];

        telemetry.addData("Left Encoder Val", leftTicks);
        telemetry.addData("Right Encoder Val", rightTicks);

        // calculates dist travelled, avg = dist forwards in straight line
        leftDist = leftTicks * TICKS_PER_ROTATION * 90 * MM_PER_INCH;
        rightDist = rightTicks * TICKS_PER_ROTATION * 90 * MM_PER_INCH;
        avgDist = (leftDist + rightDist) / 2;

        telemetry.addData("Left Distance Travelled (in)", leftDist);
        telemetry.addData("Right Distance Travelled (in)", rightDist);
        telemetry.addData("Forward (avg) Distance Travelled (in)", avgDist);
    }

    // runs once on stop
    @Override
    public void stop() {

    }
}
