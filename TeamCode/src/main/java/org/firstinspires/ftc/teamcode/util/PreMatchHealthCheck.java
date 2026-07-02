package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.hardware.Constants;
import org.firstinspires.ftc.teamcode.hardware.RobotHardware;

public final class PreMatchHealthCheck {

    private PreMatchHealthCheck() {
    }

    public static void run(TelemetryManager telemetryManager, RobotHardware hardware, Gamepad gamepad1, Gamepad gamepad2) {
        boolean unhealthy = false;

        double batteryVoltage = hardware.readBatteryVoltage();
        telemetryManager.addData("Battery Voltage", batteryVoltage);
        if (batteryVoltage < Constants.Health.LOW_BATTERY_WARNING_VOLTS) {
            telemetryManager.addWarning("Battery below threshold: " + batteryVoltage + "V");
            unhealthy = true;
        }

        Pose2D pinpointPose = hardware.pinpoint.getPosition();
        telemetryManager.addData("Pinpoint Pose", pinpointPose);

        LLStatus status = hardware.getLimelightStatus();
        telemetryManager.addData("Limelight", status.getName() + " @ " + status.getFps() + " FPS");
        telemetryManager.addData("Limelight Temp", status.getTemp());
        telemetryManager.addData("Limelight CPU", status.getCpu());
        telemetryManager.addData("Limelight Pipeline", status.getPipelineIndex() + " (" + status.getPipelineType() + ")");

        LLResult result = hardware.getLimelightResult();
        telemetryManager.addData("Limelight Valid", result.isValid());
        if (!result.isValid()) {
            telemetryManager.addWarning("Limelight result invalid. Verify cable/pipeline before match.");
            unhealthy = true;
        }

        if (unhealthy) {
            gamepad1.rumble(1000);
            gamepad2.rumble(1000);
        }

        telemetryManager.update();
    }
}
