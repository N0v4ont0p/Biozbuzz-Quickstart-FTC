package org.firstinspires.ftc.teamcode.hardware;

public interface Actuator {
    void setPower(double power);

    default void stop() {
        setPower(0.0);
    }
}
