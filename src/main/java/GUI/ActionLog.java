package GUI;

import javafx.scene.control.Label;

public class ActionLog {
    private static Label actionLog = new Label();

    public static Label getLogLabel(){
        return actionLog;
    }

    public static void setLastAction(String text){
        actionLog.setText(text);
    }
}
