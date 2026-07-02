# Team 19859 BIOBUZZ Quickstart

## 1) Install tooling
1. Install Android Studio Ladybug (2024.2) or newer.
2. Install the Android SDK components requested by the FTC project.
3. Open this repository root in Android Studio.

## 2) Clone and open project
1. `git clone` this repository.
2. Open the project in Android Studio.
3. Let Gradle sync complete.

## 3) Connect robot and Panels
1. Connect to the robot Wi-Fi network.
2. Open Panels at `http://192.168.43.1:8001`.
3. Use Panels for live telemetry and tuning of Pedro/Limelight settings.

## 4) Command-based architecture (plain-English)
- **SubsystemBase** classes own robot capabilities (drivetrain, intake, scoring placeholder).
- **CommandBase** classes express robot actions using subsystem methods.
- **CommandScheduler** runs every loop and coordinates active commands safely.
- OpModes wire hardware + subsystems + commands together, then schedule behaviors.

## 5) Read the architecture and research docs
Before changing major robot architecture, read:

- `docs/adr/0001-trajectory-following.md`
- `docs/adr/0002-hardware-abstraction-for-future-migration.md`
- `docs/research/decode-2025-26-competitor-analysis.md`
