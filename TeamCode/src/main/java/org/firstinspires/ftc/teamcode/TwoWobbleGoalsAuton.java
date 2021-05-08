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
public class TwoWobbleGoalsAuton extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    //variables:
    private static final float crashIntoWall = .50f; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 1000; //Time to get to line
    int numRings;

    Rev2mDistanceSensor distTop;
    //Rev2mDistanceSensor distBot;
    Servo leftWobble;
    Servo rightWobble;
    DcMotorEx launchLeft;
    DcMotorEx launchRight;
    //DcMotor intakeMotor;

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "initialized");
        telemetry.update();

        distTop = hardwareMap.get(Rev2mDistanceSensor.class, "distTop");
        //distBot = hardwareMap.get(Rev2mDistanceSensor.class, "distBot");
        leftWobble = hardwareMap.get(Servo.class, "leftWobble");
        rightWobble = hardwareMap.get(Servo.class, "rightWobble");
        launchLeft = hardwareMap.get(DcMotorEx.class, "launchLeft");
        launchRight = hardwareMap.get(DcMotorEx.class, "launchRight");
        //intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");


        autonLibrary = new AutonLibrary(drivingLibrary, this);
        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("rightWobblePosition", rightWobble.getPosition());
            telemetry.addData("leftWobblePosition", leftWobble.getPosition());
            telemetry.update();
            numRings = 0;
                    //autonLibrary.getStackHeight(distTop);
            leftWobble.setPosition(0.2);
            rightWobble.setPosition(1.0);
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
            //moves forward to park line
            drivingLibrary.bevelDrive(0, -.5f, 0);
            sleep(4000);
            drivingLibrary.brakeStop();
            //parked

            switch (numRings) {
                case 0:
                    //move backwards
                    drivingLibrary.bevelDrive(0, .7f, 0);
                    sleep(1000 * 5/7);
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    drivingLibrary.brakeStop();
                    //releases wobble goal
                    drivingLibrary.bevelDrive(0, .7f, 0);
                    sleep(3800 * 5/7);
                    drivingLibrary.brakeStop();
                    //moves backward
                    drivingLibrary.bevelDrive(-.7f, 0, 0);
                    sleep(3000 * 5/7);
                    //moves to pick up wobble goal
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(-.5);
                    rightWobble.setPosition(0.92);
                    sleep(500);
                    drivingLibrary.brakeStop();
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                    sleep(3000);
                    //crashes against wall
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(4000);
                    drivingLibrary.brakeStop();
                    //drops off wobble goal
                    //corrects angle
                    while (Math.abs(drivingLibrary.getIMUAngle() - 0) > .05) {
                        if (drivingLibrary.getIMUAngle() > 0) { // check which direction we need to turn
                            drivingLibrary.bevelDrive(0, 0, .1f);
                        } else {
                            drivingLibrary.bevelDrive(0, 0, -.1f);

                            ranOnce = true;
                        }

                    }
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(1.0);
                    rightWobble.setPosition(-1.0);
                    drivingLibrary.brakeStop();
                    break;
                case 1:
                    //go forwards
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(1500);
                    // go right
                    drivingLibrary.bevelDrive(-.5f, 0, 0);
                    sleep(2500);
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    //drops off wobble goal at square B
                    sleep(500);
                    //moves to the right
                    drivingLibrary.bevelDrive(-.7f, 0, 0);
                    sleep(2000 * 5/7);
                    //goes back to the wall
                    drivingLibrary.bevelDrive(0,.7f, 0);
                    sleep(6000 * 5/7);
                    //picks up wobble goal 2
                    drivingLibrary.bevelDrive(.7f, 0, 0);
                    sleep(1000 * 5/7);
                    leftWobble.setPosition(-.5);
                    rightWobble.setPosition(0.92);
                    //begins controlling second wobble goal
                    sleep(500);
                    //moves to the left
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                    sleep(1000);
                    //moves forward to square B
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(5500);
                    //parks
                    drivingLibrary.bevelDrive(0,.7f, 0);
                    sleep(1500 * 5/7);
                    drivingLibrary.brakeStop();
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    break;
                case 4:
                    //continues to square C
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(3500);
                    drivingLibrary.brakeStop();
                    //lifts wobble goal arms
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    drivingLibrary.brakeStop();
                    //move backwards
                    drivingLibrary.bevelDrive(0, .8f, 0);
                    sleep(7500 * 5/8);
                    drivingLibrary.brakeStop();
                    //moves right behind the second wobble goal
                    drivingLibrary.bevelDrive(-.8f, 0f, 0);
                    sleep(2800 * 5/8);
                    drivingLibrary.brakeStop();
                    //takes possession of the second wobble goal
                    leftWobble.setPosition(-0.5);
                    rightWobble.setPosition(0.92);
                    sleep(500);
                    drivingLibrary.brakeStop();
                    //crashes against left wall
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                    sleep(3000);
                    drivingLibrary.brakeStop();
                    //corrects angle
                    while (Math.abs(drivingLibrary.getIMUAngle() - 0) > .05) {
                        if (drivingLibrary.getIMUAngle() > 0) { // check which direction we need to turn
                            drivingLibrary.bevelDrive(0, 0, .1f);
                        } else {
                            drivingLibrary.bevelDrive(0, 0, -.1f);

                            ranOnce = true;
                        }

                    }
                    //moves to square C
                    drivingLibrary.bevelDrive(0, -.5f, 0);
                    sleep(7500);
                    drivingLibrary.brakeStop();
                    //stops controlling wobble goal
                    leftWobble.setPosition(1);
                    rightWobble.setPosition(-1);
                    drivingLibrary.brakeStop();
                    //move backwards to park on the line
                    drivingLibrary.bevelDrive(0, .8f, 0);
                    sleep(2500);
                    drivingLibrary.brakeStop();
                    break;
            }
        }
    }
}
