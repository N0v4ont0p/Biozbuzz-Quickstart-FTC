package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;

import java.util.function.DoubleSupplier;

public class TeleopDriveCommand extends CommandBase {

    private final DrivetrainSubsystem drivetrainSubsystem;
    private final DoubleSupplier forward;
    private final DoubleSupplier strafe;
    private final DoubleSupplier turn;

    public TeleopDriveCommand(DrivetrainSubsystem drivetrainSubsystem,
                              DoubleSupplier forward,
                              DoubleSupplier strafe,
                              DoubleSupplier turn) {
        this.drivetrainSubsystem = drivetrainSubsystem;
        this.forward = forward;
        this.strafe = strafe;
        this.turn = turn;
        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void execute() {
        drivetrainSubsystem.driveFieldCentric(forward.getAsDouble(), strafe.getAsDouble(), turn.getAsDouble());
    }
}
