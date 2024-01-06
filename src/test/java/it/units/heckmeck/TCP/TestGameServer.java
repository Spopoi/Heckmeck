package it.units.heckmeck.TCP;

import TCP.Server.GameServer;
import org.junit.jupiter.api.AfterEach;
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

        gameServer = new GameServer(2);

    }
    @AfterEach
    void closeUp() throws IOException {
        gameServer.close();
    }

    @Test
    void init_server_socket() {
        assertFalse(gameServer.ss == (null));
        gameServer.close();
    }

    @Test
    void testAcceptConnections() throws IOException {
        gameServer.ss = mock(ServerSocket.class);
        Socket socket = mock(Socket.class);
        when(gameServer.ss.accept()).thenReturn(socket);
        when(socket.isConnected()).thenReturn(true);
        when(socket.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(socket.getInputStream()).thenReturn(mock(InputStream.class));
        gameServer.acceptConnections();
        Assertions.assertEquals(2, gameServer.clients.size());
    }

}
