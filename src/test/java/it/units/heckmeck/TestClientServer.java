package it.units.heckmeck;

import org.junit.jupiter.api.Test;
import Heckmeck.Client;
import Heckmeck.GameServer;

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
        Thread t1 =new Thread(gameServer);
        t1.start();
        Client cli = new Client();

        cli.startConnection("127.0.0.1", 51734);
        String response = cli.sendMessage("hello server");
        System.out.println(response);
        String request = gameServer.readReceivedMessage();
        System.out.println(request);
        assertTrue(request.equals("hello server"));
        try {
            t1.join();
            gameServer.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    void send_recieve_message(){
        //GameServer gameServer =new GameServer();
        Thread t1 =new Thread(gameServer);
        t1.start();
        Client cli = new Client();
        Thread thread = new Thread(gameServer); // Create temporary thread for handling both Client and Server
        thread.start();
        //srv.run();
        cli.startConnection("127.0.0.1", 51734);
        String response = cli.sendMessage("hello server");
        assertEquals("hello client", response);
        try {
            t1.join();
            gameServer.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
