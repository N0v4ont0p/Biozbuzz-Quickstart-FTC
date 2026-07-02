package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

public final class Constants {

    private Constants() {
    }

    public static final class Health {
        public static final double LOW_BATTERY_WARNING_VOLTS = 12.0;

        private Health() {
        }
    }

    public static final class Limelight {
        public static final int DEFAULT_PIPELINE = 0;

        private Limelight() {
        }
    }

    public static final class Pinpoint {
        public static final double X_OFFSET_MM = 0.0;
        public static final double Y_OFFSET_MM = 0.0;
        public static final GoBildaPinpointDriver.GoBildaOdometryPods POD_TYPE =
                GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
        public static final GoBildaPinpointDriver.EncoderDirection X_DIRECTION =
                GoBildaPinpointDriver.EncoderDirection.FORWARD;
        public static final GoBildaPinpointDriver.EncoderDirection Y_DIRECTION =
                GoBildaPinpointDriver.EncoderDirection.FORWARD;

        private Pinpoint() {
        }
    }
}
