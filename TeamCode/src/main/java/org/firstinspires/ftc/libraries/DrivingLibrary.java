/**
 * library for drive train functions
 * (strafing, turning, spinning, etc)
 */

package org.firstinspires.ftc.libraries;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.enums.DrivingMode;
import org.firstinspires.ftc.enums.Encoders;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Hashtable;


import java.util.Arrays;

public class DrivingLibrary {
    // hardware variables
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor leftRear;
    public DcMotor rightRear;
    //public DcMotor rightEncoder;
    //public DcMotor leftEncoder;
    public DcMotor launchMotor;
    public DcMotor intakeMotor;
    private DcMotor[] allMotors;
    private HardwareMap hardwareMap;
    private double[] strafeBias;
    private double[] strafePowers;
    private int[] encoderValues;

    // sensor variables
    private BNO055IMU imu; //gyroscope in rev hub
    private Orientation angles;
    private Acceleration gravity;

    // other variables
    private double speedSetting; //sets max speed
    private DrivingMode drivingMode;
    private OpMode opMode;
    private double theta;
    private double strafeError;
    private double targetAngle;
    public double xPos;
    public double yPos;
    private Hashtable<Encoders, Integer> encoderTable;
    private Hashtable<Encoders, Integer> recordEncoderTable;

    public DrivingLibrary(OpMode opMode) {
        this.opMode = opMode;
        hardwareMap = opMode.hardwareMap;

        leftFront = hardwareMap.tryGet(DcMotor.class, "leftFront");
        rightFront = hardwareMap.tryGet(DcMotor.class, "rightFront");
        leftRear = hardwareMap.tryGet(DcMotor.class, "leftRear");
        rightRear = hardwareMap.tryGet(DcMotor.class, "rightRear");
        launchMotor =hardwareMap.tryGet(DcMotor.class, "launchMotor");
        intakeMotor = hardwareMap.tryGet(DcMotor.class, "intakeMotor");

        encoderTable = new Hashtable<Encoders, Integer>();
        recordEncoderTable=new Hashtable<Encoders,Integer>();


        // MOTOR ORDER: LF, RF, LR, RR
        allMotors = new DcMotor[] {leftFront, rightFront, leftRear, rightRear};
        for (DcMotor motor : allMotors) {
            if (motor == null) {
                opMode.telemetry.addLine("Wrong configuration");
                opMode.telemetry.update();
                opMode.stop();
            }
        }

        //motors face opposite directions so one side is reversed
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        //for imu set up
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);

        strafeBias = new double[] {1, 1, 1, 1};
        encoderValues = new int[] {0, 0, 0, 0};

