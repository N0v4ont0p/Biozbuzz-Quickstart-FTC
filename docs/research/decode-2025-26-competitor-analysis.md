# DECODE 2025-26 competitor analysis for BIOBUZZ quickstart planning

This document is **preparatory competitive research only**. It compares strong public FTC DECODE (2025-26) codebases against this repository's current architecture so the team can react faster **after** BIOBUZZ Kickoff reveals the real scoring problem.

It does **not** assume BIOBUZZ equals DECODE. Any forward-looking recommendation in this document is conditional.

We paraphrase public repository structure and patterns in our own words and link back to source repos/files where possible. We do **not** vendor or copy other teams' source code into this repository.

## Our current baseline

This quickstart already has:

- SolversLib command-based structure (`TeamCode/src/main/java/org/firstinspires/ftc/teamcode/commands/`, `.../subsystems/`)
- Pedro Pathing follower integration in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/DrivetrainSubsystem.java`
- Native goBILDA Pinpoint + native Limelight wiring in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/hardware/RobotHardware.java`
- Panels-oriented operator workflow documented in `docs/onboarding/quickstart.md`
- Hardware-abstraction guidance in `docs/adr/0002-hardware-abstraction-for-future-migration.md`
- An intentionally empty scoring placeholder in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/ScoringSubsystem.java`

## Summary table

| Team | Repo | Lang / license | Verified highlights | Explicit unknowns / caveats |
|---|---|---|---|---|
| 19043 Clueless | `FTCclueless/Decode` | Java / BSD-3-Clause | Limelight over Ethernet-over-USB, Pinpoint, dual flywheel + hood + feeder, LED artifact signaling, priority `HardwareQueue`, loop telemetry, GVF path following, sensor/vision/localizer fusion | Uses its own architecture, not Pedro/SolversLib; details below are from files such as `Robot.java`, `nDecode.xml`, `Sensors.java`, `Vision.java` |
| 16093 X-Impact | `tqdmye/FTC16093-2026DECODE` | Java / BSD-style | Command-based structure, Pedro 1.x + FTCLib, dual flywheel + feeder + hood servo, velocity PIDF presets, color tuning/dashboard tools, Pinpoint constants, test OpModes | ⚠️ Motor brands/gear ratios not stated in the config the user could read — do not assume specifics beyond what you verify yourself. No hardware XML committed |
| 16093 X-Impact Premier | `lucasnotfound59/FTC16093-2026DECODE-Premier` | Java / BSD-style | Pedro 2.x, Pinpoint, Limelight3A, FullPanels, sophisticated world-frame bearing tracker with latency compensation, dual flywheel + pan + pitch/gate setup | Supplementary variant of #2, not the main repo; still useful for architecture and vision ideas |
| 22105 Runtime Terror | `Runtime-Terror-22105/el-skibo` | Java / BSD-style | Hardware XML shipped, side-by-side dual flywheel, motorized spindexer with analog absolute encoder, 3x REV Color Sensor V3, Servo Hub, Pinpoint, dual webcams, large subsystem/task architecture | No Limelight in verified XML; some inferred behavior comes from large subsystem files rather than a single architecture document |
| 18742 WoEN: AiR | `WoEN239/Decode18742` | Kotlin / MIT-style | 3 hardware threads, module interface, coroutines, hot-restart service, threaded gamepad/event bus/battery/telemetry systems, MeepMeep module | ⚠️ Hardware BOM not documented in-repo per the user's research — do not assume specifics. Runtime motion library and scoring hardware were not fully verified |
| 15993 infO(1)Robotics | `info1robotics/Ryuu-DECODE` (+ `info1robotics/DECODE`) | Kotlin+Java / MIT-style | Carries both Pedro and Road Runner, async `tasks/` framework, subsystem singleton pattern, shooter/turret evolution across two repos | ⚠️ Hardware map not examined by the user — do not assume BOM. Which motion library is primary in autonomous was not fully verified |
| 24089 Iron Lions | `IronLionsFTC/FTC24089-DECODE` | Java / MIT-variant | FTCLib-inspired task/state-machine architecture, Pedro package, Limelight3A, projectile model, turret/shooter/feed subsystems, offline projectile simulator constants | ⚠️ **Explicit accuracy warning from the user's research, preserve exactly:** line 39 (`angle_rad = 1/math.atan(...)/()`) is literally incomplete/broken WIP code in the repo — the simulator renders but the closed-form angle solver was left unfinished. Train on/reference the physics constants only, not that specific broken line. |
| 12808 RevAmped Robotics | `junkjunk123/RevAmped-Decode-V2` | Java / BSD-style | Pedro + custom `FusionLocalizer`, decomposed shooter stack (flywheel/feeder/hood/turret/gate/tracking threads), Limelight integration, OctoQuad support | Exact auto behavior and some hardware specifics were not fully read |
| 15083 Overclock | `Xtendera/OverclockDecode` | Java / BSD-style | SolversLib + Pedro, dual-motor shooter, Limelight dual-pipeline logic, tuned LUTs for velocity/hood/time, shoot-on-the-move math, turret feedforward controller | Not every command/constant was fetched; some subsystem purposes remain partially inferred |
| 3543 Titan Robotics | `trc492/Ftc2026Decode` | Java / MIT | TrcLib/FtcLib framework, very large Shooter/Spindexer/Vision subsystems, feature-flagged architecture, Limelight + classifier + AprilTag stack, auto task library | Specific Shooter/Spindexer internals are too large to treat as fully verified without deeper repo-specific review |
| 33333 WoEN: EDGE | `WoEN239/Decode33333` | Java / template-style license | Custom hardware abstraction, revolver/sorting architecture, artifact-color enum, gun control modules, custom movement/vision folders | Shooter code appears mid-refactor; one `Gun.java` is fully commented out |

## Per-team notes

### 1) Team 19043 "Clueless" — `FTCclueless/Decode`

Repo: <https://github.com/FTCclueless/Decode>

Verified from `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/nDecode.xml` and `Robot.java`:

- `EthernetDevice name="limelight"` at `ipAddress="172.29.0.1"` (Limelight 3A over Ethernet-over-USB)
- Expansion Hub 2 motors `roller`, `feed`, `shooter1`, `shooter2`
- Servo `lindex` plus CR servos `park`, `park1`, `park2`
- Digital devices `light0G` / `light0P` for green/purple indication
- Subsystems in `Robot.java`: drive, intake, shooter, park, sensors, optional vision
- Priority-based `HardwareQueue` to schedule hardware writes and control loop time
- Dashboard canvas drawing and loop-time telemetry
- `waitWhile` / `waitFor` blocking helpers with stop checks
- Repo layout directly matches the user's note: `opmodes/`, `sensors/`, `subsystems/`, `tests/`, `utils/`, `vision/`

Additional verified patterns:

- The shooter stack is split across `Shooter.java`, `Flywheel.java`, `Turret.java`, `ShotTable.java`, and `ShotTable2.java`
- The robot uses a hood-angle table plus interpolated shot data rather than a single fixed preset
- `Sensors.java` includes analog color sensing plus digital LED feedback
- `Vision.java` uses AprilTags for localization assistance

Why it matters to us:

- This is the clearest example of an elite DECODE team treating scoring as a **collection of cooperating subsystems** instead of one monolithic "shooter" class
- Their Limelight/vision/localizer interplay is relevant to our existing `RobotHardware.java` + Pinpoint/Limelight baseline, even though the implementation style is different

### 2) Team 16093 "X-Impact" — `tqdmye/FTC16093-2026DECODE`

Repo: <https://github.com/tqdmye/FTC16093-2026DECODE>

Verified from `Subsystems/Shooter.java`, `build.dependencies.gradle`, and the source tree:

- Over/under dual flywheel: `shooterUp` + `shooterDown`
- Separate `preShooter` feeder motor
- `shooterAngle` hood servo
- REV velocity PIDF via `setVelocityPIDFCoefficients(P,I,D,F)`
- Flywheels use FLOAT zero-power behavior; feeder uses BRAKE
- Presets include MID (~1320 ticks/s) and FAST (~1500 ticks/s)
- Ready window is approximately ±40 ticks/s
- Hood micro-steps are implemented as repeated +0.02 servo increments around the mid preset
- `emergency()` reverses both flywheels; `outtake()` runs feeder at `-0.8`

Verified structural patterns:

- Command-based layout: `commands/`, `controllers/`, `Subsystems/`, `opmodes/`
- Pedro 1.x + FTCLib stack from `build.dependencies.gradle`
- `Vision.java`, `AprilTagWebCam.java`, `ballDetection.java`, `colorCurveDashboard.java`
- Bundled `GoBildaPinpointDriver.java`
- Dedicated test OpModes including `ShooterVelTest.java`, `IntakeTest.java`, `driveTest.java`

Explicit caveat to preserve:

> ⚠️ Motor brands/gear ratios not stated in the config the user could read — do not assume specifics beyond what you verify yourself.

Why it matters to us:

- This is the cleanest example of a **simple, competition-usable dual-flywheel + feeder + hood preset architecture**
- The ready-to-fire tolerance window is a very actionable pattern if BIOBUZZ later turns out to reward repeatable launch timing

### 2b) Premier-event fork — `lucasnotfound59/FTC16093-2026DECODE-Premier`

Repo: <https://github.com/lucasnotfound59/FTC16093-2026DECODE-Premier>

Verified from `build.dependencies.gradle`, `Hardwares.java`, `VisionBearingTracker.java`, and `subsystems/Shooter.java`:

- Pedro 2.x (`com.pedropathing:ftc`)
- FullPanels (`com.bylazar:fullpanels`)
- Native `Limelight3A` + Pinpoint + voltage sensor
- Dual flywheel, pan motor, pitch servo, gate servo
- Vision tracker computes a **world-frame bearing** and compensates for both pan rate and robot yaw rate during Limelight latency
- Uses freshness gating so stale vision data falls back to odometry-only aiming
- Includes a dashboard kill switch for disabling Limelight during testing

Why it matters to us:

- It is the strongest directly comparable example to our repo's existing **native Pinpoint + native Limelight** architecture
- If BIOBUZZ involves aimed scoring while moving, this repo is the best evidence in the set for how much value good latency compensation can add

### 3) Team 22105 "Runtime Terror" — `Runtime-Terror-22105/el-skibo`

Repo: <https://github.com/Runtime-Terror-22105/el-skibo>

Verified from `hw-config/rs485.xml`:

- Expansion Hub 2 with goBILDA 5202 motors `shooterLeft`, `shooterRight`, `spindexer`, `intake`
- `RevColorSensorV3` devices `topSensor`, `leftSensor`, `rightSensor`
- `AnalogInput` devices `rearLeftEncoder`, `spindexEncoder`, plus `rearRightEncoder`
- `ServoHub name="Servo Hub 3"`
- Dual webcams and Pinpoint
- Dual turret servos + hood + wall/ramp servos
- PTO servo (`pto`)

Verified architecture details from tree/layout and Pedro constants:

- Large subsystem files: `ShooterSubsystem.java`, `SpindexerSubsystem.java`, `SpindexerEncoderLUT.java`
- Vendored Pedro pathing package with tuned `FollowerConstants`
- Custom gamepad/publisher/light helpers
- Pre-match UI folder `prompts/`

Why it matters to us:

- This is the strongest public example in the set of a **true scored-magazine / spindexer architecture** with absolute position awareness and multiple color sensors
- If BIOBUZZ introduces any rotating storage or selective feed requirement, this repo is more relevant than the simpler feeder-only designs

### 4) Team 18742 "WoEN: AiR" — `WoEN239/Decode18742`

Repo: <https://github.com/WoEN239/Decode18742>

Verified from `documentation/architecture.md` and repo structure:

- True multithreading across 3 hardware threads (Control Hub, Expansion Hub, Camera)
- `IModule` pattern with `process()`, `isBusy`, `dispose()`
- Kotlin coroutines used for long-running operations
- Singleton services including `ThreadManager`, `HotRun`, `ThreadedTimers`, `ThreadedGamepad`, `ThreadedEventBus`, `ThreadedBattery`, `ThreadedTelemetry`
- Dashboard-exposed configuration annotations
- Utility math/filter classes including `ExponentialFilter` and a custom `Regulator`
- `MeepMeepTesting/` present for path visualization

Explicit caveat to preserve:

> ⚠️ Hardware BOM not documented in-repo per the user's research — do not assume specifics.

Still unknown after this research:

- The exact runtime motion library was not fully confirmed
- The contents of the scoring system were not fetched deeply enough to treat the hardware as verified

Why it matters to us:

- This repo is less useful as a direct hardware template and more useful as a **systems-engineering reference** for teams that outgrow a single-thread loop
- It also demonstrates that some elite teams attack FTC reliability through infrastructure rather than only mechanism code

### 5) Team 15993 "infO(1)Robotics" — `info1robotics/Ryuu-DECODE` (+ `info1robotics/DECODE`)

Repos:

- <https://github.com/info1robotics/Ryuu-DECODE>
- <https://github.com/info1robotics/DECODE>

Verified structural findings:

- Both repos carry `pedro/` and `roadrunner/`
- Both include `tasks/`, `subsystems/`, `common/`, `enums/`
- `tasks/` is an async autonomous-task framework with serial, parallel, conditional, sleep, and action task types
- The task system is effectively identical across the two repos, while subsystem directories differ

Verified mechanism evolution:

- Earlier `DECODE` shooter uses lookup-table RPM calculation plus runtime voltage compensation
- `Ryuu-DECODE` shifts to a formula-based shooter model and different turret/shooter organization
- `Ryuu-DECODE` turret code uses field-relative target math and dual servo positioning

Explicit caveat to preserve:

> ⚠️ Hardware map not examined by the user — do not assume BOM.

Still unknown after this research:

- Which motion library is primary in competition autonomous
- The full hardware BOM

Why it matters to us:

- This is the clearest public example of a team **hedging between Pedro and Road Runner simultaneously**
- It also shows how a task framework can remain stable while subsystems evolve between robot revisions

### 6) Team 24089 "Iron Lions" — `IronLionsFTC/FTC24089-DECODE`

Repo: <https://github.com/IronLionsFTC/FTC24089-DECODE>

Verified from repo structure and subsystem files:

- FTCLib-inspired command/state-machine architecture via `lioncore/tasks/`
- Pedro-related source tree
- Limelight3A support
- `Turret.java`, `Feed.java`, `ProjectileMotion.java`, and related task classes form a dedicated scoring stack
- Offline projectile/tuning work exists in `proj_motion.py`

Physics constants verified from `proj_motion.py`:

- gravity `g = 385 in/s²`
- ball diameter `5.0 in`
- mass `75 g`
- drag coefficient `Cd = 0.3`
- effective area scaled by `0.7`
- air density `ρ = 2.37e-6 slugs/in³`
- quadratic drag form `Fd = 1/2 * Cd * ρ * A * v²`
- Euler integration `dt = 0.01s`
- example muzzle velocity `v0 = 210 in/s`

Required caveat, preserved exactly:

> ⚠️ **Explicit accuracy warning from the user's research, preserve exactly:** line 39 (`angle_rad = 1/math.atan(...)/()`) is literally incomplete/broken WIP code in the repo — the simulator renders but the closed-form angle solver was left unfinished. Train on/reference the physics constants only, not that specific broken line.

Why it matters to us:

- This repo is the best evidence in the set that elite teams were doing **offline ballistic tuning**, not just live trial-and-error
- That is relevant whether BIOBUZZ uses launching or any other mechanism that benefits from simulation-first tuning

### 7) Team 12808 "RevAmped Robotics" — `junkjunk123/RevAmped-Decode-V2`

Repo: <https://github.com/junkjunk123/RevAmped-Decode-V2>

Verified highlights:

- Pedro-based drive stack with custom `FusionLocalizer`
- Shooter broken into many focused classes (`Flywheel`, `FlywheelController`, `FeederWheel`, `Hood`, `ServoTurretMTI`, `ShooterGate`, tracking threads)
- Limelight and OpenCV/HuskyLens-related vision files coexist
- OctoQuad support present

Why it matters to us:

- This is a strong example of **decomposing scoring into many small hardware-control classes**
- It also reinforces a cross-team pattern of sensor-fused localization plus separate mechanism tracking

### 8) Team 15083 "Overclock" — `Xtendera/OverclockDecode`

Repo: <https://github.com/Xtendera/OverclockDecode>

Verified highlights:

- SolversLib command-based architecture plus Pedro
- Dual-motor shooter subsystem with tuned LUTs for velocity, hood, and time of flight
- Limelight subsystem with AprilTag and artifact-detection pipeline switching
- Feedforward turret controller and shoot-on-the-move compensation math
- Sorting mode for green vs. non-green artifacts

Why it matters to us:

- This is one of the few repos in the set already close to our **SolversLib + Pedro** direction
- It is especially useful if BIOBUZZ scoring later requires continuous interpolation instead of a few discrete presets

### 9) Team 3543 "Titan Robotics" — `trc492/Ftc2026Decode`

Repo: <https://github.com/trc492/Ftc2026Decode>

Verified highlights:

- Mature TrcLib/FtcLib framework with very large subsystem/task implementations
- `RobotParams.java` uses clean feature flags for enabling intake/spindexer/shooter/vision capabilities
- Vision stack includes Limelight, webcam, and classifier support
- Dedicated auto-task classes for pickup and shooting

Why it matters to us:

- This is a good architectural reference for **feature flags, subsystem gating, and large competition-code organization**
- It is less directly reusable for us than the SolversLib/Pedro-based repos, but still useful for structure ideas

### 10) Team 33333 "WoEN: EDGE" — `WoEN239/Decode33333`

Repo: <https://github.com/WoEN239/Decode33333>

Verified highlights:

- Custom hardware abstraction layer under `core/`
- `main/revolver/` includes `ArtefactColor.java`, `SortingRoll.java`, and a fully commented-out `Gun.java`
- `main/modules/` contains active `GunControl.java` / `TransferBall.java`
- Separate custom movement and OpenCV folders

Why it matters to us:

- It is useful mostly as a comparison point against `Decode18742`: same broader ecosystem, very different implementation style
- It also reinforces that **artifact sorting / magazine-style handling** was a real competitive pattern in DECODE

### Bonus repositories / availability notes

- `ayaan-gupta/ftc21813-opensource-research`: not located during this pass
- `fu-silent/FTC-32477-Decode-Program-History`: not located during this pass
- Fission310 DECODE repo: still not found publicly during this pass; treat as unavailable rather than inferred
- Pioneer Robotics Kotlin codebase was found and is a valuable extra reference, especially for custom pathing plus spindexer/turret/flywheel architecture, but it was outside the ranked top-10 list

## Patterns observed across elite DECODE teams

### 1) Scoring is split into multiple cooperating mechanisms

Common pattern:

- intake / transfer / feeder
- flywheel(s)
- hood or launch-angle control
- turret or pan aiming
- gate/indexer/spindexer/revolver
- sensor feedback layer

This appears in different forms across Clueless, X-Impact, Runtime Terror, RevAmped, Overclock, Titan, and WoEN EDGE.

### 2) Dual-flywheel launchers were common, but packaging varied

Observed variants:

- over/under dual flywheel (`tqdmye/FTC16093-2026DECODE`)
- side-by-side dual flywheel (`Runtime-Terror-22105/el-skibo`)
- dual flywheel with additional turret/pitch/gate integration (Premier fork, Overclock, RevAmped)

The common software implication was **closed-loop velocity control** plus either discrete presets or interpolated models.

### 3) "Ready to fire" gating mattered

Strong teams rarely treated "commanded RPM" as "ready now."

Examples:

- X-Impact uses a ±40 ticks/s window
- Other repos use moving averages, bang-bang assist, or gating logic before feed actions

This is a software pattern, not just a hardware one.

### 4) Aiming systems often combined heading + localizer + vision

Observed combinations:

- Pinpoint + Limelight / AprilTag correction
- Pinpoint + odometry-only fallback
- Turret/pan world-frame bearing tracking
- Sensor-fused localizers (`FusionLocalizer`, `nMergeLocalizer`)

The strongest vision examples treat stale vision as optional input, not a single point of failure.

### 5) Game-piece identification/sorting was a real differentiator

Observed methods:

- analog light/color sensing + LEDs (Clueless)
- `RevColorSensorV3` arrays (Runtime Terror)
- dashboard-tuned color curves and ball detection (X-Impact)
- artifact-color enums and sorting rollers/revolvers (WoEN EDGE)
- green/non-green handling modes (Overclock)

### 6) Elite teams invested in infrastructure around loop timing and reliability

Examples:

- priority hardware write queues (Clueless)
- hot restart, dedicated hardware threads, event bus, thread-safe telemetry (WoEN 18742)
- task graphs and system registries (Iron Lions, Titan, infO(1) task framework)
- bulk caching and pre-match configuration utilities

### 7) Tuning often moved beyond hand-written presets

Examples:

- lookup tables for hood and velocity
- interpolation over distance
- empirically fitted formulas
- offline projectile simulators / physics notebooks
- dashboard-exposed tunables

### 8) Some teams carried multiple motion approaches at once

Examples:

- Pedro + Road Runner in the infO(1) repos
- MeepMeep or custom pathing support alongside match code

That suggests motion-library flexibility was strategically useful during the season, even if only one stack ran in competition.

## Relevance to our architecture

| Observed pattern | Elite examples | What we already have | What we do not have yet |
|---|---|---|---|
| Command/subsystem split | X-Impact, Overclock, Titan (different framework), Iron Lions task model | SolversLib commands/subsystems in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/commands/` and `.../subsystems/` | No scoring-specific command graph yet because `ScoringSubsystem.java` is intentionally a placeholder |
| Pedro-based pathing | X-Impact, Premier fork, Runtime Terror, RevAmped, Overclock, Iron Lions | Pedro integration in `DrivetrainSubsystem.java` and ADR 0001 | No scoring-motion coupling yet (appropriately) |
| Native Pinpoint localization | X-Impact, Premier fork, Runtime Terror, Pioneer bonus, others | `RobotHardware.java` initializes `GoBildaPinpointDriver` and `PinpointPedroLocalizerAdapter` | No higher-level localization fusion beyond the current adapter |
| Native Limelight support | Premier fork, Overclock, Iron Lions, Clueless (different transport setup) | `RobotHardware.java` already owns `Limelight3A` and exposes status/result accessors | No advanced latency-compensated targeting layer yet |
| Dashboard/tuning workflow | Premier fork, Runtime Terror, Overclock, WoEN 18742 | Panels usage is already part of `docs/onboarding/quickstart.md` and the current operator workflow | No scoring-specific dashboard tuning surfaces yet |
| Hardware abstraction boundary | Many strong teams isolate hardware from logic differently | `RobotHardware.java`, `Actuator.java`, `MotorActuator.java`, `IntakeSubsystem.java`, ADR 0002 | No equivalent abstraction for future scoring actuators because the real mechanism is still unknown |
| Velocity-gated scoring control | X-Impact, Overclock, Premier fork, RevAmped | None yet in our placeholder scoring path | We intentionally have no flywheel/hood/turret logic yet |
| Spindexer/revolver state tracking | Runtime Terror, Titan, WoEN EDGE, Pioneer bonus | None | No scoring magazine/indexer concept exists in this quickstart today |
| Color sorting / artifact classification | Clueless, Runtime Terror, X-Impact, Overclock, WoEN EDGE | None in our current baseline beyond generic vision hooks | No scoring-piece sensor abstraction yet |
| Advanced loop-time infrastructure | Clueless hardware queue, WoEN multithreading, Iron Lions jobs | Current code remains intentionally simpler; TeleOp wires systems in `opmodes/teleop/BioBuzzTeleOp.java` | No queue/thread/event-bus infrastructure yet, which is acceptable for a quickstart |

