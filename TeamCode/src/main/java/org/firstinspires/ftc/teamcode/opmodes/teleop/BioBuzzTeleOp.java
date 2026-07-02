package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.commands.TeleopDriveCommand;
import org.firstinspires.ftc.teamcode.hardware.MotorActuator;
import org.firstinspires.ftc.teamcode.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ScoringSubsystem;
import org.firstinspires.ftc.teamcode.util.PreMatchHealthCheck;
import org.firstinspires.ftc.teamcode.util.TelemetryManager;

@TeleOp(name = "BIOBUZZ TeleOp", group = "BIOBUZZ")
public class BioBuzzTeleOp extends CommandOpMode {

    private final RobotHardware robotHardware = new RobotHardware();
    private TelemetryManager telemetryManager;

    private DrivetrainSubsystem drivetrainSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private ScoringSubsystem scoringSubsystem;

    @Override
    public void initialize() {
        robotHardware.init(hardwareMap);

        Follower follower = new FollowerBuilder(new FollowerConstants(), hardwareMap).build();
        drivetrainSubsystem = new DrivetrainSubsystem(follower, robotHardware.localizerAdapter);
        intakeSubsystem = new IntakeSubsystem(new MotorActuator(robotHardware.intakeMotor), () -> false);
        scoringSubsystem = new ScoringSubsystem();

        telemetryManager = new TelemetryManager(telemetry);
        PreMatchHealthCheck.run(telemetryManager, robotHardware, gamepad1, gamepad2);

        drivetrainSubsystem.setDefaultCommand(new TeleopDriveCommand(
                drivetrainSubsystem,
                () -> -gamepad1.left_stick_y,
                () -> -gamepad1.left_stick_x,
                () -> -gamepad1.right_stick_x
        ));

        register(drivetrainSubsystem, intakeSubsystem, scoringSubsystem);
    }

    @Override
    public void run() {
        super.run();

        LLResult result = robotHardware.getLimelightResult();
        if (result.isValid()) {
            drivetrainSubsystem.applyVisionCorrection(result);
        }

        telemetryManager.addData("Pose", drivetrainSubsystem.getPose());
        telemetryManager.update();
    }
}
