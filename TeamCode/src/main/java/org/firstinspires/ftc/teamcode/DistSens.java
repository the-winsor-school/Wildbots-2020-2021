package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.*;

@Autonomous

public class DistSens extends LinearOpMode {
    Rev2mDistanceSensor DistSenTop;
    Rev2mDistanceSensor DistSenBottom;

    //establishes the sensor names for configuration
    @Override
    public void runOpMode() throws InterruptedException {
        DistSenTop = hardwareMap.get(Rev2mDistanceSensor.class, "DistSenTop");
        DistSenBottom = hardwareMap.get(Rev2mDistanceSensor.class, "DistSenBottom");

        telemetry.addData("status", "initialized");
        telemetry.update();
//prints the distance sensed by the top and bottom sensors respectively
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("Top Sensor Value", DistSenTop.getDistance(DistanceUnit.CM));
            telemetry.addData("Bottom Sensor Value", DistSenBottom.getDistance(DistanceUnit.CM));
            //if the top distance sensor senses that there is a ring less than 200 centimeters, return: four rings)
            if (DistSenTop.getDistance(DistanceUnit.CM) < 200) {
                telemetry.addData("Four Rings", DistSenTop.getDistance(DistanceUnit.CM));
                //otherwise, if the bottom distance sensor senses that there is a ring less than 200 centimeters, return: one ring)
            } else if (DistSenBottom.getDistance(DistanceUnit.CM) < 200) {
                telemetry.addData("One Ring", DistSenBottom.getDistance(DistanceUnit.CM));
                //if no sensor returns a value less than 200, there are no rings)
            } else {
                telemetry.addData("Zero Rings", DistSenBottom.getDistance(DistanceUnit.CM));
            }
            telemetry.update();
//sum (values added) and count (# of values counted) start at zero
            double sum = 0;
            int count = 0;
            //will only take values until its collected 10
            while (count < 10) {
                // adds one to the number of values counted
                count++;
                sum += DistSenBottom.getDistance(DistanceUnit.CM);

            }

            //gets the average of the bottom sensor's values
            telemetry.addData("Average-bottom sensor", sum / count);

        }
    }
}