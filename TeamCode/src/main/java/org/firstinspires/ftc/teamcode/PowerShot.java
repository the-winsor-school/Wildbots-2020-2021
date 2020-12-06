package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//Names the Auton -> "name =" appears on the phone
@Autonomous(name = "JustThePowerShot")
public class PowerShot extends LinearOpMode {


    private DrivingLibrary drivingLibrary;
    //target dist is the distance the robot should be from the right wall
    //when it is lined up with power shot 1
    //NOTE: measure what the actual distances are and don't GUESS DUMMY
    private static final double TARGET_1_DISTANCE = 36.0;
    private static final double TARGET_2_DISTANCE = 30.0;
    private static final double TARGET_3_DIATANCE = 24.0;

    //Needs the method "runOpMode" when extending to a LinearOpMode
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "initialized");
        telemetry.update();

        // assume we start lined up with the goal and have the camera
        // and launch mech facing the goal and the robot is behind the
        // launch line

        waitForStart();
        // when the robot is at the wrong distance, move until you reach or pass it
        // get distance from right wall using dist sens
        moveToDistance(TARGET_1_DISTANCE);
        takeShot();
        moveToDistance(TARGET_2_DISTANCE);
        takeShot();
        moveToDistance(TARGET_3_DIATANCE);
        takeShot();
    }

    // when we know how, put in the code for launching
    private void takeShot() {

    }

    private void moveToDistance(double targetDistance) {
        double distance = drivingLibrary.distSenBottom.getDistance(DistanceUnit.INCH);

        while (distance >= targetDistance) {
            //sets the direction robot will move in (Is 1 too fast?)
            drivingLibrary.bevelDrive(1, 0, 0);
            distance = drivingLibrary.distSenBottom.getDistance(DistanceUnit.INCH);
        }
    }
}