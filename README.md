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
- `docs/hardware/wiring_map.csv`
- `docs/onboarding/quickstart.md`
- `.github/workflows/build-validation.yml`

## Docs
- ADR template: `/docs/adr/template.md`
- Trajectory decision: `/docs/adr/0001-trajectory-following.md`
- Hardware abstraction decision: `/docs/adr/0002-hardware-abstraction-for-future-migration.md`
- Onboarding guide: `/docs/onboarding/quickstart.md`
- Wiring map: `/docs/hardware/wiring_map.csv`

## License
This project preserves BSD-3-Clause-Clear attribution and lineage from FTCLib-Quickstart.
See `/LICENSE` for details.
