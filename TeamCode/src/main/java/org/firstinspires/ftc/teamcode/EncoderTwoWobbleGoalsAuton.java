package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous
public class EncoderTwoWobbleGoalsAuton extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line
    int numRings;
    //how long a square is :T
    int square = 24;
    int halfSquare=12;

    Rev2mDistanceSensor distTop;
    //Rev2mDistanceSensor distBot;
    Servo leftWobble;
    Servo rightWobble;
    //DcMotor launchMotor;
    //DcMotor intakeMotor;

    //initializing
    //@Override
    //drives forwards using encoders. Note: do not need to make motorPower negative, targetDist is in inches
    public void driveForwards(double targetDist, float motorPower){
        drivingLibrary.resetEncoderValues();
        while(drivingLibrary.getDistTravelled() < targetDist) {
            drivingLibrary.bevelDrive(0, -motorPower, 0);
        }
        drivingLibrary.brakeStop();
    }
    //drives backwards using encoders
    public void driveBackwards(double targetDist, float motorPower){
        drivingLibrary.resetEncoderValues();
        while(drivingLibrary.getDistTravelled() > -targetDist) {
            drivingLibrary.bevelDrive(0, motorPower, 0);
        }
        drivingLibrary.brakeStop();
    }
    //sets both servos to their respective up position
    public void servosUp(){
        leftWobble.setPosition(1);
        rightWobble.setPosition(0);
    }
    //sets both servos to their respective down position
    public void servosDown(){
        leftWobble.setPosition(0);
        rightWobble.setPosition(1.0);
    }

    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        drivingLibrary.setMode(1);
        telemetry.addData("status", "initialized");
        telemetry.update();

        distTop = hardwareMap.get(Rev2mDistanceSensor.class, "distTop");
        //distBot = hardwareMap.get(Rev2mDistanceSensor.class, "distBot");
        leftWobble = hardwareMap.get(Servo.class, "leftWobble");
        rightWobble = hardwareMap.get(Servo.class, "rightWobble");
        //launchMotor = hardwareMap.get(DcMotor.class, "launchMotor");
        //intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");


        autonLibrary = new AutonLibrary(drivingLibrary, this);
        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("rightWobblePosition", rightWobble.getPosition());
            telemetry.addData("leftWobblePosition", leftWobble.getPosition());
            telemetry.update();
            //numRings = autonLibrary.getStackHeight(distTop);\
            numRings=4;
            sleep(500);
            servosDown();
            //both down
            sleep(1000);
            telemetry.addData("rightWobblePosition", rightWobble.getPosition());
            telemetry.addData("leftWobblePosition", leftWobble.getPosition());
            telemetry.update();
            //moves left
            drivingLibrary.bevelDrive(.5f, 0, 0);
            sleep(2000);
            drivingLibrary.brakeStop();
            //corrects angle
            while (Math.abs(drivingLibrary.getIMUAngle() - 0) > .1) {
                if (drivingLibrary.getIMUAngle() > 0) { // check which direction we need to turn
                    drivingLibrary.bevelDrive(0, 0, .1f);
                } else {
                    drivingLibrary.bevelDrive(0, 0, -.1f);

                    ranOnce = true;
                }
            }



            switch (numRings) {
                case 0:
                    //moves forward to park line
                    driveForwards(2*square+halfSquare, 0.5f);
                    //parked
                    //move backwards
                    driveBackwards(halfSquare, 0.7f);

                    servosUp();
                    //releases wobble goal

                    driveBackwards(1.9*square,0.7f);
                    //moves backward

                    //goes right(?) for [number] squares
                    drivingLibrary.bevelDrive(-.7f, 0, 0);
                    sleep(3000 * 5/7);
                    //moves to pick up wobble goal
                    drivingLibrary.brakeStop();

                    driveForwards(4, 0.5f);
                    servosDown();
                    sleep(500);
                    drivingLibrary.brakeStop();

                    //goes left(?) for [number] of squares
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                    sleep(3000);

                    //crashes against wall
                    driveForwards(2.5*square, 0.5f);

                    //drops off wobble goal
                    driveBackwards(halfSquare, 0.75f);

                    drivingLibrary.bevelDrive(-.5f, 0, 0);
                    sleep(2000);
                    driveForwards(halfSquare, 0.75f);


                    servosUp();
                    drivingLibrary.brakeStop();
                    break;

                case 1:
                    //go forwards
                    driveForwards(3.5*square, 0.5f);

                    // go right
                    drivingLibrary.bevelDrive(-.5f, 0, 0);
                    sleep(2500);
                    drivingLibrary.brakeStop();
                    servosUp();
                    //drops off wobble goal at square B
                    sleep(500);
                    driveBackwards(halfSquare, 0.7f);
                    //moves to the left
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                    sleep(2500);
                    drivingLibrary.brakeStop();
                    //goes back to the wall
                    driveBackwards(3*square, 0.7f);

                    // go right
                    drivingLibrary.bevelDrive(-.7f, 0, 0);
                    sleep(2200);
                    drivingLibrary.brakeStop();
                    //picks up wobble goal 2
                    sleep(1000 * 5/7);
                    servosDown();
                    //begins controlling second wobble goal
                    sleep(500);
                    //moves to the left
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                    sleep(1000);
                    //moves forward to square B
                    driveForwards(3.5*square,0.5f);
                    //parks
                    driveBackwards(1.5*square, 0.7f);
                    drivingLibrary.bevelDrive(0,.7f, 0);
                    sleep(1500 * 5/7);
                    drivingLibrary.brakeStop();
                    servosUp();
                    break;
                case 4:
                    //continues to square C
                    driveForwards(4.5*square,0.5f);

                    //lifts wobble goal arms
                    servosUp();
                    drivingLibrary.brakeStop();
                    //move backwards
                    driveBackwards(4.5*square,1f);

                    //moves right behind the second wobble goal
                    drivingLibrary.bevelDrive(-.8f, 0f, 0);
                    sleep(2800 * 5/8);
                    drivingLibrary.brakeStop();
                    //takes possession of the second wobble goal
                    servosDown();
                    sleep(500);
                    drivingLibrary.brakeStop();
                    //crashes against left wall
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                    sleep(3000);
                    drivingLibrary.brakeStop();


                    //moves to square C
                    driveForwards(4.5*square, 0.5f);
                    //stops controlling wobble goal
                    servosUp();
                    drivingLibrary.brakeStop();
                    //move backwards to park on the line
                    driveBackwards(2*square,0.8f);
                    drivingLibrary.bevelDrive(-0.8f,0,0);
                    sleep(1500);
                    drivingLibrary.brakeStop();
                    break;
            }
        }
    }
}
