package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.commands.DriveToPoseCommand;
import org.firstinspires.ftc.teamcode.commands.IntakePollenCommand;
import org.firstinspires.ftc.teamcode.hardware.MotorActuator;
import org.firstinspires.ftc.teamcode.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.util.PreMatchHealthCheck;
import org.firstinspires.ftc.teamcode.util.TelemetryManager;

@Autonomous(name = "BIOBUZZ Auto", group = "BIOBUZZ")
public class BioBuzzAuto extends CommandOpMode {

    private final RobotHardware robotHardware = new RobotHardware();
    private TelemetryManager telemetryManager;

    @Override
    public void initialize() {
        robotHardware.init(hardwareMap);
        telemetryManager = new TelemetryManager(telemetry);
        PreMatchHealthCheck.run(telemetryManager, robotHardware, gamepad1, gamepad2);

        Follower follower = new FollowerBuilder(new FollowerConstants(), hardwareMap).build();
        DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem(follower, robotHardware.localizerAdapter);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(new MotorActuator(robotHardware.intakeMotor), () -> false);

        PathChain moveToPollen = follower.pathBuilder()
                .addPath(new BezierLine(new Pose(0, 0, 0), new Pose(24, 0, 0)))
                .build();

        register(drivetrainSubsystem, intakeSubsystem);

        schedule(
                new DriveToPoseCommand(drivetrainSubsystem, moveToPollen, true),
                new IntakePollenCommand(intakeSubsystem),
                new InstantCommand(() -> {
                    LLResult result = robotHardware.getLimelightResult();
                    if (result.isValid()) {
                        drivetrainSubsystem.applyVisionCorrection(result);
                    }
                })
        );
    }
}
