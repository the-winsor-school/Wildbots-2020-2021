package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;

@Autonomous
public class LowGoalAuton extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line
    int numRings;

    Rev2mDistanceSensor distTop;
    Servo leftWobble;
    Servo rightWobble;
    DcMotor launchMotorLeft;
    DcMotor launchMotorRight;
    DcMotor intakeMotor;

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "initialized");
        telemetry.update();

        distTop = hardwareMap.get(Rev2mDistanceSensor.class, "distTop");
        leftWobble = hardwareMap.get(Servo.class, "leftWobbleGoalArm");
        rightWobble = hardwareMap.get(Servo.class, "rightWobbleGoalArm");
        launchMotorLeft = hardwareMap.get(DcMotor.class, "launchMotor1");
        launchMotorRight = hardwareMap.get(DcMotor.class, "launchMotor2");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");


        autonLibrary = new AutonLibrary(drivingLibrary, this);
        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        if (opModeIsActive()) {
            drivingLibrary.bevelDriveCorrect(0, .5f, 0);
            sleep(10000);
            drivingLibrary.brakeStop();
            launchMotorLeft.setPower(.3f);
            launchMotorRight.setPower(.3f);
            sleep(1000);
            intakeMotor.setPower(-.5f);
            sleep(6000);
            launchMotorLeft.setPower(0f);
            launchMotorRight.setPower(0f);
            intakeMotor.setPower(0f);
            drivingLibrary.bevelDriveCorrect(0, -.5f, 0);
            sleep(4500);
            drivingLibrary.brakeStop();
        }
    }
}