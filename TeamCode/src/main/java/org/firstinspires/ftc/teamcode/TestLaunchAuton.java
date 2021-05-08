package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled
public class TestLaunchAuton extends LinearOpMode {
    DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    DcMotor launchMotor;
    DcMotor intakeMotor;
    Rev2mDistanceSensor distFront;
    Rev2mDistanceSensor distLeft;
    boolean ranOnce = false;

    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        autonLibrary = new AutonLibrary(drivingLibrary, this);
        launchMotor = hardwareMap.get(DcMotor.class, "launchMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        distFront = hardwareMap.get(Rev2mDistanceSensor.class, "distTop");
        distLeft = hardwareMap.get(Rev2mDistanceSensor.class, "distBot");

        waitForStart();

        while (opModeIsActive()) {
            if (!ranOnce) {
                drivingLibrary.bevelDrive(-.5f, 0, 0); // only put this in case 4 rings
                sleep(1000);
                drivingLibrary.brakeStop();

                while (Math.abs(drivingLibrary.getIMUAngle() - Math.PI / 2) > .1) {
                    if (drivingLibrary.getIMUAngle() > Math.PI / 2) { // check which direction we need to turn
                        drivingLibrary.bevelDrive(0, 0, .5f);
                    } else {
                        drivingLibrary.bevelDrive(0, 0, -.5f);
                    }
                }
                drivingLibrary.brakeStop();

                while (distLeft.getDistance(DistanceUnit.CM) > 65) {
                    drivingLibrary.bevelDrive(.5f, 0, 0);
                }
                drivingLibrary.brakeStop();

                while (distFront.getDistance(DistanceUnit.CM) < 90) {
                    drivingLibrary.bevelDrive(0, .5f, 0);
                }
                drivingLibrary.brakeStop();

                launchMotor.setPower(1);
                sleep(2000);
                intakeMotor.setPower(.5);
            }
            ranOnce = true;
        }
    }
}
