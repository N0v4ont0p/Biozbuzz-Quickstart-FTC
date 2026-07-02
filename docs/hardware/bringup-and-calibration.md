# Hardware bring-up and calibration

This document covers the physical setup work that sits between "the code compiles" and "the robot behaves correctly." None of these steps can be verified by CI or by an AI agent without the real robot in front of them, so each step below is written as an explicit human action.

## 1) Device-name matching (do first)
**Human action:** Open the Driver Station hardware configuration and compare every configured device name against the names hard-coded in `/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/hardware/RobotHardware.java`.

`RobotHardware.init()` expects these exact, case-sensitive names:

- `front_left`
- `front_right`
- `back_left`
- `back_right`
- `intake`
- `pinpoint`
- `limelight`

If any one of these names is missing or spelled differently, `init()` will throw immediately when the opmode starts.

Cross-check those names against `/docs/hardware/wiring_map.csv`. Treat that CSV as the single source of truth for what is plugged in where. Keep its header row (`hub_name,port_number,device,subsystem,notes`) intact, and record wiring context in the existing `notes` column rather than inventing undocumented columns. Update the CSV the same day any physical wiring changes.

Current limitation to notice before you trust the CSV: it only lists Control Hub ports. If the physical robot uses an Expansion Hub for anything, `/docs/hardware/wiring_map.csv` is already out of date and must be corrected by whoever owns the physical robot.

## 2) goBILDA Pinpoint calibration
**Human action:** Put the physical robot on a work surface, inspect the installed odometry hardware, measure the mounted sensor location, and run live motion tests while watching pose telemetry.

`/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/hardware/Constants.java` currently contains:

```java
public static final double X_OFFSET_MM = 0.0;
public static final double Y_OFFSET_MM = 0.0;
public static final GoBildaPinpointDriver.GoBildaOdometryPods POD_TYPE = GoBildaOdometryPods.goBILDA_4_BAR_POD;
public static final GoBildaPinpointDriver.EncoderDirection X_DIRECTION = EncoderDirection.FORWARD;
public static final GoBildaPinpointDriver.EncoderDirection Y_DIRECTION = EncoderDirection.FORWARD;
```

All five values above are unverified placeholders, not measurements. They are only correct by coincidence if the Pinpoint is mounted exactly at the robot's center of rotation and both encoder channels happen to be wired in the default forward orientation, which is unlikely on a real chassis.

Calibrate in this order:

1. Confirm the physically purchased odometry pod type matches `POD_TYPE`. The scaling changes if the robot is using a different goBILDA pod variant than `goBILDA_4_BAR_POD`.
2. Physically measure, in millimeters, the X and Y distance from the robot's center of rotation to the Pinpoint sensor mounting point. Update `X_OFFSET_MM` and `Y_OFFSET_MM` with those measured values.
3. Run goBILDA's official encoder-direction push test instead of assuming both directions are `FORWARD`:
   - Push the robot in pure `+X` and confirm the reported X position increases. If it decreases, flip `X_DIRECTION` to `REVERSED`.
   - Push the robot in pure `+Y` and confirm the reported Y position increases. If it decreases, flip `Y_DIRECTION` to `REVERSED`.
4. Re-test after each change until pose increases match the real robot motion on both axes.

Illustrative reference from FTC team 15993 infO(1)Robotics' Ryuu-DECODE repository (`TeamCode/src/main/java/org/firstinspires/ftc/teamcode/pedro/Constants.java`): <https://github.com/info1robotics/Ryuu-DECODE/blob/main/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/pedro/Constants.java>

```java
public static PinpointConstants localizerConstants = new PinpointConstants()
        .forwardPodY(-128.5)
        .strafePodX(-219.13)
        .distanceUnit(DistanceUnit.MM)
        .hardwareMapName("pinpoint")
        .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
        .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
        .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);
```

This is included only as a cited real-world example. The important lesson is not the specific numbers, but that a real robot ended up with non-zero measured offsets and one reversed encoder direction. If this repository still shows `0.0`, `0.0`, and both directions set to `FORWARD`, treat that as a clear signal that calibration has not yet been done.

Once real values are measured, commit them as their own small, clearly labeled commit separate from unrelated work (for example: `Calibrate Pinpoint offsets/direction for physical robot`). Keeping calibration data isolated makes `git blame` and future maintenance much easier.

## 3) Limelight network and pipeline setup
**Human action:** Connect the Limelight to the Control Hub as a USB-Ethernet device, confirm the device comes online, then open the Limelight web UI and inspect the configured pipelines.

`/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/hardware/Constants.java` sets `Constants.Limelight.DEFAULT_PIPELINE = 0`. That only works if pipeline slot `0` has already been configured on the Limelight itself. This repository does not create or manage Limelight pipelines for you.

Typical workflow:

1. Confirm the Control Hub can see the Limelight over USB-Ethernet.
2. Open the Limelight web UI (typically `http://172.29.0.1` once connected through the Control Hub). If that address does not respond, verify the actual USB-Ethernet address from the team's current Control Hub networking setup before proceeding.
3. Verify that pipeline `0` is actually configured the way the team wants.
4. Revisit that configuration after Kickoff once the real BIOBUZZ vision task is known (AprilTag-only, pollen alignment, or something else).

## 4) Battery telemetry threshold sanity check
**Human action:** Decide, as a team, what voltage should trigger a "low battery" warning during operation and compare that decision to the current constant.

`/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/hardware/Constants.java` currently sets `Constants.Health.LOW_BATTERY_WARNING_VOLTS = 12.0`.

That is a reasonable default, but it is still a tuning choice. Some teams prefer an earlier warning such as `12.5V` so the operator gets more notice before brownout risk increases. Do not inherit `12.0V` silently; confirm it deliberately against the team's battery rotation and match-day habits.
