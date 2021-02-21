package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class RevServoTest extends LinearOpMode {
    Servo left;
    Servo right;

    double leftPos;
    double rightPos;

    @Override
    public void runOpMode() throws InterruptedException {

        left = hardwareMap.get(Servo.class, "leftWobble");
        right = hardwareMap.get(Servo.class, "rightWobble");

        leftPos = left.getPosition();
        rightPos = right.getPosition();

        waitForStart();

        while(opModeIsActive()) {
            left.setPosition(leftPos);
            right.setPosition(rightPos);

            if(gamepad1.a) {
                rightPos += .01;
            }

            if(gamepad1.b) {
                rightPos -= .01;
            }

            if(gamepad1.x) {
                leftPos += .01;
            }

            if(gamepad1.y) {
                leftPos -= .01;
            }

            telemetry.addData("left pos", leftPos);
            telemetry.addData("right pos", rightPos);
            telemetry.update();
        }
    }
}
