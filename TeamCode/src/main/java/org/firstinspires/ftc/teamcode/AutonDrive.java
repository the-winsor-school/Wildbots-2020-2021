package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Autonomous(name = "PARK")
public class AutonDrive extends LinearOpMode {

    Rev2mDistanceSensor DistSenTop;
    Rev2mDistanceSensor DistSenBottom;

    DrivingLibrary drivingLibrary;
    AutonLibrary autonLibrary;
    int drivingMode;


    @Override
    public void runOpMode() throws InterruptedException {

        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);
        autonLibrary = new AutonLibrary(drivingLibrary, this);

        boolean ranOnce = false;


        telemetry.addData("status", "initialized");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            if (!ranOnce) {
                if (autonLibrary.getStackHeight(DistSenTop, DistSenBottom) == 0){
                    drivingLibrary.bevelDrive(0, .50f, (float)0.72);
                    sleep(2000);
                    drivingLibrary.brakeStop();
                    drivingLibrary.spinToAngle(Math.PI/2);
                } else if (autonLibrary.getStackHeight(DistSenTop, DistSenBottom) == 1){
                    drivingLibrary.bevelDrive(0, .50f, (float)0.72);
                    sleep(3000);
                    drivingLibrary.brakeStop();
                    drivingLibrary.spinToAngle(Math.PI/2);
                    drivingLibrary.bevelDrive(0, .50f, (float)0.72);
                    sleep(1000);
                } else if (autonLibrary.getStackHeight(DistSenTop, DistSenBottom) == 4){
                    drivingLibrary.bevelDrive(0, .50f,(float)0.72);
                    sleep(5000);
                    drivingLibrary.brakeStop();
                    drivingLibrary.spinToAngle(Math.PI/2);
                }
                ranOnce = true;
            }
        }
    }
}