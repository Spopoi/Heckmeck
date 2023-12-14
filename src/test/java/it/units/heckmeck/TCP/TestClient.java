package it.units.heckmeck.TCP;

import Heckmeck.IOHandler;
import TCP.Client;
import TCP.Message;
import TCP.Server.GameServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)

public class TestClient {
    PrintWriter out;
    BufferedReader in;
    Client client;
    IOHandler io;
    @BeforeEach
    void setup(){
        out = mock(PrintWriter.class);
        in = mock(BufferedReader.class);
        io = mock(IOHandler.class);
        client = new Client(false, io, in, out);

    }

    @Test
    void testSendMessage(){
        client.sendMessage("Test Line");
        verify(out).println("Test Line");
    }

    @Test
    void testReadRxBuffer() throws IOException {
        when(in.readLine()).thenReturn("Test Line");
        Assertions.assertEquals("Test Line", client.readRxBuffer());
    }

    @Test
    void testReadIncomingMessage() throws IOException {
        Message msg = Message.generateMessage();
        msg.text = "Test Line";
        msg.operation = Message.Action.INFO;
        when(in.readLine()).thenReturn(msg.toJSON());
        Message rxMsg = client.readIncomingMessage();
        Assertions.assertEquals(msg.toJSON(), rxMsg.toJSON());
    }

    @Test
    void testPerformGetInput(){
        when(io.getInputString()).thenReturn("Test Input");
        Message txMessage = Message.generateMessage();
        txMessage.setOperation(Message.Action.RESPONSE);
        txMessage.setText("Test Input");
        txMessage.setPlayerID(0);
        client.perform_get_input();
        verify(out).println(txMessage.toJSON());

    }
    @Test
    void testCommandInterpreter_GET_PLAYER_NAME(){
        Message msg = Message.generateMessage();
        msg.setOperation(Message.Action.GET_PLAYER_NAME);


    }


}
