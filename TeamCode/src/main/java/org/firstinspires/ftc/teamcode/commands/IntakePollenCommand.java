package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class IntakePollenCommand extends SequentialCommandGroup {

    public IntakePollenCommand(IntakeSubsystem intakeSubsystem) {
        addCommands(
                // Add intake lowering command here when the physical mechanism is finalized.
                new InstantCommand(intakeSubsystem::intake, intakeSubsystem),
                new WaitUntilCommand(intakeSubsystem::hasPollen),
                new InstantCommand(intakeSubsystem::stop, intakeSubsystem)
        );
    }
}
