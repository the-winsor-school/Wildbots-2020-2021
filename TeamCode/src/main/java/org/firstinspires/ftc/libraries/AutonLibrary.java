package org.firstinspires.ftc.libraries;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


import java.util.*;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public class AutonLibrary {

    //
    public DrivingLibrary drivingLibrary;
    private OpMode opMode;

    // vuforia variables
    public VuforiaLocalizer vuforia = null;
    private ArrayList<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
    private OpenGLMatrix robotFromCamera;
    private OpenGLMatrix lastLocation = null;
    public VuforiaTrackables targetsUltimateGoal;

    private static final String VUFORIA_KEY =
            "AVR5VxH/////AAABmQUP+WbSPEy9iyJjnD0VyyZdSmGgLyXX1NscDt7/AWW92iCkV0ckLd5A92CIRczLOcQ6lQlSI/u0JFsCyQYMB+1eKbLJcYKjpOpW64fzTJ9kkzDHin+ybf7Kin2dLtzW+HkvqNsZWkSIyWGM3AOquQiIIoi3MOZRUe0aCX8+dGwPe8FBOMDi4EaJXehqP0HqD2mBeElngDR6Fhg/VZvkNksRTA+KeBVUnNzuX4FERrsd89EXutOuq3Y3ocqhN+tJL8B2U/iV9qNC11Vj7ipxni+Uen4zYyOovIOhgoy0tG1XGzge7RZMg5n+wVOBkWtViLlC2G34qks2H/EBRRoQ9nRacyCeNj75/q/6J1NE/DaL";

    // webcam variables
    private static WebcamName webcamName = null;

    private static final float X_ROTATE = 0;
    private static final float Y_ROTATE = 0;
    private static final float Z_ROTATE = 0;

    // field constants
    private static final float MM_PER_INCH = 25.4f;
    private static final float MM_TARGET_HEIGHT = (6) * MM_PER_INCH;
    private static final float HALF_FIELD = 72 * MM_PER_INCH;
    private static final float QUAD_FIELD = 36 * MM_PER_INCH;

    // other variables? (currently singular variable)
    public boolean goalReached;

    public AutonLibrary (DrivingLibrary drivingLibrary, OpMode opMode) {
        this.drivingLibrary = drivingLibrary;
        this.opMode = opMode;

        webcamName = opMode.hardwareMap.tryGet(WebcamName.class, "Webcam 1");

        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcamName;
        parameters.useExtendedTracking = false;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // load all the targets
        targetsUltimateGoal = this.vuforia.loadTrackablesFromAsset("UltimateGoal");
        VuforiaTrackable blueTowerGoalTarget = targetsUltimateGoal.get(0);
        blueTowerGoalTarget.setName("Blue Tower Goal Target");
        VuforiaTrackable blueAllianceTarget = targetsUltimateGoal.get(3);
        blueAllianceTarget.setName("Blue Alliance Target");
        VuforiaTrackable frontWallTarget = targetsUltimateGoal.get(4);
        frontWallTarget.setName("Front Wall Target");

        // add target locations
        blueAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, HALF_FIELD, MM_TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));
        frontWallTarget.setLocation(OpenGLMatrix
                .translation(-HALF_FIELD, 0, MM_TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));
        blueTowerGoalTarget.setLocation(OpenGLMatrix
                .translation(HALF_FIELD, QUAD_FIELD, MM_TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        // array list of targets
        allTrackables.addAll(targetsUltimateGoal);

        // camera location relative to robot center
        final float CAMERA_FORWARD_DISPLACEMENT = 4f * MM_PER_INCH;
        final float CAMERA_VERTICAL_DISPLACEMENT = 8f * MM_PER_INCH;
        final float CAMERA_LEFT_DISPLACEMENT = 0f * MM_PER_INCH;

        this.robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, Y_ROTATE, Z_ROTATE, X_ROTATE));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }
    }

    public double getStackHeight(Rev2mDistanceSensor DistSenTop, Rev2mDistanceSensor DistSenBottom){
        opMode.telemetry.addData("Top Sensor Value", DistSenTop.getDistance(DistanceUnit.CM));
        opMode.telemetry.addData("Bottom Sensor Value", DistSenBottom.getDistance(DistanceUnit.CM));
        //if the top distance sensor senses that there is a ring less than 200 centimeters, return: four rings)
        if (DistSenTop.getDistance(DistanceUnit.CM) < 200) {
            opMode.telemetry.addData("Four Rings", DistSenTop.getDistance(DistanceUnit.CM));
            //otherwise, if the bottom distance sensor senses that there is a ring less than 200 centimeters, return: one ring)
        } else if (DistSenBottom.getDistance(DistanceUnit.CM) < 200) {
            opMode.telemetry.addData("One Ring", DistSenBottom.getDistance(DistanceUnit.CM));
            //if no sensor returns a value less than 200, there are no rings)
        } else {
            opMode.telemetry.addData("Zero Rings", DistSenBottom.getDistance(DistanceUnit.CM));
        }
        opMode.telemetry.update();
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
        opMode.telemetry.addData("Average-bottom sensor", sum / count);
        return sum/count;
    }

    public boolean getImageTarget(){
        boolean targetVisible = false;

        targetVisible = false;
        VuforiaTrackable x = null;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                opMode.telemetry.addData("Visible Target", trackable.getName());
                opMode.telemetry.update();
                targetVisible = true;

                if(trackable.getName().equals("Blue Tower Goal Target")) {
                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean getAllianceTarget(){
        boolean targetVisible = false;

        targetVisible = false;
        VuforiaTrackable x = null;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                opMode.telemetry.addData("Visible Target", trackable.getName());
                opMode.telemetry.update();
                targetVisible = true;

                if(trackable.getName().equals("Blue Alliance Target")) {
                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public float[] lineUpWithGoal () {
        float min = 3;
        float max = 17;
        float t = 0;
        double angleRange = .1;
        double targetAngle = -1/2 * Math.PI;

        boolean x = getAllianceTarget(); // do we see target?

        if(!x) { // can't see target
            opMode.telemetry.addLine("no target visible");
            opMode.telemetry.update();
            float[] speeds = new float[] {0, 0, 0};
            return speeds;

        }
        else {
            //goal positions
            float goalX = -22.4f;
            float goalY = 41.1f;

            //get current x and y
            VectorF translation = lastLocation.getTranslation();
            float fieldX = translation.get(0) / MM_PER_INCH;
            float fieldY = translation.get(1) / MM_PER_INCH;

            float speedX;
            float speedY;

            if(Math.abs(goalX - fieldX) < min) { // target x has been reached
                speedX = 0;
            }
            else if(Math.abs(goalX - fieldX) > max) { // outside of max x - full power
                speedX = goalX > fieldX ? 1 : -1;
            }
            else {
                speedX = (goalX - fieldX - 1) / (max - min); // scale x based on distance
            }

            if(Math.abs(goalY - fieldY) < min) { // target y has been reached
                speedY = 0;
            }
            else if(Math.abs(goalY - fieldY) > max) { // outside of max y - full power
                speedY = goalY > fieldY ? 1 : -1;
            }
            else {
                speedY = (goalY - fieldY - 1) / (max - min); // scale y based on distance
            }

            if (Math.abs(drivingLibrary.getIMUAngle() - targetAngle) >= .1) { // if we're outside the target range of t
                if (drivingLibrary.getIMUAngle() > targetAngle) { // check which direction we need to turn
                    t = .1f;
                }
                else {
                    t = -.1f;
                }
            }

            // are we within the min error range of the target position?
            if(Math.abs(goalY - fieldY) < min && Math.abs(goalX - fieldX) < min && Math.abs(targetAngle - drivingLibrary.getIMUAngle()) < angleRange) {
                goalReached = true;
            }

            //print speeds - for debugging
            opMode.telemetry.addData("x pos", fieldX);
            opMode.telemetry.addData("y pos", fieldY);
            opMode.telemetry.addData("x speed", speedX);
            opMode.telemetry.addData("y speed", speedY);
            opMode.telemetry.addData("x diff", goalX - fieldX);
            opMode.telemetry.addData("y diff", goalY - fieldY);
            opMode.telemetry.update();

            float[] speeds = new float[] {speedX, speedY, t};

            return speeds;
        }

    }

}
