package Heckmeck;

/**
 * Network configuration constants for multiplayer games.
 */
public final class NetworkConfig {
    
    private NetworkConfig() {} // Utility class
    
    /**
     * Default port for game server.
     * Can be overridden via system property: -Dheckmeck.port=12345
     */
    public static final int DEFAULT_PORT = 51734;
    
    /**
     * Gets the configured server port.
     */
    public static int getServerPort() {
        String portProperty = System.getProperty("heckmeck.port");
        if (portProperty != null) {
            try {
                return Integer.parseInt(portProperty);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port property: " + portProperty + ", using default " + DEFAULT_PORT);
            }
        }
        return DEFAULT_PORT;
    }
    
    /**
     * Connection timeout in seconds.
     */
    public static final int CONNECTION_TIMEOUT_SECONDS = 30;
    
    /**
     * Socket read timeout in milliseconds.
     */
    public static final int SOCKET_TIMEOUT_MS = 60_000; // 1 minute
}
