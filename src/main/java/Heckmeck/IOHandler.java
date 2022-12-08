package Heckmeck;

import exception.HeckmeckException;

public class IOHandler {
    private final InputHandler input;
    private final OutputHandler output;

    public IOHandler(InputHandler input, OutputHandler output){
        this.input = input;
        this.output = output;
    }

    public void showWelcomeMessage(){
        output.showWelcomeMessage();
    }

    public boolean wantToPlay(){
        while(true) {
            try {
                return input.wantToPlay();
            } catch (HeckmeckException e) {
                output.showExceptionMessage(e);
            }
        }
    }



}
