package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Arrays;
//class name
@TeleOp(name  = "bro idek", group = "UnFinished")
public class LocalizationTeleOP extends LinearOpMode{
    //drive train
    DrivingLibrary drivingLibrary;
    int drivingMode;
    int ranOnce = 0;
    Rev2mDistanceSensor DistSenTop;
    public void runOpMode() throws InterruptedException {
        //set up all the stuff ya know
        drivingLibrary = new DrivingLibrary(this);
        DistSenTop = hardwareMap.get(Rev2mDistanceSensor.class, "DistSenTop");

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
        int[] values;

        waitForStart();

        while (opModeIsActive()) {
            if (!ranOnce) {
                //sends text to driver phone
                telemetry.addData("Status", "Running");
                telemetry.addData("Brake Mode", drivingLibrary.getMode());

                drivingLibrary.bevelDrive(0, .75f, 0);
                values=drivingLibrary.getEncoderValues();
                telemetry.addData("encoder values:", Arrays.toString(values));
                telemetry.update();
                //is it at the correct x and y positions but there's def a smarter way of doing it
                boolean dumbX=false;
                boolean dumbY=false;
                int x=drivingLibrary.getLeftFrontEncoderValueSpecifically();

                while(opModeIsActive()&&xPos!=x){
                    //localization
                    values=drivingLibrary.getEncoderValues();
                    deltaX= drivingLibrary.getDeltaX();
                    xPos+=deltaX;
                    deltaY=drivingLibrary.getDeltaY();
                    yPos+=deltaY;

                    drivingLibrary.setRecordEncoderTable();


                    //whole lot of telemetry additions
                    telemetry.addData("encoder values:", Arrays.toString(values));
                    telemetry.addData("xPos:",xPos);
                    telemetry.addData("yPos:", yPos);
                    telemetry.addData("Top Sensor Value", DistSenTop.getDistance(DistanceUnit.CM));
                    telemetry.update();

                }

                drivingLibrary.brakeStop();
                //drivingLibrary.printEncoderValues();


                ranOnce=true;
            }







        }
    }
}

