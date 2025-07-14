package com.rs2.config;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ServerConfig {
    private String serverName;
    private boolean guiEnabled;
    private boolean serverDebugEnabled;
    private boolean fileServer;
    private int httpPort;
    private int jaggrabPort;
    private int fileServerPort;
    private int worldId;
    private int maxPlayer;
    private int timeout;
    private boolean enableCycleLogging;
    private int cycleLoggingTick;
}
