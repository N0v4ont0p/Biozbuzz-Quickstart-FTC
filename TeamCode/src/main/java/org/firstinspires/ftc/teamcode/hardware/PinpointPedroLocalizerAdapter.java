package org.firstinspires.ftc.teamcode.hardware;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

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
        // NOTE: Pose3D has no toPose2D() convenience method in the FTC SDK.
        // The verified pattern (confirmed against the official SDK samples and
        // multiple published DECODE-season team codebases) is to pull the
        // Position (via getPosition().toUnit(...)) and the yaw (via
        // getOrientation().getYaw(...)) off the Pose3D separately.
        Pose3D botpose = result.getBotpose();
        Position position = botpose.getPosition().toUnit(DistanceUnit.INCH);
        double headingRadians = botpose.getOrientation().getYaw(AngleUnit.RADIANS);

        applyVisionPose(new Pose2D(
                DistanceUnit.INCH,
                position.x,
                position.y,
                AngleUnit.RADIANS,
                headingRadians
        ));
    }

    public Pose getPose() {
        return latestPose;
    }
}
