package com.rs2.config;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GameConfig {

    private double xpRate;
    private boolean enableXpRateMultipliers;
    private int[] xpRateMultipliers;

    private boolean membersOnlyMode;
    private boolean enableTutorialIsland;
    private boolean enablePartyRoom;
    private boolean enableClueScrolls;

    private boolean enableAdminTrade;
    private boolean enableAdminSell;

    private int respawnCoordinatesX;
    private int respawnCoordinatesY;

    private int saveIntervalSeconds;
    private boolean enforceItemRequirements;
}
