package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;

import java.util.Arrays;
//class name
@TeleOp(name  = "HDSFJKFDAJLZGJKBDA", group = "UnFinished")
public class TestingLocationTeleOP extends LinearOpMode{
        //drive train
        DrivingLibrary drivingLibrary;
        int drivingMode;
        int ranOnce = 0;
        public void runOpMode() throws InterruptedException {
            //set up our driving library
            drivingLibrary = new DrivingLibrary(this);
            //max speed ((1 / distance of joy stick from 0)) x the % of motor powers)
            drivingLibrary.setSpeed(1);
            drivingMode = 0;
            drivingLibrary.setMode(drivingMode);
            boolean ranOnce = false;
            telemetry.addData("status", "initilized");
            telemetry.update();
            drivingLibrary.resetEncoderValues();
            drivingLibrary.setEncoders(12);
            drivingLibrary.setRunMode(true);

            drivingLibrary.setRecordEncoderTable();

            double deltaX;
            double deltaY;
            double xPos=0;
            double yPos=0;

            telemetry.addData("Status", "Initialized");
            telemetry.update();

            waitForStart();

            while (opModeIsActive()) {
                if (!ranOnce) {
                    drivingLibrary.resetEncoderValues();

                    //sends text to driver phone
                    telemetry.addData("Status", "Running");
                    telemetry.addData("Brake Mode", drivingLibrary.getMode());

                    int[] values = drivingLibrary.getEncoderValues();

                    drivingLibrary.bevelDrive(0, -.75f, 0);
                    values=drivingLibrary.getEncoderValues();
                    telemetry.addData("encoder values:", Arrays.toString(values));
                    telemetry.update();

                    while(opModeIsActive() && drivingLibrary.motorsBusy()){
                        values=drivingLibrary.getEncoderValues();
                        values=drivingLibrary.getEncoderValues();
                        deltaX= drivingLibrary.getDeltaX();
                        xPos+=deltaX;
                        deltaY=drivingLibrary.getDeltaY();
                        yPos+=deltaY;
                        drivingLibrary.setRecordEncoderTable();
                        drivingLibrary.bevelDrive(0, -.75f, 0);

                        telemetry.addData("encoder values:", Arrays.toString(values));
                        telemetry.addData("xPos:",xPos);
                        telemetry.addData("yPos:", yPos);
                        telemetry.update();

                    }

                    drivingLibrary.brakeStop();
                    //drivingLibrary.printEncoderValues();


                    ranOnce=true;
                }







            }
        }
    }

