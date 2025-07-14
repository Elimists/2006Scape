package com.rs2.enums;

public enum RespawnLocation {
    LUMBRDIGE(3210, 3424),
    FALADOR(2965, 3379);

    public final int x, y;

    RespawnLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
