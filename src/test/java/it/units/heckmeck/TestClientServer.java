package it.units.heckmeck;

import Heckmeck.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestClientServer {

private GameServer gameServer = new GameServer();

    void waitOneSec(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

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
    void get_connected_client(){

        Thread serverThread = new Thread(gameServer);
        serverThread.start();

        Client cli = new Client();
        Thread cliThread = new Thread(cli);
        cliThread.start();

        gameServer.setNumberOfPlayers(1);


        cli.startConnection("127.0.0.1", 51734);
        waitOneSec();

        assertEquals("ClientHandler", gameServer.clients.get(0).getClass().getSimpleName());
        gameServer.close();
    }


    @Test
    void send_message(){

        Thread serverThread = new Thread(gameServer);
        serverThread.start();

        Client cli = new Client();
        Thread cliThread = new Thread(cli);
        cliThread.start();

        gameServer.setNumberOfPlayers(1);


        cli.startConnection("127.0.0.1", 51734);
        waitOneSec();

        System.out.println(gameServer.clients);
        //String response = cli.sendMessage("hello server");
        //String request = gameServer.currentClientPlayer.ReadIncomingMessage();
        gameServer.currentClientPlayer.writeMessage("hello client 0");
        String message = cli.readRxBuffer();
        System.out.println(message);

        assertEquals("hello client 0" , message );
        gameServer.close();
    }


    @Test
    void recieve_message(){


        Thread serverThread = new Thread(gameServer);
        serverThread.start();

        gameServer.setNumberOfPlayers(1);

        Client cli = new Client();
        Thread cliThread = new Thread(cli);
        cliThread.start();

        cli.startConnection("127.0.0.1", 51734);
        waitOneSec();

        String response = cli.sendMessage("hello server");

        int playerId = 0;
        System.out.println(playerId);
        String request = gameServer.clients.get(0).readReceivedMessage();
        System.out.println(response);

        System.out.println(request);
        gameServer.close();
        assertEquals(request, "hello server");
        gameServer.close();
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
    void select_player_name(){
        Thread serverThread = new Thread(gameServer);
        serverThread.start();

        Client cli1 = new Client();
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();

        gameServer.setNumberOfPlayers(1);
        cli1.startConnection("127.0.0.1", 51734);

        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        TCPOutputHandler tcpOut = new TCPOutputHandler(gameServer);


        String response1 = cli1.sendMessage("hello server");
        System.out.println(response1);
        tcpOut.printMessage("GET PLAYER_NAME");
        //gameServer.currentClientPlayer.writeMessage("GET PLAYER_NAME");

        String receivedCommand = cli1.readRxBuffer();
        System.out.println("command is " + receivedCommand);

        int playerId = 0;
        String request = "";
        if(receivedCommand.equals("GET PLAYER_NAME")){
            String resp = cli1.sendMessage("Player1");
            System.out.println("resp is " + resp);
            request = tcpInput.readMessage(playerId);
        }

        assertEquals("Player1", request);
        gameServer.close();


    }
    @Test
    void get_player_name_with_Interface(){
        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client();
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        gameServer.setNumberOfPlayers(1);

        cli1.startConnection("127.0.0.1", 51734);
        waitOneSec();

        System.out.println("All Clients connected");

        String response1 = cli1.sendMessage("hello server");

        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        TCPOutputHandler tcpOutput = new TCPOutputHandler(gameServer);
        IOHandler io = new IOHandler(tcpInput, tcpOutput);
        String request = tcpInput.readMessage(0);

        System.out.println("Request is: "+ request);

        io.choosePlayerName(gameServer.currentClientPlayer.playerId);

        waitOneSec();

        System.out.println("playerID: " + gameServer.clients.get(0).playerId);
        if(!cli1.readRxBuffer().equals("Insert the name for player1")){
            cli1.sendMessage("Antonio");
        }
        assertEquals("Antonio", tcpInput.readMessage(0));
        gameServer.close();

    }



    @Test
    void init_game_check_players(){
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
        waitOneSec();

        TCPInputHandler tcpInput = new TCPInputHandler(gameServer);
        TCPOutputHandler tcpOutput = new TCPOutputHandler(gameServer);

        System.out.println("All Clients connected");

        Game game = new Game(tcpOutput, tcpInput);
        cli1.sendMessage("Antonio");
        cli2.sendMessage("Mario");
        cli3.sendMessage("Giorgio");

        game.init();

        Arrays.stream(game.getPlayers()).forEach(e -> System.out.println(e.getName()));
        Player[] players = game.getPlayers();
        assertEquals(players[0].getName(), "Antonio");
        assertEquals(players[1].getName(), "Mario");
        assertEquals(players[2].getName(), "Giorgio");
        gameServer.close();
    }













}
