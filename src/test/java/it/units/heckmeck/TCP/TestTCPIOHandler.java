package it.units.heckmeck.TCP;

import Heckmeck.Game;
import Heckmeck.Components.Player;
import TCP.Message;
import TCP.Server.ClientHandler;
import TCP.TCPIOHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestTCPIOHandler {

    List<ClientHandler> sockets;
    Message msg;
    Game game;
    TCPIOHandler io;
    @BeforeEach
    void setUp(){
        sockets = new ArrayList<>(2);
        ClientHandler client1 = mock(ClientHandler.class);
        ClientHandler client2 = mock(ClientHandler.class);
        when(client1.getPlayerID()).thenReturn(0);
        when(client2.getPlayerID()).thenReturn(1);
        sockets.add(client1);
        sockets.add(client2);
        msg = Message.generateMessage();
        msg.setPlayerID(0);
        msg.setText("Test Line");
        game = mock(Game.class);
        Player pl1 = Player.generatePlayer(0);
        pl1.setPlayerName("0");
        Player pl2 = Player.generatePlayer(1);
        pl2.setPlayerName("1");

        io = new TCPIOHandler(sockets);
    }

    @Test
    public void testGetOtherPlayers(){
        List<ClientHandler> others = io.getOtherPlayersSockets(Player.generatePlayer(0));
        ClientHandler cl1 = sockets.get(1);
        ClientHandler cl2 = others.get(0);
        Assertions.assertEquals(cl1, cl2);
        Assertions.assertEquals(1, others.size());
    }


    @Test
    public void testPrintMessage(){
        msg.setOperation(Message.Action.INFO);
        msg.setText("Test Line");

        io.printMessage("Test Line");
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

        verify(sockets.get(0)).writeMessage(messageCaptor.capture());
        Message capturedMessage = messageCaptor.getValue();
        assertEquals("Test Line", capturedMessage.text);
        assertEquals(Message.Action.INFO, capturedMessage.operation);
        messageCaptor = ArgumentCaptor.forClass(Message.class);

        verify(sockets.get(1)).writeMessage(messageCaptor.capture());
        capturedMessage = messageCaptor.getValue();
        assertEquals("Test Line", capturedMessage.text);
        assertEquals(Message.Action.INFO, capturedMessage.operation);

    }



    @Test
    public void testShowTurnBeginConfirm() {
        // Prepare test data

        Player player = Player.generatePlayer(0);
        player.setPlayerName("TestPlayer");
        // Perform the method under test
        io.showTurnBeginConfirm(player);
        // Verify that the correct messages are sent
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        // Verify broadcast message
        verify(sockets.get(0)).writeMessage(messageCaptor.capture());
        Message capturedMessage = messageCaptor.getValue();

        assertEquals(Message.Action.BEGIN_TURN, capturedMessage.operation);

        verify(sockets.get(1)).writeMessage(messageCaptor.capture());
        capturedMessage = messageCaptor.getValue();
        assertEquals(Message.Action.WAIT, capturedMessage.operation);
    }

}
