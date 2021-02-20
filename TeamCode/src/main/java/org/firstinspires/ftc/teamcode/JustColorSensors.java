package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "ColorSens")
public class JustColorSensors extends LinearOpMode {

    //variables:
    private static final long crashIntoWall = 500; //figure out actual time but this is the time it takes to get to the left wall
    private static final long parkLine = 4000; //Time to get to line
    RevColorSensorV3 color_sensor;
    int stackHeight;

    @Override
    public void runOpMode() throws InterruptedException {
        //  DistSenTop = hardwareMap.get(Rev2mDistanceSensor.class, "DistSenTop");
        //   DistSenBottom = hardwareMap.get(Rev2mDistanceSensor.class, "DistSenBottom");
        color_sensor = hardwareMap.get(RevColorSensorV3.class, "color");
        telemetry.addData("status", "initialized");
        telemetry.update();
        telemetry.addData("status", "BAAAAAAAH initialized");
        telemetry.update();


        //ensures that the code will only run once
        boolean ranOnce = false;

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("red", color_sensor.red());
            telemetry.addData("green", color_sensor.green());
            telemetry.addData("blue", color_sensor.blue());
            telemetry.addData("alpha", color_sensor.alpha());
            telemetry.update();
        }
    }
}
