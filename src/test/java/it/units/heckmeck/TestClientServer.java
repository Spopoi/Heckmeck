package it.units.heckmeck;

import CLI.CliOutputHandler;
import Heckmeck.*;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestClientServer {

private GameServer gameServer = new GameServer();
Gson gson = new Gson();


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

        assertEquals("SocketHandler", gameServer.clients.get(0).getClass().getSimpleName());
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
        Message message = new Message();
        message.setText("hello client 0");
        message.setOperation(Message.Action.INFO);

        gameServer.currentClientPlayer.writeLine(gson.toJson(message));
        waitOneSec();

        String text = cli.getMessage().text;
        System.out.println(text);

        assertEquals("hello client 0" , text );
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

        Message message = new Message();
        message.setText("Hello Server");
        message.setOperation(Message.Action.INFO);

        Message responseMessage = new Message();
        responseMessage.setText("OK");
        responseMessage.setOperation(Message.Action.INFO);
        gameServer.clients.get(0).writeLine(gson.toJson(responseMessage));

        cli.sendMessage(gson.toJson(message));

        String stringedMsg = gameServer.clients.get(0).readReceivedMessage();

        System.out.println(stringedMsg);
        assertEquals(stringedMsg, "hello server");
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

        TCPIOHandler io = new TCPIOHandler(gameServer);


        String response1 = cli1.sendMessage("hello server");


        System.out.println(response1);
        String response2 = cli2.sendMessage("hello server");
        System.out.println(response2);
        String response3 = cli3.sendMessage("hello server");
        System.out.println(response3);


        waitOneSec();



        int[] playerIds = {0, 1, 2};
        for(int id : playerIds){
            String request = io.readRxBuffer(id);
            assertEquals("hello server", request );
        }
        assertEquals("Hello player0", cli1.getMessage().text);
        assertEquals("Hello player1", cli2.getMessage().text);
        assertEquals("Hello player2", cli3.getMessage().text);

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
        TCPIOHandler io = new TCPIOHandler(gameServer);
        String response1 = cli1.sendMessage("hello server");
        waitOneSec();
        Message msg = new Message();
        msg.setOperation(Message.Action.GET_INPUT);
        io.printMessage(gson.toJson(msg));
        waitOneSec();
        String playerName = io.readMessage(0).text;

        assertEquals("Player0", playerName);
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
        TCPIOHandler io = new TCPIOHandler(gameServer);
        io.choosePlayerName(gameServer.currentClientPlayer.playerId);
        waitOneSec();

        Message msg = io.readMessage(0);
        waitOneSec();
        System.out.println(msg.text);

        System.out.println("playerID: " + gameServer.clients.get(0).playerId);
        assertEquals(Message.Action.RESPONSE, msg.operation);
        assertEquals("Player0", msg.text);
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
        TCPIOHandler io = new TCPIOHandler(gameServer);
        System.out.println("All Clients connected");

        Game game = new Game(io);
        waitOneSec();

        game.init();

        waitOneSec();

        Arrays.stream(game.getPlayers()).forEach(e -> System.out.println(e.getName()));
        Player[] players = game.getPlayers();
        assertEquals(players[0].getName(), "Player0");
        assertEquals(players[1].getName(), "Player1");
        assertEquals(players[2].getName(), "Player2");
        gameServer.close();
    }



    @Test
    void receive_game_obj() {
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

        TCPIOHandler io = new TCPIOHandler(gameServer);

        System.out.println("All Clients connected");

        Game game = new Game(io);
        waitOneSec();

        game.init();

        Arrays.stream(game.getPlayers()).forEach(e -> System.out.println(e.getName()));

        io.showBoardTiles(game.getBoardTiles());
        io.showPlayerData(game.getActualPlayer(), game.getDice(), game.getPlayers());
        gameServer.close();

    }
}
