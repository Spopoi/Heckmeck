package it.units.heckmeck.TCP;

import Heckmeck.Game;
import Heckmeck.Components.Player;
import TCP.Message;
import TCP.Server.ClientHandler;
import TCP.TCPIOHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

        Player[] players = {pl1, pl2};

        when(game.getActualPlayer()).thenReturn(pl1);

        //when(game.getActualPlayer().getPlayerID()).thenReturn(0);

        io = new TCPIOHandler(sockets);
    }

    @Test
    public void testGetOtherPlayers(){
        List<ClientHandler> others = io.getOtherPlayersSockets(game.getActualPlayer());
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
        assertEquals("Press enter to start your turn", capturedMessage.text);
        assertEquals(Message.Action.BEGIN_TURN, capturedMessage.operation);

        verify(sockets.get(1)).writeMessage(messageCaptor.capture());
        capturedMessage = messageCaptor.getValue();
        assertEquals("This is not your turn, please wait", capturedMessage.text);
        assertEquals(Message.Action.INFO, capturedMessage.operation);
    }
    @Disabled
    public void testChoosePlayerName(){
        Message respMsg = Message.generateMessage();
        respMsg.setText("playerName").
                setOperation(Message.Action.RESPONSE).
                setPlayerID(0);
        when(sockets.get(0).writeLine(Message.generateMessage().
                setOperation(Message.Action.GET_PLAYER_NAME).
                setText("Choose player name").
                setPlayerID(0).toJSON())).thenReturn(respMsg.toJSON());
        String response = io.choosePlayerName(Player.generatePlayer(0));
        Assertions.assertEquals("playerName", response);
    }
/*
    @Test
    public void testWantToPick(){
        Message respMsg = Message.generateMessage();
        respMsg.setText("y");
        msg.setActualPlayer(game.getPlayers()[0]);
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setText("Do you want to pick tile n. 10 ?");
        msg.setPlayerID(0);
        when(sockets.get(0).readReceivedMessage()).thenReturn(respMsg);

        boolean yn = io.wantToPick(game.getActualPlayer(), 10, 10);
        Assertions.assertEquals(true, yn );
    }
*/
    @Disabled
    @Test
    public void testGetYesAnswer(){
        Message msg = Message.generateMessage().
                setOperation(Message.Action.GET_INPUT).
                setText("TEST REQ");

        when(sockets.get(0).writeMessage(msg)).thenReturn(
                Message.generateMessage().
                        setText("y").
                        setOperation(Message.Action.RESPONSE)

        );
        boolean decision = io.getYesOrNoAnswer(0, "TEST REQ");

        Assertions.assertTrue(decision);
    }

    @Disabled
    @Test
    public void testGetNoAnswer(){
        Message msg = Message.generateMessage().
                setOperation(Message.Action.GET_INPUT).
                setText("TEST REQ");

        when(sockets.get(0).writeMessage(msg)).thenReturn(
                Message.generateMessage().
                        setText("n").
                        setOperation(Message.Action.RESPONSE)
        );
        boolean decision = io.getYesOrNoAnswer(0, "TEST REQ");
        Assertions.assertFalse(decision);
    }
    @Disabled
    @Test
    public void testGetErrorAnswer(){
        Message msg = Message.generateMessage().
                setOperation(Message.Action.GET_INPUT).
                setText("TEST REQ");

        when(sockets.get(0).writeMessage(msg)).thenReturn(
                Message.generateMessage().
                        setText("wrong string").
                        setOperation(Message.Action.RESPONSE)
        );
        boolean decision = io.getYesOrNoAnswer(0, "TEST REQ");
         msg = Message.generateMessage().
                setOperation(Message.Action.ERROR).
                setText("Invalid input, choose between y or n");
        verify(sockets.get(0)).writeLine(msg.toJSON());
    }
}
