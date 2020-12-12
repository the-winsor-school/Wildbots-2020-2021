package org.firstinspires.ftc.teamcode;

/**
 * fun note this isn't done yet so ignore this file
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.libraries.DrivingLibrary;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@Disabled
@TeleOp
public class CameraDriving extends LinearOpMode {
    // webcam settings: back camera, portrait false
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false;

    private static final String VUFORIA_KEY =
            "AVR5VxH/////AAABmQUP+WbSPEy9iyJjnD0VyyZdSmGgLyXX1NscDt7/AWW92iCkV0ckLd5A92CIRczLOcQ6lQlSI/u0JFsCyQYMB+1eKbLJcYKjpOpW64fzTJ9kkzDHin+ybf7Kin2dLtzW+HkvqNsZWkSIyWGM3AOquQiIIoi3MOZRUe0aCX8+dGwPe8FBOMDi4EaJXehqP0HqD2mBeElngDR6Fhg/VZvkNksRTA+KeBVUnNzuX4FERrsd89EXutOuq3Y3ocqhN+tJL8B2U/iV9qNC11Vj7ipxni+Uen4zYyOovIOhgoy0tG1XGzge7RZMg5n+wVOBkWtViLlC2G34qks2H/EBRRoQ9nRacyCeNj75/q/6J1NE/DaL";

    private static final float mmPerInch = 25.4f;
    private static final float mmTargetHeight = 6 * mmPerInch;

    // constants for perimeter targets
    private static final float halfField = 72 * mmPerInch;
    private static final float quadField = 36 * mmPerInch;

    // class members
    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia = null;

    WebcamName webcamName = null;

    private boolean targetVisible = false;
    private float phoneXRotate = 0;
    private float phoneYRotate = -90; //rotate camera around long axis
    private float phoneZRotate = 0;

    private float driveX = 0;
    private float driveY = 0;

    DrivingLibrary drivingLibrary;
    int drivingMode;

    @Override
    public void runOpMode() {
        drivingLibrary = new DrivingLibrary(this);
        drivingLibrary.setSpeed(1);
        drivingMode = 0;
        drivingLibrary.setMode(drivingMode);

        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcamName;
        parameters.useExtendedTracking = false;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // load the data for the image targets
        VuforiaTrackables targetsUltimateGoal = this.vuforia.loadTrackablesFromAsset("UltimateGoal");
        VuforiaTrackable blueTowerGoalTarget = targetsUltimateGoal.get(0);
        blueTowerGoalTarget.setName("Blue Tower Goal Target");
        VuforiaTrackable redTowerGoalTarget = targetsUltimateGoal.get(1);
        redTowerGoalTarget.setName("Red Tower Goal Target");
        VuforiaTrackable redAllianceTarget = targetsUltimateGoal.get(2);
        redAllianceTarget.setName("Red Alliance Target");
        VuforiaTrackable blueAllianceTarget = targetsUltimateGoal.get(3);
        blueAllianceTarget.setName("Blue Alliance Target");
        VuforiaTrackable frontWallTarget = targetsUltimateGoal.get(4);
        frontWallTarget.setName("Front Wall Target");

        // contains all trackable objects (image targets)
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsUltimateGoal);

        /**
         * here might be a good place to add specifications about the axes
         * yayyyy
         * a diagram could be nice?
         *
         * origin is the center of a full field
         *
         * before transformation, target image is conceptually located at the origin facing up
         */

        // set the position of the image targets relative to origin
        redAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        blueAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        frontWallTarget.setLocation(OpenGLMatrix
                .translation(-halfField, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        blueTowerGoalTarget.setLocation(OpenGLMatrix
                .translation(halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        redTowerGoalTarget.setLocation(OpenGLMatrix
                .translation(halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        // translate camera lens to its location on the robot
        // TODO: take measurements of these values (Current ones are from example)
        final float CAMERA_FORWARD_DISPLACEMENT = 4.0f * mmPerInch;
        final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;
        final float CAMERA_LEFT_DISPLACEMENT = 0;

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        // let all the trackable listeners know where the phone is
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }

        waitForStart();

        // Note: To use the remote camera preview:
        // AFTER you hit Init on the Driver Station, use the "options menu" to select "Camera Stream"
        // Tap the preview window to receive a fresh image.

        targetsUltimateGoal.activate();
        while(!isStopRequested()) {
            drivingLibrary.bevelDrive(driveX, driveY, 0);

            // if the blue tower goal target is on camera
            if(((VuforiaTrackableDefaultListener) blueTowerGoalTarget.getListener()).isVisible()) {
                telemetry.addLine("Blue Tower Goal Target Visible");

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) blueTowerGoalTarget.getListener()).getUpdatedRobotLocation();
                if(robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }

                VectorF translation = lastLocation.getTranslation();
                float fieldX = translation.get(0) / mmPerInch;
                float fieldY = translation.get(1) / mmPerInch;
                // goal position: (11.1, 35.5)
                float goalX = 11.1f;
                float goalY = 35.5f;

                /**
                 * the fieldX gets bigger as the robot gets closer to the goal
                 * the fieldY gets bigger as the robot gets closer to the near wall
                 */

                if (Math.abs(goalX - fieldX) > 1) {
                    telemetry.addLine("not at the right X");
                    //when the goalX is bigger than the fieldX, the robot needs to move TOWARDS THE GOAL
                    //POSITIVE IS TOWARDS THE GOAL
                    driveX = (goalX - fieldX) / goalX;
                }
                else {
                    driveX = 0;
                }

                if (Math.abs(goalY - fieldY) > 1) {
                    telemetry.addLine("not at the right Y");
                    //when the goalY is bigger than the fieldY, the robot needs to move TOWARDS THE WALL
                    //POSITIVE IS TOWARDS THE WALL
                    driveY = (goalY - fieldY) / goalY;
                }
                else {
                    driveY = 0;
                }
                telemetry.addData("Driving X", driveX);
                telemetry.addData("Driving Y", driveY);
            }
            telemetry.update();
        }
    }
}
