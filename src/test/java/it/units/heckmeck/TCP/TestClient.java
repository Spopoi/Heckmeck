package it.units.heckmeck.TCP;

import Heckmeck.IOHandler;
import TCP.Client.Client;
import TCP.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;

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
        client = new Client(io, in, out);

    }

    @Test
    void testSendMessage(){
        client.sendLine("Test Line");
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
    void testCommandInterpreter_GET_PLAYER_NAME(){
        Message msg = Message.generateMessage();
        msg.setOperation(Message.Action.GET_PLAYER_NAME);
    }




}
