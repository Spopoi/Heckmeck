
/*package it.units.heckmeck;

import Heckmeck.*;
import TCP.Client;
import TCP.Server.GameServer;
import TCP.Message;
import TCP.Server.TCPIOHandler;
import com.google.gson.Gson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestClientServer {

    private GameServer gameServer;
    Gson gson = new Gson();


    void waitOneSec() {
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    void get_connected_client() {
        gameServer = new GameServer();

        Thread serverThread = new Thread(gameServer);
        serverThread.start();

        Client cli = new Client(true);
        Thread cliThread = new Thread(cli);
        cliThread.start();

        gameServer.setNumberOfPlayers(1);

        cli.startConnection("127.0.0.1", 51734);
        waitOneSec();

        assertEquals("SocketHandler", gameServer.sockets.get(0).getClass().getSimpleName());
        assertEquals(1, gameServer.sockets.size());
        gameServer.close();
    }
}


    @Test
    @Disabled
    void send_message() {
        gameServer = new GameServer();

        Thread serverThread = new Thread(gameServer);
        serverThread.start();

        Client cli = new Client(true);
        Thread cliThread = new Thread(cli);
        cliThread.start();

        gameServer.setNumberOfPlayers(1);

        cli.startConnection("127.0.0.1", 51734);
        waitOneSec();

        Message message = new Message();
        message.setText("Hello player0");
        message.setOperation(Message.Action.INFO);


        gameServer.sockets.stream().forEach(client -> client.writeMessage(message));
        waitOneSec();
        String text = cli.text;


        assertEquals("Hello player0", text);
        gameServer.close();
    }


    @Test
    @Disabled
    void recieve_message() {
        gameServer = new GameServer();

        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        gameServer.setNumberOfPlayers(1);
        Client cli = new Client(true);
        Thread cliThread = new Thread(cli);
        cliThread.start();
        cli.startConnection("127.0.0.1", 51734);
        waitOneSec();
        Message message = new Message();
        message.setText("Hello Server");
        message.setOperation(Message.Action.GET_INPUT);
        cli.sendMessage(gson.toJson(message));
        waitOneSec();
        String stringedMsg = gameServer.sockets.get(0).readLine();
        System.out.println(stringedMsg);
        assertEquals(gson.toJson(message), stringedMsg);
        gameServer.close();
    }

    @Test
    @Disabled
    void init_client() {
        gameServer = new GameServer();

        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client(true);
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        gameServer.setNumberOfPlayers(1);
        cli1.startConnection("127.0.0.1", 51734);
        waitOneSec();
        boolean isInit = gameServer.sockets.get(0).initClient();
        assertTrue(isInit);
        gameServer.close();
    }

    @Test
    @Disabled
    void init_multi_clients() {
        gameServer = new GameServer();

        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client(true);
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        Client cli2 = new Client(true);
        Thread cli2Thread = new Thread(cli2);
        cli2Thread.start();
        gameServer.setNumberOfPlayers(2);

        cli1.startConnection("127.0.0.1", 51734);
        cli2.startConnection("127.0.0.1", 51734);

        waitOneSec();
        boolean isInit1 = gameServer.sockets.get(0).initClient();
        boolean isInit2 = gameServer.sockets.get(1).initClient();
        assertTrue(isInit1);
        assertTrue(isInit2);
        gameServer.close();

    }


    @Test
    @Disabled
    void select_player_name(){
        gameServer = new GameServer();

        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client(true);
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        gameServer.setNumberOfPlayers(1);
        cli1.startConnection("127.0.0.1", 51734);
        waitOneSec();
        boolean isInit = gameServer.sockets.get(0).initClient();

        TCPIOHandler io = new TCPIOHandler(gameServer);
        Message msg = new Message();
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setText("Get player name");
        io.choosePlayerName(0);

        gameServer.close();
    }
    @Test
    @Disabled
    void get_player_name_with_Interface(){
        gameServer = new GameServer();

        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client(true);
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();

        gameServer.setNumberOfPlayers(1);
        cli1.startConnection("127.0.0.1", 51734);
        waitOneSec();
        boolean isInit1 = gameServer.sockets.get(0).initClient();

        System.out.println("All Clients connected");
        TCPIOHandler io = new TCPIOHandler(gameServer);
        io.choosePlayerName(0);
        waitOneSec();
        Message msg = io.readMessage(0);
        waitOneSec();
        System.out.println(msg.text);
        assertEquals(Message.Action.RESPONSE, msg.operation);
        assertEquals("Player0", msg.text);
        gameServer.close();
    }

    @Test
    @Disabled
    void get_players_name_with_Interface(){
        gameServer = new GameServer();

        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client(true);
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        Client cli2 = new Client(true);
        Thread cli2Thread = new Thread(cli2);
        cli2Thread.start();
        Client cli3 = new Client(true);
        Thread cli3Thread = new Thread(cli3);
        cli3Thread.start();


        gameServer.setNumberOfPlayers(3);

        cli1.startConnection("127.0.0.1", 51734);
        cli2.startConnection("127.0.0.1", 51734);
        cli3.startConnection("127.0.0.1", 51734);

        waitOneSec();
        boolean isInit1 = gameServer.sockets.get(0).initClient();
        boolean isInit2 = gameServer.sockets.get(1).initClient();
        boolean isInit3 = gameServer.sockets.get(2).initClient();


        System.out.println("All Clients connected");
        TCPIOHandler io = new TCPIOHandler(gameServer);
        io.choosePlayerName(0);
        waitOneSec();
        io.choosePlayerName(1);
        waitOneSec();
        io.choosePlayerName(2);

        Message msg1 = io.readMessage(0);
        Message msg2 = io.readMessage(1);
        Message msg3 = io.readMessage(2);

        System.out.println(msg1.text);
        assertEquals(Message.Action.RESPONSE, msg1.operation);
        assertEquals("Player0", msg1.text);

        System.out.println(msg2.text);
        assertEquals(Message.Action.RESPONSE, msg2.operation);
        assertEquals("Player1", msg2.text);

        System.out.println(msg2.text);
        assertEquals(Message.Action.RESPONSE, msg2.operation);
        assertEquals("Player2", msg3.text);

        gameServer.close();

    }


    @Test
    void init_game_check_players(){
        gameServer = new GameServer();


        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client(true);
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        Client cli2 = new Client(true);
        Thread cli2Thread = new Thread(cli2);
        cli2Thread.start();
        Client cli3 = new Client(true);
        Thread cli3Thread = new Thread(cli3);
        cli3Thread.start();

        gameServer.setNumberOfPlayers(3);

        cli1.startConnection("127.0.0.1", 51734);
        cli2.startConnection("127.0.0.1", 51734);
        cli3.startConnection("127.0.0.1", 51734);
        waitOneSec();
        boolean isInit1 = gameServer.sockets.get(0).initClient();
        boolean isInit2 = gameServer.sockets.get(1).initClient();
        boolean isInit3 = gameServer.sockets.get(2).initClient();


        waitOneSec();
        TCPIOHandler io = new TCPIOHandler(gameServer);
        System.out.println("All Clients connected");

        //gameServer.game = new Game(io);
        waitOneSec();
        //

        waitOneSec();

        Arrays.stream(gameServer.game.getPlayers()).forEach(e -> System.out.println(e.getName()));
        Player[] players = gameServer.game.getPlayers();
        assertEquals(players[0].getName(), "Player0");
        assertEquals(players[1].getName(), "Player1");
        assertEquals(players[2].getName(), "Player2");
        gameServer.close();
    }

    @Test
    @Disabled
    void tryToPlay(){
        gameServer = new GameServer();


        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client(true);
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        Client cli2 = new Client(true);
        Thread cli2Thread = new Thread(cli2);
        cli2Thread.start();
        Client cli3 = new Client(true);
        Thread cli3Thread = new Thread(cli3);
        cli3Thread.start();

        gameServer.setNumberOfPlayers(2);

        cli1.startConnection("127.0.0.1", 51734);
        cli2.startConnection("127.0.0.1", 51734);
        waitOneSec();
        waitOneSec();
        gameServer.game.getDice().eraseDice();
        for(int i = 0; i<8; i++){
            gameServer.game.getDice().addSpecificDie(Die.Face.WORM);
        }


    }





    @Test
    @Disabled
    void send_game_obj() {
        gameServer = new GameServer();


        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client(true);
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        Client cli2 = new Client(true);
        Thread cli2Thread = new Thread(cli2);
        cli2Thread.start();
        Client cli3 = new Client(true);
        Thread cli3Thread = new Thread(cli3);
        cli3Thread.start();

        gameServer.setNumberOfPlayers(3);

        cli1.startConnection("127.0.0.1", 51734);
        cli2.startConnection("127.0.0.1", 51734);
        cli3.startConnection("127.0.0.1", 51734);
        waitOneSec();
        boolean isInit1 = gameServer.sockets.get(0).initClient();
        boolean isInit2 = gameServer.sockets.get(1).initClient();
        boolean isInit3 = gameServer.sockets.get(2).initClient();


        waitOneSec();
        TCPIOHandler io = new TCPIOHandler(gameServer);
        System.out.println("All Clients connected");

        gameServer.game = new Game(io);
        waitOneSec();
        gameServer.game.init();

        waitOneSec();

        Arrays.stream(gameServer.game.getPlayers()).forEach(e -> System.out.println(e.getName()));

        io.showBoardTiles(gameServer.game.getBoardTiles());
        BoardTiles receivedTiles = cli1.rxMessage.boardTiles;
        assertEquals(gameServer.game.getBoardTiles().getClass(), receivedTiles.getClass());
        //cli1.readIncomingMessage();
        io.showPlayerData(gameServer.game.getActualPlayer(), gameServer.game.getDice(), gameServer.game.getPlayers());
        // TODO fare un confronto tra stringhe, non tra oggetti

        gameServer.close();

    }



    @Test
    @Disabled
    void play_game(){
        gameServer = new GameServer();


        Thread serverThread = new Thread(gameServer);
        serverThread.start();
        Client cli1 = new Client(true);
        Thread cli1Thread = new Thread(cli1);
        cli1Thread.start();
        Client cli2 = new Client(true);
        Thread cli2Thread = new Thread(cli2);
        cli2Thread.start();
        Client cli3 = new Client(true);
        Thread cli3Thread = new Thread(cli3);
        cli3Thread.start();

        gameServer.setNumberOfPlayers(3);

        cli1.startConnection("127.0.0.1", 51734);
        cli2.startConnection("127.0.0.1", 51734);
        cli3.startConnection("127.0.0.1", 51734);
        waitOneSec();
        boolean isInit1 = gameServer.sockets.get(0).initClient();
        boolean isInit2 = gameServer.sockets.get(1).initClient();
        boolean isInit3 = gameServer.sockets.get(2).initClient();

        while(!gameServer.isRoomClosed());
        //boolean isInit1 = gameServer.clients.get(0).initClient();
        //boolean isInit2 = gameServer.clients.get(1).initClient();
        //boolean isInit3 = gameServer.clients.get(2).initClient();

        waitOneSec();
        TCPIOHandler io = new TCPIOHandler(gameServer);
        System.out.println("All Clients connected");

        gameServer.game = new Game(io);
        waitOneSec();
        gameServer.game.init();
        gameServer.game.play();
        gameServer.close();

    }
}
*/