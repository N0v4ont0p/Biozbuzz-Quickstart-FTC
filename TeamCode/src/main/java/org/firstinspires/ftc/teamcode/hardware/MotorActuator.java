package org.firstinspires.ftc.teamcode.hardware;

import com.seattlesolvers.solverslib.hardware.motors.Motor;

public class MotorActuator implements Actuator {

    private final Motor motor;

    public MotorActuator(Motor motor) {
        this.motor = motor;
    }

    @Override
    public void setPower(double power) {
        motor.set(power);
    }
}
