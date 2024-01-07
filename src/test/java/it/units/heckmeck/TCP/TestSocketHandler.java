package it.units.heckmeck.TCP;

import TCP.Message;
import TCP.Server.ClientHandler;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

class TestSocketHandler {
    PrintWriter out;
    BufferedReader in;
    ClientHandler clientHandler;
    Gson gson = new Gson();
    @BeforeEach
    void setUp(){
        out = mock(PrintWriter.class);
        in = mock(BufferedReader.class);
        clientHandler = new ClientHandler(1, in, out);
    }

    @Test
    void testReadLine() throws IOException {

        when(in.readLine()).thenReturn("Test Line");
        String result = clientHandler.readLine();
        assertEquals("Test Line", result);
    }

    @Test
    void testWriteLine() throws IOException {
        clientHandler.writeAndRead("Test Message");
        verify(out).println("Test Message");
    }

    @Test
    void testWriteMessage() {

        Message msg = Message.generateMessage();
        msg.setPlayerID(1);
        msg.setText("Test Line");
        String msgString = gson.toJson(msg);
        when(clientHandler.writeAndRead(msgString)).thenReturn(msgString);
        Message message = clientHandler.writeMessage(msg);
        assertEquals(msg.text, message.text);
    }

    @Test
    void testInitClient() throws IOException {
        Message msg = Message.generateMessage();
        msg.setPlayerID(1);
        msg.setOperation(Message.Action.INIT);
        msg.setText("Hello");
        when(in.readLine()).thenReturn(gson.toJson(msg, msg.getClass()));
        Assertions.assertTrue(clientHandler.initClient());
    }


}
