package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.libraries.AutonLibrary;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous

public class DistSens extends LinearOpMode {
    Rev2mDistanceSensor distTop;
    Rev2mDistanceSensor distBot;
    AutonLibrary autonLibrary;
    private DrivingLibrary drivingLibrary;
    int ringAmount = 0;

    //establishes the sensor names for configuration
    @Override
    public void runOpMode() throws InterruptedException {
        distTop = hardwareMap.get(Rev2mDistanceSensor.class, "distTop");
        //distBot = hardwareMap.get(Rev2mDistanceSensor.class, "distBot");
        telemetry.addData("status", "initialized");
        telemetry.update();
        autonLibrary = new AutonLibrary(drivingLibrary, this);
//prints the distance sensed by the top and bottom sensors respectively
        waitForStart();
        while (opModeIsActive()) {
            autonLibrary.getStackHeight(distTop);
            /*telemetry.addData("Top Sensor Value", distTop.getDistance(DistanceUnit.CM));
            telemetry.addData("Bottom Sensor Value", distBot.getDistance(DistanceUnit.CM));
            //if the top distance sensor senses that there is a ring less than 200 centimeters, return: four rings)
            if (distTop.getDistance(DistanceUnit.CM) < 200) {
                telemetry.addData("Four Rings", distTop.getDistance(DistanceUnit.CM));
                ringAmount = 4;
                //otherwise, if the bottom distance sensor senses that there is a ring less than 200 centimeters, return: one ring)
            } else if (distBot.getDistance(DistanceUnit.CM) < 200) {
                telemetry.addData("One Ring", distBot.getDistance(DistanceUnit.CM));
                ringAmount = 1;
                //if no sensor returns a value less than 200, there are no rings)
            } else {
                telemetry.addData("Zero Rings", distBot.getDistance(DistanceUnit.CM));
                ringAmount = 0;
            }
            telemetry.update();
//sum (values added) and count (# of values counted) start at zero
          /*  double sum = 0;
            int count = 0;
            //will only take values until its collected 10
            while (count < 10) {
                // adds one to the number of values counted
                count++;
                sum += DistSenBottom.getDistance(DistanceUnit.CM);

            }
            //gets the average of the bottom sensor's values
            telemetry.addData("Average-bottom sensor", sum / count);*/


        }
    }
}