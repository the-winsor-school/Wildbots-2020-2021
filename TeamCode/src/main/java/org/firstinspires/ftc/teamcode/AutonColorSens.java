package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;

@Disabled
public class AutonColorSens extends LinearOpMode {

    private DrivingLibrary drivingLibrary;
    //variables:
    private static final long crashIntoWall = 500; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 4000; //Time to get to line
    RevColorSensorV3 color_sensor;
    AutonLibrary autonLibrary;
    // Rev2mDistanceSensor DistSenTop;
    // Rev2mDistanceSensor DistSenBottom;
    int blue = color_sensor.blue();
    int stackHeight;

    //initializing
    @Override
    public void runOpMode() throws InterruptedException {
        //  DistSenTop = hardwareMap.get(Rev2mDistanceSensor.class, "DistSenTop");
        //   DistSenBottom = hardwareMap.get(Rev2mDistanceSensor.class, "DistSenBottom");
        color_sensor = hardwareMap.get(RevColorSensorV3.class, "color");
        telemetry.addData("status", "initialized");
        telemetry.update();
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);
        telemetry.addData("status", "BAAAAAAAH initialized");
        telemetry.update();
        autonLibrary = new AutonLibrary(drivingLibrary, this);

        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();


        //drive for (crashIntoWall) speed
        while (opModeIsActive()) {
            telemetry.addData("red", color_sensor.red());
            telemetry.addData("green", color_sensor.green());
            telemetry.addData("blue", color_sensor.blue());
            telemetry.addData("alpha", color_sensor.alpha());
            telemetry.update();
            stackHeight = 0;
            //   autonLibrary.getStackHeight(DistSenTop, DistSenBottom);
            if (stackHeight == 0) {
                {
                    //moving towards the parking line until it hits the blue line
                    //blue line is (part of wobble goal box)
                    if (!isWhite()) {
                        drivingLibrary.bevelDrive(.5f, 0, 0);
                    } else {
                        drivingLibrary.bevelDrive(0, 0, 0);
                        break;
                    }
                }
            }
        }
    }


    //defining what the color sensor sees as gray (darker than white and blue)
//!!!test whether blue is darker than gray
    private boolean isGray() {
        //TEST whether 50 is the right brightness
        return 1500 < blue && blue < 3000;
    }

    private boolean isBlue() {
        return blue < 1500;
    }

    private boolean isWhite() {
        return blue > 3000;
    }

}
