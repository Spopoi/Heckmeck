package it.units.heckmeck;

import Heckmeck.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestClientServer {

private GameServer gameServer = new GameServer();


    @Test
    void init_server_socket(){

        assertFalse(gameServer.ss ==(null));
        gameServer.close();
    }

    @Test
    void check_socket_port(){

        assertTrue(gameServer.ss.getLocalPort() == 51734);
        gameServer.close();
    }


    @Test
    void send_message(){

        Client cli = new Client();
        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        cli.startConnection("127.0.0.1", 51734);
        System.out.println("Client connected");
        gameServer.closeRoom();
        String response = cli.sendMessage("hello server");
        System.out.println(response);
        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        int playerId = 0;
        String request = tcpInput.readMessage(playerId);
        gameServer.close();
        assertEquals(response, "hello client 1");
    }


    @Test
    void recieve_message(){
        Client cli = new Client();

        Thread serverThread = new Thread(gameServer);
        serverThread.start();

        cli.startConnection("127.0.0.1", 51734);
        gameServer.closeRoom();

        System.out.println("Client connected");
        String response = cli.sendMessage("hello server");

        System.out.println(response);
        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        int playerId = 0;
        String request = tcpInput.readMessage(playerId);
        gameServer.close();
        assertEquals(request, "hello server");
    }

    @Test
    void connect_multi_clients(){
        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client();
        Client cli2 = new Client();
        Client cli3 = new Client();
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        Thread cli2Thread = new Thread(cli2);
        cli2Thread.start();
        Thread cli3Thread = new Thread(cli3);
        cli3Thread.start();
        cli1.startConnection("127.0.0.1", 51734);
        cli2.startConnection("127.0.0.1", 51734);
        cli3.startConnection("127.0.0.1", 51734);

        System.out.println("All Clients connected");


        String response1 = cli1.sendMessage("hello server");
        String response2 = cli2.sendMessage("hello server");
        String response3 = cli3.sendMessage("hello server");
        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        int[] playerIds = {0, 1, 2};
        for(int id : playerIds){
            String request = tcpInput.readMessage(id);
            assertEquals(request, "hello server");
        }
        assertEquals("hello client 1", response1);
        assertEquals("hello client 2", response2);
        assertEquals("hello client 3", response3);

        gameServer.close();

    }













}
