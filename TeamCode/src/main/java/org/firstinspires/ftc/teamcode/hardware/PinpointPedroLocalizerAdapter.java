package org.firstinspires.ftc.teamcode.hardware;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class PinpointPedroLocalizerAdapter {

    private final GoBildaPinpointDriver pinpoint;
    private Pose latestPose = new Pose(0, 0, 0);

    public PinpointPedroLocalizerAdapter(GoBildaPinpointDriver pinpoint) {
        this.pinpoint = pinpoint;
    }

    public void updateFromPinpoint() {
        pinpoint.update();
        Pose2D pose = pinpoint.getPosition();
        latestPose = new Pose(
                pose.getX(DistanceUnit.INCH),
                pose.getY(DistanceUnit.INCH),
                pose.getHeading(AngleUnit.RADIANS)
        );
    }

    public void applyVisionPose(Pose2D limelightPose) {
        latestPose = new Pose(
                limelightPose.getX(DistanceUnit.INCH),
                limelightPose.getY(DistanceUnit.INCH),
                limelightPose.getHeading(AngleUnit.RADIANS)
        );
    }

    public void applyVisionResult(LLResult result) {
        if (!result.isValid()) {
            return;
        }
        applyVisionPose(result.getBotpose().toPose2D());
    }

    public Pose getPose() {
        return latestPose;
    }
}
