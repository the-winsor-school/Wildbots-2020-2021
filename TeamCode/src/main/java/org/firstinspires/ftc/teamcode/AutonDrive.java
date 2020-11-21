package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;


@Autonomous(name = "PARK")
public class AutonDrive extends LinearOpMode {

    DrivingLibrary drivingLibrary;
    int drivingMode;


    @Override
    public void runOpMode() throws InterruptedException {

        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);

        boolean ranOnce = false;


        telemetry.addData("status", "initialized");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            if (!ranOnce) {
                drivingLibrary.bevelDrive(0, .50f,(float)0.72);
                sleep(5000);
                drivingLibrary.brakeStop();
                drivingLibrary.spinToAngle(Math.PI/2);
                ranOnce = true;
            }
        }
    }
}