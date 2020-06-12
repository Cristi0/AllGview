package UI.Alert;

import javafx.scene.control.Alert;

public class Dialog {

    /**
     * Creeaza o fereastra de eroare cu un anumit mesaj
     * @param mesaj
     * @return
     */
    public static Alert error(String mesaj){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Eroare");
        a.setHeaderText(null);
        a.setContentText(mesaj);
        a.showAndWait();
        return a;
    }
}