## Recommendations for when BIOBUZZ Kickoff reveals real scoring mechanics

**These are options to evaluate later, not commitments.**

### If BIOBUZZ involves launching Pollen into a goal

- Evaluate a **dual-flywheel + adjustable hood** architecture rather than a single fixed-speed shooter
- Add **closed-loop velocity control** and a **ready-to-fire tolerance window** similar to the pattern in `tqdmye/FTC16093-2026DECODE:Subsystems/Shooter.java`
- Decide early whether the team needs only a few discrete presets or an interpolated LUT / fitted formula like Overclock, Clueless, or the infO(1) repos

### If BIOBUZZ requires selective feed, storage rotation, or multi-piece buffering

- Evaluate a **spindexer / revolver / indexed feeder** design instead of a simple one-ball feeder
- If the mechanism rotates through known slots, plan for **absolute position sensing** from the start, following the Runtime Terror spindexer pattern

### If BIOBUZZ involves game-piece color/identity handling

- Add a sensor abstraction first, not hard-coded match logic
- Consider using **LED/operator feedback** and dashboard-tunable classification thresholds, following Clueless and X-Impact patterns

### If scoring favors accurate aimed shots while the robot is moving

- Keep our current native Pinpoint + native Limelight foundation
- Consider adding a **latency-aware targeting layer** similar in spirit to the Premier fork's world-frame bearing tracker
- Only do this if match testing shows the extra complexity is worth it

