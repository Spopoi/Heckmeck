package it.units.heckmeck.TCP;

import TCP.Server.GameServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TestGameServer {

    private GameServer gameServer;
    @BeforeEach
    void setUp() throws IOException {
        Socket socket = mock(Socket.class);
        gameServer = new GameServer();
        gameServer.ss = mock(ServerSocket.class);
        when(gameServer.ss.accept()).thenReturn(socket);
        when(socket.isConnected()).thenReturn(true);
        when(socket.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(socket.getInputStream()).thenReturn(mock(InputStream.class));
    }

    @Test
    void init_server_socket() {
        gameServer = new GameServer();
        assertFalse(gameServer.ss == (null));
        gameServer.close();
    }

    @Test
    void check_socket_port() {
        gameServer = new GameServer();

        assertTrue(gameServer.ss.getLocalPort() == 51734);
        gameServer.close();
    }

    @Test
    void testAcceptConnections() throws IOException {
        gameServer.acceptConnections();
        Assertions.assertEquals(8, gameServer.sockets.size());
    }
}
