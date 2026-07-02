# Biozbuzz-Quickstart-FTC (Team 19859)

This repository is **based on FTCLib/FTCLib-Quickstart** (BSD-3-Clause-Clear lineage preserved), then modernized for a current BIOBUZZ-ready command-based stack.

## Lineage and baseline note
- Baseline imported from `FTCLib/FTCLib-Quickstart` (POWERPLAY-era, roughly SDK v8.2 equivalent).
- This repository then modernizes forward to the FTC v11.1-era robot controller stack.

## Current stack
- SolversLib (`org.solverslib:core`, `org.solverslib:pedroPathing`)
- Pedro Pathing (`com.pedropathing:ftc`)
- Panels (`com.bylazar:fullpanels`)
- Native FTC SDK integrations:
  - `GoBildaPinpointDriver` for localization
  - `Limelight3A` (`LLStatus`, `LLResult`) for vision

## Repository layout
- `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`
  - `commands/`, `subsystems/`, `hardware/`, `opmodes/`, `util/`
- `docs/adr/` (including ADR 0001 and ADR 0002)
- `docs/research/` (competitive research and architecture comparisons)
- `docs/hardware/wiring_map.csv`
- `docs/onboarding/quickstart.md`
- `.github/workflows/build-validation.yml`

## Docs
- ADR template: `/docs/adr/template.md`
- Trajectory decision: `/docs/adr/0001-trajectory-following.md`
- Hardware abstraction decision: `/docs/adr/0002-hardware-abstraction-for-future-migration.md`
- DECODE competitor research: `/docs/research/decode-2025-26-competitor-analysis.md`
- Onboarding guide: `/docs/onboarding/quickstart.md`
- Hardware bring-up and calibration: `/docs/hardware/bringup-and-calibration.md`
- Wiring map: `/docs/hardware/wiring_map.csv`

## Setup, Verification & Bring-Up Checklist
### Phase 0 — Documentation read-through (no hardware required)
Read these documents in order before touching code or hardware:

1. `/docs/onboarding/quickstart.md` — minimal local setup, Android Studio, Panels access, and command-based architecture orientation.
2. `/docs/research/decode-2025-26-competitor-analysis.md` — competitive landscape, current accuracy caveats, and pre-Kickoff conditional recommendations.
3. `/docs/adr/0001-trajectory-following.md` — why this repository currently uses Pedro Pathing instead of Road Runner.
4. `/docs/adr/0002-hardware-abstraction-for-future-migration.md` — why hardware access is centralized behind the current abstraction layer for future migration work.

### Phase 1 — Dependency freshness verification (no hardware required)
Before each build season and periodically pre-Kickoff, complete the manual version check in [Dependency version verification](#dependency-version-verification). Do not trust unattended "latest version" lookups for these FTC community repositories.

### Phase 2 — Physical hardware bring-up and calibration (physical robot required)
Follow the full procedure in `/docs/hardware/bringup-and-calibration.md`. At a minimum, this phase must cover:

- Confirm every Driver Station hardware configuration name exactly matches the case-sensitive names used in `RobotHardware.java`.
- Compare the physical robot against `/docs/hardware/wiring_map.csv` and update that CSV the same day if wiring has changed.
- Measure and calibrate goBILDA Pinpoint offsets, pod type, and encoder directions instead of leaving placeholder defaults in place.
- Confirm Limelight USB-Ethernet connectivity and verify that pipeline slot `0` is actually configured the way the team wants on the device itself.
- Deliberately confirm the low-battery warning threshold is acceptable for the team's battery-management habits.

### Phase 3 — First compile-and-deploy test (physical robot required)
1. Open the repository in Android Studio and let Gradle sync finish cleanly.
2. Deploy the app to the Control Hub.
3. In the Driver Station opmode list, confirm both `BioBuzzTeleOp` and `BioBuzzAuto` appear.
4. Run `BioBuzzTeleOp` first and confirm drivetrain control, intake behavior, Pinpoint telemetry, and Limelight telemetry all behave sanely.
5. Only after TeleOp behaves correctly should the team attempt `BioBuzzAuto`.

### Phase 4 — Team knowledge-transfer validation
The documentation is not "done" until a teammate other than the original setup person can complete Phase 0 through Phase 3 using only the written docs in this repository, with zero verbal explanation, and succeed.

### Phase 5 — Ongoing documentation discipline
- Every future architecture decision gets a new ADR from `/docs/adr/template.md`.
- Every wiring change updates `/docs/hardware/wiring_map.csv` on the same day.
- Dependency versions get re-checked periodically before Kickoff using the manual process below.

### Phase 6 — Kickoff-triggered work (September 12, 2026)
Once the real BIOBUZZ scoring rules are public, re-open `/docs/research/decode-2025-26-competitor-analysis.md` and review its recommendations section, decide which conditional branch applies, only then implement the real logic in `/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/ScoringSubsystem.java`, and write a new ADR documenting that decision.

### Dependency version verification
Pinned today in `/TeamCode/build.dependencies.gradle`:

- `org.solverslib:core:0.3.4`
- `org.solverslib:pedroPathing:0.3.4`
- `com.pedropathing:ftc:2.0.6`
- `com.pedropathing:telemetry:1.0.0`
- `com.bylazar:fullpanels:1.0.12`

Known limitation: automated searches for "latest" versions in these niche FTC community Maven repositories have already returned contradictory and obviously unreliable answers (including answers older than the versions pinned here). Treat unattended version lookups as untrusted data for these artifacts.

Release-list links for manual human verification:

- SolversLib core: <https://repo.dairy.foundation/#/releases/org/solverslib/core>
- SolversLib pedroPathing: <https://repo.dairy.foundation/#/releases/org/solverslib/pedroPathing>
- Pedro Pathing FTC: <https://repo.dairy.foundation/#/releases/com/pedropathing/ftc>
- Pedro telemetry: <https://repo.dairy.foundation/#/releases/com/pedropathing/telemetry>
- Panels / fullpanels: <https://mymaven.bylazar.com/#/releases/com/bylazar/fullpanels>

Before each build season and periodically pre-Kickoff, a human must open these links and manually compare them against the pinned versions above, since automated lookups for these niche repositories are not reliable enough to trust unattended.

## License
This project preserves BSD-3-Clause-Clear attribution and lineage from FTCLib-Quickstart.
See `/LICENSE` for details.
