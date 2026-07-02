package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class RobotHardware {

    public Motor frontLeft;
    public Motor frontRight;
    public Motor backLeft;
    public Motor backRight;
    public Motor intakeMotor;

    public GoBildaPinpointDriver pinpoint;
    public PinpointPedroLocalizerAdapter localizerAdapter;

    public Limelight3A limelight;
    public VoltageSensor batterySensor;

    public void init(HardwareMap hardwareMap) {
        frontLeft = new Motor(hardwareMap, "front_left");
        frontRight = new Motor(hardwareMap, "front_right");
        backLeft = new Motor(hardwareMap, "back_left");
        backRight = new Motor(hardwareMap, "back_right");
        intakeMotor = new Motor(hardwareMap, "intake");

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setOffsets(Constants.Pinpoint.X_OFFSET_MM, Constants.Pinpoint.Y_OFFSET_MM, DistanceUnit.MM);
        pinpoint.setEncoderResolution(Constants.Pinpoint.POD_TYPE);
        pinpoint.setEncoderDirections(Constants.Pinpoint.X_DIRECTION, Constants.Pinpoint.Y_DIRECTION);
        pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
        localizerAdapter = new PinpointPedroLocalizerAdapter(pinpoint);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(Constants.Limelight.DEFAULT_PIPELINE);
        limelight.start();

        batterySensor = hardwareMap.voltageSensor.iterator().next();
    }

    public double readBatteryVoltage() {
        return batterySensor.getVoltage();
    }

    public LLStatus getLimelightStatus() {
        return limelight.getStatus();
    }

    public LLResult getLimelightResult() {
        return limelight.getLatestResult();
    }
}
