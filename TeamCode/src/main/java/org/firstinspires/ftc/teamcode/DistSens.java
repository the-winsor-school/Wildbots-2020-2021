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

    @Override
    public void runOpMode() throws InterruptedException{
        DistSenTop = hardwareMap.get(Rev2mDistanceSensor.class,"DistSenTop");
        DistSenBottom = hardwareMap.get(Rev2mDistanceSensor.class, "DistSenBottom");

        telemetry.addData("status", "initialized");
        telemetry.update();

        waitForStart();
        while(opModeIsActive()){
            telemetry.addData("Top Sensor Value", DistSenTop.getDistance(DistanceUnit.CM));
            telemetry.addData("Bottom Sensor Value", DistSenBottom.getDistance(DistanceUnit.CM));
            if(DistSenTop.getDistance(DistanceUnit.CM) < 200){
                telemetry.addData("Four Rings", DistSenTop.getDistance(DistanceUnit.CM));
            } else if (DistSenBottom.getDistance(DistanceUnit.CM) < 200){
                telemetry.addData("One Ring", DistSenBottom.getDistance(DistanceUnit.CM));
            } else {
                telemetry.addData("Zero Rings", DistSenBottom.getDistance(DistanceUnit.CM));
            }
            telemetry.update();
        }
    }
}