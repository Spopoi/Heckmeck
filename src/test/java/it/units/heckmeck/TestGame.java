package it.units.heckmeck;

import Heckmeck.Game;
import Heckmeck.HeckmeckRules;
import Heckmeck.IOHandler;
import Heckmeck.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestGame {
    IOHandler io;
    Game game;
    @BeforeEach
    public void setUp() throws IOException {
        io = mock(IOHandler.class);
        Rules rules = new HeckmeckRules();
        game = new Game(io, rules);
    }

    @Test
    public void init(){
        when(io.chooseNumberOfPlayers()).thenReturn(2);
        when(io.choosePlayerName(any())).thenReturn("Player0", "player1");
        game.init();
        verify(io).printMessage("OK, let's begin!");
    }


}
