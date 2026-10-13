package com.example.k8s;

/** Pure logic behind the probes. A real service would check the database and broker here. */
public final class HealthStatus {

    public enum Status { UP, DOWN }

    private final boolean databaseReachable;
    private final boolean warmedUp;

    public HealthStatus(boolean databaseReachable, boolean warmedUp) {
        this.databaseReachable = databaseReachable;
        this.warmedUp = warmedUp;
    }

    /** Liveness only asks whether the process is responsive. Readiness also needs dependencies. */
    public Status live() {
        return Status.UP;
    }

    public Status ready() {
        return databaseReachable && warmedUp ? Status.UP : Status.DOWN;
    }
}
