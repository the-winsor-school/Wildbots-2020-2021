package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.libraries.DrivingLibrary;

import java.util.Arrays;

@Autonomous
public class DeleteMe extends LinearOpMode {
    DrivingLibrary drivingLibrary;
    int drivingMode;
    boolean ranOnce = false;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor rightRear;
    DcMotor leftRear;
    @Override
    public void runOpMode() throws InterruptedException {
        drivingLibrary = new DrivingLibrary(this);
        boolean ranOnce = false;
        leftFront = hardwareMap.tryGet(DcMotor.class, "leftFront");
        rightFront = hardwareMap.tryGet(DcMotor.class, "rightFront");
        leftRear = hardwareMap.tryGet(DcMotor.class, "leftRear");
        rightRear = hardwareMap.tryGet(DcMotor.class, "rightRear");

        drivingLibrary.resetEncoderValues();
        drivingLibrary.setEncoders(27);
        drivingLibrary.setSpeed(1);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("status", "initilized");
        telemetry.update();
        //rightFront.setDirection(DcMotor.Direction.REVERSE);

        int[] values = drivingLibrary.getEncoderValues();

        waitForStart();
        leftFront.setPower(0.50);
        rightFront.setPower(0.50);
        leftRear.setPower(0.50);
        rightRear.setPower(0.50);
        while(opModeIsActive() && leftFront.isBusy()){
            values=drivingLibrary.getEncoderValues();
            telemetry.addData("encoder values:", Arrays.toString(values));
            telemetry.update();
        }
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);


    }
}
