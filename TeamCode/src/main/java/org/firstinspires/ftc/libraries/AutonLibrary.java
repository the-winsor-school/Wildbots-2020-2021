package org.firstinspires.ftc.libraries;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.enums.Encoders;
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaUltimateGoalNavigationWebcam;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


import java.util.*;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

public class AutonLibrary {

    private VuforiaLocalizer vuforia;
    public DrivingLibrary drivingLibrary;
    private float mmPerInch;


    public AutonLibrary (DrivingLibrary drivingLibrary, VuforiaLocalizer vuforia) {
        this.drivingLibrary = drivingLibrary;
        this.vuforia = vuforia;
        this.mmPerInch = mmPerInch;
    }

    public double getStackHeight(Rev2mDistanceSensor DistSenTop, Rev2mDistanceSensor DistSenBottom){
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
        return sum/count;
    }

    public VuforiaTrackable getImageTarget(ArrayList <VuforiaTrackable> allTrackables, float mmPerInch){
        boolean targetVisible = false;
        OpenGLMatrix lastLocation = null;

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        VuforiaTrackables targetsUltimateGoal = this.vuforia.loadTrackablesFromAsset("UltimateGoal");
        VuforiaTrackable blueTowerGoalTarget = targetsUltimateGoal.get(0);
        blueTowerGoalTarget.setName("Blue Tower Goal Target");
        VuforiaTrackable blueAllianceTarget = targetsUltimateGoal.get(3);
        blueAllianceTarget.setName("Blue Alliance Target");
        VuforiaTrackable frontWallTarget = targetsUltimateGoal.get(4);
        frontWallTarget.setName("Front Wall Target");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables.addAll(targetsUltimateGoal);

        targetVisible = false;
        VuforiaTrackable x = null;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                telemetry.addData("Visible Target", trackable.getName());
                targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                x = trackable;
                break;
            }
        }
        return x;
    }

    public float[] lineUpWithGoal (ArrayList <VuforiaTrackable> allTrackables) {
        int min = 1;
        int max = 12;
        OpenGLMatrix lastLocation = null;

        VuforiaTrackable x = getImageTarget(allTrackables, 25.4f);
        if(x!=null) {
            telemetry.addLine("no target visible");
            telemetry.update();
            float[] speeds = new float[] {0, 0};
            return speeds;

        } else if (x == allTrackables.get(3)) {
            float goalX = 11.1f;
            float goalY = 35.5f;

            OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) x.getListener()).getUpdatedRobotLocation();
            if (robotLocationTransform != null) {
                lastLocation = robotLocationTransform;
            }

            VectorF translation = lastLocation.getTranslation();
            float fieldX = translation.get(0) / mmPerInch;
            float fieldY = translation.get(1) / mmPerInch;

            float speedX = (goalX - fieldX - 1) / (max-min);
            float speedY = (goalY - fieldY - 1) / (max-min);

            float[] speeds = new float[] {speedX, speedY};

            return speeds;
        } else {
            telemetry.addLine("goal target not visible");
            telemetry.update();
            float [] speeds = new float[] {0, 0};
            return speeds;
        }

    }

}
