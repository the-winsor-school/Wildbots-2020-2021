package org.firstinspires.ftc.teamcode;

//imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;

import java.util.Arrays;


//class name
@TeleOp(name  = "TeleOp Mode", group = "Finished")
public class DriveOnlyTeleOp extends LinearOpMode {
    //drive train
    DrivingLibrary drivingLibrary;
    int drivingMode;



    public void runOpMode() throws InterruptedException {
        //set up our driving library
        drivingLibrary = new DrivingLibrary(this);
        //max speed ((1 / distance of joy stick from 0)) x the % of motor powers)
        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);
        //telemetry prints to the driver phone
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            //gamepad1.b = b button on 1st controller
            if (gamepad1.b) {
                drivingMode++;
                drivingMode %= DrivingMode.values().length;
                drivingLibrary.setMode(drivingMode);
            }

            drivingLibrary.bevelDrive(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

/*<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/driveOnlyteleOp.java
=======
            //sends text to driver phone
>>>>>>> 72f4f270501388f86ff259d0d7b76b12b5978f25:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/DriveOnlyTeleOp.java*/
            telemetry.addData("Status", "Running");
            telemetry.addData("Brake Mode", drivingLibrary.getMode());
            drivingLibrary.printEncoderValues();

            if (gamepad1.x){
                int[] values = drivingLibrary.getEncoderValues();
                telemetry.log().add("Encoder values(fl, fr, rl, rr)"+ Arrays.toString(values));
            }
            telemetry.update();







        }
    }
}
