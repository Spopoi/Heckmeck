package Heckmeck.Gateway.Protocol;

import Heckmeck.GameAction;
import Heckmeck.GameState;

import java.io.Serializable;
import java.util.List;

/**
 * Protocol for client-server communication in remote games.
 * All messages are Serializable for transmission over TCP/ObjectStreams.
 */
public final class Protocol {
    
    private Protocol() {} // Utility class
    
    /**
     * Base message interface for all protocol messages.
     */
    public sealed interface Message extends Serializable {
        String getType();
    }
    
    // ========== Action Messages ==========
    
    /**
     * Client -> Server: Request to execute a GameAction.
     */
    public record ActionRequest(GameAction action, int playerIndex) implements Message {
        @Override
        public String getType() { return "ACTION_REQUEST"; }
    }
    
    /**
     * Server -> Client: Game state update after action.
     */
    public record StateUpdate(GameState state) implements Message {
        @Override
        public String getType() { return "STATE_UPDATE"; }
    }
    
    /**
     * Server -> Client: Error response when action fails.
     */
    public record ErrorResponse(List<String> errors) implements Message {
        @Override
        public String getType() { return "ERROR_RESPONSE"; }
    }
    
    // ========== Connection Messages ==========
    
    /**
     * Client -> Server: Request to join game.
     */
    public record ConnectRequest(String playerName) implements Message {
        @Override
        public String getType() { return "CONNECT_REQUEST"; }
    }
    
    /**
     * Server -> Client: Response to connection request.
     */
    public record ConnectResponse(boolean success, int assignedPlayerIndex, String message) implements Message {
        @Override
        public String getType() { return "CONNECT_RESPONSE"; }
    }
    
    /**
     * Bidirectional: Player disconnected notification.
     */
    public record DisconnectNotice(int playerIndex, String reason) implements Message {
        @Override
        public String getType() { return "DISCONNECT_NOTICE"; }
    }
    
    /**
     * Bidirectional: Keep-alive ping/pong.
     */
    public record PingPong(boolean isPing) implements Message {
        @Override
        public String getType() { return isPing ? "PING" : "PONG"; }
    }
}
