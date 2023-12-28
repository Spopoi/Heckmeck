package it.units.heckmeck;

import Heckmeck.Game;
import Heckmeck.IOHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestGame {
    IOHandler io;
    Game game;
    @BeforeEach
    public void setUp(){
        io = mock(IOHandler.class);
        game = new Game(io);
    }

    @Test
    public void init(){
        when(io.chooseNumberOfPlayers()).thenReturn(2);
        when(io.choosePlayerName(any())).thenReturn("Player0", "player1");
        game.init();
        verify(io).printMessage("OK, let's begin!");
    }


}
