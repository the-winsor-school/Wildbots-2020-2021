package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@Autonomous
public class TestAutonLibrary extends LinearOpMode {
    DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    float[] speeds;


    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        autonLibrary = new AutonLibrary(drivingLibrary, this);
        autonLibrary.goalReached = false;
        drivingLibrary.setSpeed(1);
        drivingLibrary.setMode(1);

        waitForStart();

        autonLibrary.targetsUltimateGoal.activate();

        while(!isStopRequested() && !autonLibrary.goalReached) {
            speeds = autonLibrary.lineUpWithGoal();
            drivingLibrary.bevelDrive(speeds[0], speeds[1], 0);
        }
        autonLibrary.targetsUltimateGoal.deactivate();
    }

}
