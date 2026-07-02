# ADR 0002: Hardware abstraction boundary for future SystemCore/A301 migration

## Context
The 2027-28 legal introduction of SystemCore/MotionCore + A301 actuators creates a likely future control migration. BIOBUZZ hardware remains current-generation, but code should avoid tight coupling to direct SDK motor/servo types inside subsystem logic.

## Decision
Subsystems do not instantiate `DcMotor`/`Servo` directly. Instead, subsystem constructors accept SolversLib `Motor` wrappers or thin custom abstractions (`Actuator`) injected from `RobotHardware`.

## Consequences
- Command/subsystem logic remains stable across hardware backend changes.
- Future SystemCore migration scope is reduced to hardware wrapper implementations.
- Maintainers should continue constructor-injection patterns and keep direct hardware map calls centralized in `hardware/RobotHardware.java`.
