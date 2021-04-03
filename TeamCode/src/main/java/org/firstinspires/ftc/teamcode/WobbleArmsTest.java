package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.libraries.DrivingLibrary;

@TeleOp
public class WobbleArmsTest extends LinearOpMode{
    DrivingLibrary drivingLibrary;
    Servo leftWobble;
    Servo rightWobble;
    float leftpos;
    float rightpos;

    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1.0);

        leftWobble = hardwareMap.get(Servo.class, "leftWobble");
        rightWobble = hardwareMap.get(Servo.class, "rightWobble");

        telemetry.addData("status", "initialized");
        telemetry.update();

        float leftpos = 0;
        float rightpos = 0;

        waitForStart();

        while(opModeIsActive()){

            if(gamepad2.x) {
                leftpos += 0.1;
            }

            if(gamepad2.y) {
                rightpos += 0.1;
            }

            if(gamepad2.a) {
                leftpos -= 0.1;
            }

            if(gamepad2.b) {
                rightpos -= 0.1;
            }

            leftWobble.setPosition(leftpos);
            rightWobble.setPosition(rightpos);

            telemetry.addData("rightpos", rightpos);
            telemetry.addData("leftpos", leftpos);
            telemetry.update();

        }

    }

}