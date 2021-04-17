package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous
public class OneWobbleGoalShootingAuton extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line

    Rev2mDistanceSensor distTop;
    //Rev2mDistanceSensor distBot;
    Servo leftWobble;
    Servo rightWobble;
    int drivingMode;
    DcMotorEx lunchLeft;
    DcMotorEx lunchRight;
    Servo intakeHelp;
    DcMotor intakeMotor;

    //lunchVel is for the high goal??? unsure
    double lunchVel = 1250;
    boolean atSpeed = false;
    int ringsLunched = 0;
    int speedHeld = 0;
    int speedRange = 20;
    float intakePower = 0.5f;
    int numRings;
    //how long a square is :T
    int square = 24;
    int halfSquare = 12;

    //DcMotor launchMotor;
    //DcMotor intakeMotor;

    //initializing
    //@Override
    //drives forwards using encoders. Note: do not need to make motorPower negative, targetDist is in inches
    public void driveForwards(double targetDist, float motorPower) {
        drivingLibrary.resetEncoderValues();
        while (drivingLibrary.getDistTravelled() < targetDist) {
            drivingLibrary.bevelDrive(0, -motorPower, 0);
            telemetry.addData("Target dist", targetDist);
            telemetry.addData("Dist travelled", drivingLibrary.getDistTravelled());
            telemetry.update();
        }
        drivingLibrary.brakeStop();
    }

    //drives backwards using encoders
    public void driveBackwards(double targetDist, float motorPower) {
        drivingLibrary.resetEncoderValues();
        while (drivingLibrary.getDistTravelled() > -targetDist) {
            drivingLibrary.bevelDrive(0, motorPower, 0);
            telemetry.addData("Target dist", targetDist);
            telemetry.addData("Dist travelled", drivingLibrary.getDistTravelled());
            telemetry.update();
        }
        drivingLibrary.brakeStop();
    }

    //sets both servos to their respective up position
    public void servosUp() {
        leftWobble.setPosition(1);
        rightWobble.setPosition(0);
    }

    //sets both servos to their respective down position
    public void servosDown() {
        leftWobble.setPosition(0);
        rightWobble.setPosition(1.0);
    }

    public void launchThreeRingsAndPark(){
        drivingLibrary.spinToAngle(Math.PI);
        // runs until 3 rings have been launched
        while (ringsLunched < 4) {
            // turns on launcher motors
            lunchLeft.setVelocity(-lunchVel);
            lunchRight.setVelocity(lunchVel);

            // launches when motors are at a high enough velocity
            if(lunchLeft.getVelocity() <= -(lunchVel - speedRange) &&

                    lunchRight.getVelocity() >= (lunchVel - speedRange)
                    ) {
                speedHeld++;

                if(speedHeld >= 5) {
                    intakeMotor.setPower(-intakePower);
                    atSpeed = true;
                }

            } else {
                speedHeld = 0;
                intakeMotor.setPower(0);

                // registers when a ring has been launched and resets the atSpeed variable
                if (atSpeed) {
                    ringsLunched++;
                    atSpeed = false;
                }
            }
        }
        lunchRight.setVelocity(0);
        lunchLeft.setVelocity(0);

        // moves the intake helper to position
        drivingLibrary.resetEncoderValues();
        while (drivingLibrary.getDistTravelled() > -4) {
            drivingLibrary.bevelDrive(0, 0.5f, 0);
        }
        drivingLibrary.brakeStop();
        // intakeHelp.setPosition(-0.5f);
    }
    public void turnToAngle(double angle){
        while (Math.abs(drivingLibrary.getIMUAngle() - angle) > .1) {
            if (drivingLibrary.getIMUAngle() > angle) { // check which direction we need to turn
                drivingLibrary.bevelDriveCorrect(0, 0, .5f);
            } else {
                drivingLibrary.bevelDriveCorrect(0, 0, -.5f);
            }
        }
    }

    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        drivingLibrary.setMode(1);
        distTop = hardwareMap.get(Rev2mDistanceSensor.class, "distTop");
        //distBot = hardwareMap.get(Rev2mDistanceSensor.class, "distBot");
        leftWobble = hardwareMap.get(Servo.class, "leftWobble");
        rightWobble = hardwareMap.get(Servo.class, "rightWobble");
        //launch stuff
        lunchLeft = hardwareMap.get(DcMotorEx .class, "launchMotorLeft");
        lunchRight = hardwareMap.get(DcMotorEx.class, "launchMotorRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        telemetry.addData("status", "initialized");
        telemetry.update();

        autonLibrary = new AutonLibrary(drivingLibrary, this);
        //ensures that the code will only run once
        boolean ranOnce = false;
        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("rightWobblePosition", rightWobble.getPosition());
            telemetry.addData("leftWobblePosition", leftWobble.getPosition());
            telemetry.update();
            //numRings = autonLibrary.getStackHeight(distTop);\

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
                    driveForwards(2 * square + halfSquare, 0.5f);
                    //parked
                    //move backwards
                    driveBackwards(square, 0.7f);

                    servosUp();
                    //releases wobble goal

                    //goes right(?) for [number] squares
                    turnToAngle(-(Math.PI)/2);
                    driveForwards(18, 0.7f);

                    turnToAngle(-Math.PI);
                    driveBackwards(8, 0.7f);

                    launchThreeRingsAndPark();
                    break;

                case 1:
                    //go forwards
                    driveForwards(3.5 * square, 0.5f);

                    // go right
                    drivingLibrary.bevelDrive(-.5f, 0, 0);
                    sleep(2500);
                    drivingLibrary.brakeStop();
                    servosUp();

                    //drops off wobble goal at square B
                    sleep(500);
                    driveBackwards(square, 0.7f);

                    turnToAngle(-Math.PI);

                    //launchThreeRingsAndPark();
                    break;
                case 4:
                    //continues to square C
                    driveForwards(4.5 * square, 0.5f);

                    //lifts wobble goal arms
                    servosUp();

                    //move backwards
                    driveBackwards(2.5 * square, 1f);

                    //go to shooting location
                    turnToAngle(-(Math.PI)/2);
                    driveForwards(10, 0.7f);

                    turnToAngle(-(Math.PI));
                    driveBackwards(halfSquare, 0.7f);

                    //launchThreeRingsAndPark();

                    break;
            }
        }
    }
}
