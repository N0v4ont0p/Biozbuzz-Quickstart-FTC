package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryManager {

    private final Telemetry driverStationTelemetry;
    private final Object panelsTelemetry;

    public TelemetryManager(Telemetry telemetry) {
        this.driverStationTelemetry = telemetry;
        this.panelsTelemetry = tryCreatePanelsTelemetry(telemetry);
    }

    public void addData(String caption, Object value) {
        driverStationTelemetry.addData(caption, value);
        invokePanels("addData", new Class<?>[]{String.class, Object.class}, new Object[]{caption, value});
    }

    public void addWarning(String warning) {
        driverStationTelemetry.addLine("⚠ " + warning);
        invokePanels("addLine", new Class<?>[]{String.class}, new Object[]{"⚠ " + warning});
    }

    public void update() {
        driverStationTelemetry.update();
        invokePanels("update", new Class<?>[]{}, new Object[]{});
    }

    private Object tryCreatePanelsTelemetry(Telemetry telemetry) {
        try {
            Class<?> klass = Class.forName("com.bylazar.fullpanels.Telemetry");
            return klass.getConstructor(Telemetry.class).newInstance(telemetry);
        } catch (Exception ignored) {
            return null;
        }
    }

    private void invokePanels(String methodName, Class<?>[] signature, Object[] args) {
        if (panelsTelemetry == null) {
            return;
        }
        try {
            panelsTelemetry.getClass().getMethod(methodName, signature).invoke(panelsTelemetry, args);
        } catch (Exception ignored) {
        }
    }
}
