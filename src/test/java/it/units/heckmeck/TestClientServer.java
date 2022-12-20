package it.units.heckmeck;

import org.junit.jupiter.api.Test;
import Heckmeck.Client;
import Heckmeck.Server;

import static org.junit.jupiter.api.Assertions.*;

public class TestClientServer {

    @Test
    void init_server_socket(){
        Server srv = new Server();
        assertFalse(srv.ss.equals(null));
    }

    @Test
    void check_socket_port(){
        Server srv = new Server();
        assertTrue(srv.ss.getLocalPort() == 51734);
    }

    @Test
    void init_connection(){
        Server srv = new Server();
        srv.run();
    }

    @Test
    void receive_simple_message(){
        Server srv = new Server();
        Client cli = new Client();
        Thread thread = new Thread(srv); // Create temporary thread for handling both Client and Server
        thread.start();
        //srv.run();
        cli.startConnection("127.0.0.1", 51734);
        String response = cli.sendMessage("hello server");
        assertEquals("hello client", response);

    }

}
