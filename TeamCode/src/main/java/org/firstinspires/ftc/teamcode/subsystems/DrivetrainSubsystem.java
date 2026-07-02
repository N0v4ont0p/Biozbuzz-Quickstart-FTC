package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.hardware.PinpointPedroLocalizerAdapter;

public class DrivetrainSubsystem extends SubsystemBase {

    private final Follower follower;
    private final PinpointPedroLocalizerAdapter localizerAdapter;

    public DrivetrainSubsystem(Follower follower, PinpointPedroLocalizerAdapter localizerAdapter) {
        this.follower = follower;
        this.localizerAdapter = localizerAdapter;
        this.follower.startTeleopDrive();
    }

    public void driveFieldCentric(double forward, double strafe, double turn) {
        follower.setTeleOpDrive(forward, strafe, turn, false);
    }

    public void followPath(Path path, boolean holdEnd) {
        follower.followPath(path, holdEnd);
    }

    public void followPath(PathChain pathChain, boolean holdEnd) {
        follower.followPath(pathChain, holdEnd);
    }

    public void applyVisionCorrection(LLResult result) {
        localizerAdapter.applyVisionResult(result);
    }

    public Pose getPose() {
        return localizerAdapter.getPose();
    }

    public boolean isBusy() {
        return follower.isBusy();
    }

    @Override
    public void periodic() {
        localizerAdapter.updateFromPinpoint();
        follower.update();
    }
}