        targetAngle = getIMUAngle();

    }

    // WHEEL ORDER: LF, RF, LR, RR

    //speed is relative to other wheels - >1 will make it faster, <1 will make it slower
    public void setStrafeBias(int wheel, double speed) {
        strafeBias[wheel] = speed;
    }

    //for incrementally changing strafe bias for testing
    public void updateStrafeBias(int wheel, int multiplier) {
        strafeBias[wheel] += (.01 * multiplier);
    }

    //displays current strafe bias on phone
    public void printStrafeBias() {
        opMode.telemetry.addData("front left strafe bias", strafeBias[0]);
        opMode.telemetry.addData("front right strafe bias", strafeBias[1]);
        opMode.telemetry.addData("rear left strafe bias", strafeBias[2]);
        opMode.telemetry.addData("rear right strafe bias", strafeBias[3]);
    }

    //strafing on one joystick with twist on the other
    //uses joystick 1 x, joystick 1 y, and joystick 2 x
    public void drive(float x, float y, float t) {
        double vd = strafeSpeed(x, y);
        theta = Math.atan2(y, x);
        x = 0;
        theta = Math.atan2(y, x);
        double vt = t;

        //in order -- LF, RF, LR, RR
        strafePowers = new double[] {
                vd * Math.sin(theta + Math.PI/4) * strafeBias[0] - vt,
                vd * Math.sin(theta - Math.PI/4) * strafeBias[1] + vt,
                vd * Math.sin(theta - Math.PI/4) * strafeBias[2] - vt,
                vd * Math.sin(theta + Math.PI/4) * strafeBias[3] + vt
        };

        strafeScale(strafePowers);

        leftFront.setPower(-strafePowers[0] * speedSetting);
        rightFront.setPower(-strafePowers[1] * speedSetting);
        leftRear.setPower(strafePowers[2] * speedSetting);
        rightRear.setPower(strafePowers[3] * speedSetting);
    }

    //essentially the same as regular driving but different wheels are reversed
    public void bevelDrive(float x, float y, float t) {
        double vd = strafeSpeed(x, y);
        theta = Math.atan2(y, x);
        double vt = t;

        /*if (vt == 0) {
            if (Math.abs(getIMUAngle() - targetAngle) >= .1) {
                if (getIMUAngle() > 0) {
                    vt = .1;
                }
                else {
                    vt = .1;
                }
            }
        }
        else {
            targetAngle = getIMUAngle();
        }
        */
        //in order -- lF, rF, rR, lR
        strafePowers = new double[] {
                vd * Math.sin(theta + Math.PI/4) * strafeBias[0] - vt,
                vd * Math.sin(theta - Math.PI/4) * strafeBias[1] + vt,
                vd * Math.sin(theta + Math.PI/4) * strafeBias[2] + vt,
                vd * Math.sin(theta - Math.PI/4) * strafeBias[3] - vt
        };

        strafeScale(strafePowers);

        leftFront.setPower(strafePowers[0] * speedSetting);
        rightFront.setPower(strafePowers[1] * speedSetting);
        rightRear.setPower(strafePowers[2] * speedSetting);
        leftRear.setPower(strafePowers[3] * speedSetting);

        if (vt != 0) {
            targetAngle = getIMUAngle();
        }
    }

    //gets speed for strafing
    public double strafeSpeed(float x, float y) {
        double d = Math.sqrt(x*x + y*y);
        if (d > 1) {
            d = 1;
        }
        return d;
    }

    //scales strafing values so all motor powers are between -1 and 1
    public double[] strafeScale(double[] strafePowers) {
        double maxPower = Math.abs(strafePowers[0]);
        for (int i = 1; i < 4; i++) {
            if (Math.abs(strafePowers[i]) > maxPower) {
                maxPower = Math.abs(strafePowers[i]);
            }
        }
        double scale = maxPower;
        if (scale >= 1) {
            for (int i = 0; i < 4; i++) {
                strafePowers[i] /= scale;
            }
        }
        return strafePowers;
    }

    //spin to a particular angle in radians (currently uses bevelDrive but can also be switch to use drive)
    //use angles between -pi and pi
    public void spinToAngle(double angle) {
        double goalAngle = getIMUAngle() + angle;
        while (Math.abs(angle - getIMUAngle()) > .15) {
            if (angle > 0) {
                bevelDrive(0, 0, .25f);
            }
            else {
                bevelDrive(0, 0, -.25f);
            }
            opMode.telemetry.update();
        }
        brakeStop();
        targetAngle = getIMUAngle();
    }

    //turns motor power off (doesn't stop immediately)
    public void floatStop() {
        for (DcMotor motor : allMotors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motor.setPower(0);
        }
    }

    //turns motor power off and brakes
    public void brakeStop() {
        for (DcMotor motor : allMotors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setPower(0);
        }
    }

    //sets brake mode (int) - float stop is 0, brake stop is 1
    public void setMode(int i) {
        DrivingMode[] values = DrivingMode.values();
        drivingMode = values[i];

        switch (drivingMode) {
            case FLOAT_STOP:
                for (DcMotor motor : allMotors) {
                    motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                } //coasts when it stops
                break;
            case BRAKE_STOP:
                for (DcMotor motor : allMotors) {
                    motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                } //fully brakes
                break;
        }
    }

    //sets brake mode
    public void setMode(DrivingMode d) {
        drivingMode = d;

        switch (drivingMode) {
            case FLOAT_STOP:
                for (DcMotor motor : allMotors) {
                    motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                }
                break;
            case BRAKE_STOP:
                for (DcMotor motor : allMotors) {
                    motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                }
                break;
        }
    }

    //sets max robot speed (values between 0 and 1 - default is 1)
    public void setSpeed(double speed) {
        speedSetting = speed;
    }

    //resets all encoder values to 0 (then runs motors without encoders tracking after?? FIGURE THIS OUT)
    public void resetEncoderValues() {
        for (DcMotor motor : allMotors) {
            if (motor != null) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    //get current driving mode (brake type)
    public String getMode() {
        return drivingMode.getStringValue();
    }

    // get current robot angle (radians)
    public double getIMUAngle() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        return angles.firstAngle;
    }

    //get current motor powers
    public String getMotorPowers() {
        double[] powers = new double[] {leftFront.getPower(), leftRear.getPower(), rightFront.getPower(), rightRear.getPower()};
        return Arrays.toString(powers);
    }

    //get current encoder values
    public int[] getEncoderValues() {

        encoderValues[0] = leftFront.getCurrentPosition();
        encoderValues[1] = rightFront.getCurrentPosition();

        return encoderValues;
    }

    //print current encoder values to driver phone
    public void printEncoderValues() {
        getEncoderValues();
        opMode.telemetry.addData("left encoder", encoderValues[0]);
        opMode.telemetry.addData("front right encoder", encoderValues[1]);
    }
    //Supposed to be able to convert ticks to inches
    public void distanceToEncoderValue(int dist){
        //are these values accurate? unsure, but they're in inches
        int[] e = getEncoderValues();
        double wheelCircumference = (4*3.14159); //pi times diameter
        encoderTable.put(Encoders.LF, (int)(dist+e[0]/8192*wheelCircumference));
        encoderTable.put(Encoders.RF, (int)(dist+e[1]/8192*wheelCircumference));
    }
    public int getLeftFrontEncoderValueSpecifically(){
        // i think this does not need to exist anymore but i'm not confident enough in that assertion to delete this
        return encoderTable.get(Encoders.LF);
    }
    //Set what position they should drive to
    public void setEncoders(int dist){
        distanceToEncoderValue(dist);
        leftFront.setTargetPosition(encoderTable.get(Encoders.LF));
        rightFront.setTargetPosition(encoderTable.get(Encoders.RF));

        }
        //another potentially obsolete function
    public boolean motorsBusy() {
        if(leftFront.isBusy() || rightRear.isBusy() || leftRear.isBusy()) {
            return true;
        }
        return false;
    }
    public void setRunMode(boolean encoderMode){
        if (encoderMode == true){
            for (DcMotor motor : allMotors) {
                motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        }
        else{
            for (DcMotor motor : allMotors) {
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }
    }
    //record the current encoder position, to be used when comparing whether or not the wheel is at
    //the right position
    public void setRecordEncoderTable(){
        recordEncoderTable.put(Encoders.LF, leftFront.getCurrentPosition());
        recordEncoderTable.put(Encoders.RF, rightFront.getCurrentPosition());

    }
    //Delta s is the change in position of the center of the robot
    private double getDeltaS(){
        double Sr=rightFront.getCurrentPosition()-recordEncoderTable.get(Encoders.RF);
        double Sl=leftFront.getCurrentPosition()-recordEncoderTable.get(Encoders.LF);
        double s =(Sr-Sl)/2;
        return s;
    }
    //delta theta is the change in angle
    private double getDeltaTheta() {
        double Sr=rightFront.getCurrentPosition()-recordEncoderTable.get(Encoders.RF);
        double Sl=leftFront.getCurrentPosition()-recordEncoderTable.get(Encoders.LF);
        //unit in inches
        double theta=(Sr-Sl)/(18.5*2);
        return theta;
    }
    //when converting to (x,y) coordinates, need to get delta x from delta s
    public double getDeltaX(){
        //half delta theta is delta theta divided by two because that's how the math works
        //and I didn't want to make another variable :/
        double halfDeltaTheta = getDeltaTheta()/2;
        double deltaS = getDeltaS();
        double deltaX=deltaS*Math.cos(getIMUAngle()+halfDeltaTheta);
        return deltaX;
    }
    //same as delta y
    public double getDeltaY(){
        double halfDeltaTheta = getDeltaTheta()/2;
        double deltaS = getDeltaS();
        double deltaY=deltaS*Math.sin(getIMUAngle()+halfDeltaTheta);
        return deltaY;
    }
    public void resetXY(){
        //set to where the robot is set down, probably won't be in the bottom left corner
        xPos=0;
        yPos=0;
    }


}
