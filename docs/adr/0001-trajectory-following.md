# ADR 0001: Use Pedro Pathing via SolversLib for trajectory following

## Context
The imported FTCLib/FTCLib-Quickstart baseline (POWERPLAY-era, ~SDK v8.2) shipped `AutonomousOpMode.java` using `RamseteCommand`, `RamseteController`, and `TrajectoryGenerator`. For the current BIOBUZZ-ready stack, this is outdated versus current FTC community practice and requires additional tuning overhead compared to modern waypoint/Bézier based path following.

## Decision
We removed inherited Ramsete-based trajectory code from TeamCode and adopted Pedro Pathing. Integration is done through SolversLib (`org.solverslib:pedroPathing`) to preserve the command/subsystem architecture migrated from FTCLib to SolversLib. We additionally include `com.pedropathing:ftc` from `https://mymaven.bylazar.com/releases`.

We considered Pedro Pathing's lightweight command layer Ivy, but selected SolversLib command integration to keep one consistent command-based architecture across subsystems and opmodes.

## Consequences
- Deleted legacy Ramsete trajectory usage from TeamCode.
- Added Pedro-path-driven drivetrain and `DriveToPoseCommand` command flow.
- Autonomous opmodes now build and execute Pedro `PathChain` objects.
- Future maintainers should keep path logic in commands/subsystems and avoid reintroducing Ramsete classes.
