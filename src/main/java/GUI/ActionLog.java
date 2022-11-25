package GUI;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class ActionLog {
    private static Label actionLog = new Label();

    public static Label getLogLabel(){
        actionLog.setFont(Font.font("Verdana", 15));
        return actionLog;
    }

    public static void setLastAction(String text){
        actionLog.setText(text);
    }
}
