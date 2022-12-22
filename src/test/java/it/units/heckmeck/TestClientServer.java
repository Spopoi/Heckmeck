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
        assertEquals(response, "hello client 0");
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
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        Client cli2 = new Client();
        Thread cli2Thread = new Thread(cli2);
        cli2Thread.start();
        Client cli3 = new Client();
        Thread cli3Thread = new Thread(cli3);
        cli3Thread.start();

        gameServer.setNumberOfPlayers(3);

        cli1.startConnection("127.0.0.1", 51734);
        cli2.startConnection("127.0.0.1", 51734);
        cli3.startConnection("127.0.0.1", 51734);

        System.out.println("All Clients connected");


        String response1 = cli1.sendMessage("hello server");
        System.out.println(response1);
        String response2 = cli2.sendMessage("hello server");
        System.out.println(response2);
        String response3 = cli3.sendMessage("hello server");
        System.out.println(response3);

        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        int[] playerIds = {0, 1, 2};
        for(int id : playerIds){
            String request = tcpInput.readMessage(id);
            assertEquals(request, "hello server");
        }
        assertEquals("hello client 0", response1);
        assertEquals("hello client 1", response2);
        assertEquals("hello client 2", response3);

        gameServer.close();

    }

    @Test
    void choose_player_name(){
        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client();
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();

        gameServer.setNumberOfPlayers(1);
        cli1.startConnection("127.0.0.1", 51734);

        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        TCPOutputHandler tcpOutput = new TCPOutputHandler(gameServer);
        IOHandler io = new IOHandler(tcpInput, tcpOutput);

        //String response1 = cli1.sendMessage("hello server");
        String command = cli1.readRxBuffer();
        System.out.println(command);
        String name = "";

        /*while(true){
            if(cli1.readRxBuffer().equals("GET PLAYER_NAME")){
                System.out.println("After if");
                //cli1.sendMessage("Player1");
                name = gameServer.clients.get(0).readReceivedMessage();
        }

        }*/
        //assertEquals("Player1", name);


    }
    @Test
    void get_player_Names(){
        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client();
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        Client cli2 = new Client();
        Thread cli2Thread = new Thread(cli2);
        cli2Thread.start();
        Client cli3 = new Client();
        Thread cli3Thread = new Thread(cli3);
        cli3Thread.start();

        gameServer.setNumberOfPlayers(3);

        cli1.startConnection("127.0.0.1", 51734);
        cli2.startConnection("127.0.0.1", 51734);
        cli3.startConnection("127.0.0.1", 51734);

        System.out.println("All Clients connected");


        String response1 = cli1.sendMessage("hello server");
        String response2 = cli2.sendMessage("hello server");
        String response3 = cli3.sendMessage("hello server");

        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        TCPOutputHandler tcpOutput = new TCPOutputHandler(gameServer);
        IOHandler io = new IOHandler(tcpInput, tcpOutput);

        gameServer.clients.stream().forEach(client -> io.choosePlayerName(client.playerId));

        if(cli1.readRxBuffer().equals("GET PLAYER_NAME")){
            String playerName = "Player1";
            cli1.sendMessage(playerName);
            System.out.println(gameServer.clients.get(0));
        }

    }




    @Test
    void create_game_check_num_of_players(){
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

        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        TCPOutputHandler tcpOutput = new TCPOutputHandler(gameServer);


        System.out.println("All Clients connected");

        Game game = new Game(tcpOutput, tcpInput);
        game.init();




    }













}