### If tuning becomes difficult

- Prefer building a **small tuning/simulation workflow** before guessing constants in code
- Iron Lions, Overclock, and the infO(1) repos all suggest that elite teams moved tuning work offline when mechanism behavior got nonlinear

### If the final BIOBUZZ scoring mechanism is not launch-based at all

- Reuse only the **architectural** lessons: subsystem decomposition, sensor abstraction, task/state coordination, ready-state gating, and dashboard tuning
- Keep `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/ScoringSubsystem.java` as the single place where the real mechanism will later be introduced

### What not to do before Kickoff

- Do **not** guess the scoring hardware and bake it into `ScoringSubsystem.java`
- Do **not** overfit this quickstart to DECODE-specific assumptions just because Pollen is ball-shaped
- Do **not** add multithreading, spindexers, or shooter math early unless the actual BIOBUZZ rules justify that complexity

## Bottom line

Across these DECODE codebases, the strongest repeating theme is not one exact mechanism. It is that elite teams:

1. decomposed scoring into small cooperating subsystems,
2. treated sensing/localization as first-class inputs to scoring,
3. used explicit ready-state gating instead of timing guesses, and
4. invested in tuning infrastructure when the scoring problem became nonlinear.

Our quickstart already matches the **command architecture**, **Pedro direction**, and **native Pinpoint/Limelight baseline** needed to adopt those ideas later. The main deliberate gap is still the right one: `ScoringSubsystem.java` remains a placeholder until BIOBUZZ reveals what scoring actually is.
