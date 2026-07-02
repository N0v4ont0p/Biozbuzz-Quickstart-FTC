package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.paths.PathChain;
import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;

public class DriveToPoseCommand extends CommandBase {

    private final DrivetrainSubsystem drivetrainSubsystem;
    private final PathChain pathChain;
    private final boolean holdEnd;

    public DriveToPoseCommand(DrivetrainSubsystem drivetrainSubsystem, PathChain pathChain, boolean holdEnd) {
        this.drivetrainSubsystem = drivetrainSubsystem;
        this.pathChain = pathChain;
        this.holdEnd = holdEnd;
        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void initialize() {
        drivetrainSubsystem.followPath(pathChain, holdEnd);
    }

    @Override
    public boolean isFinished() {
        return !drivetrainSubsystem.isBusy();
    }
}
