package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.hardware.Actuator;

import java.util.function.BooleanSupplier;

public class IntakeSubsystem extends SubsystemBase {

    private final Actuator intakeActuator;
    private final BooleanSupplier hasPollenSensor;

    public IntakeSubsystem(Actuator intakeActuator, BooleanSupplier hasPollenSensor) {
        this.intakeActuator = intakeActuator;
        this.hasPollenSensor = hasPollenSensor;
    }

    public void intake() {
        intakeActuator.setPower(1.0);
    }

    public void outtake() {
        intakeActuator.setPower(-1.0);
    }

    public void stop() {
        intakeActuator.stop();
    }

    public boolean hasPollen() {
        return hasPollenSensor.getAsBoolean();
    }
}
