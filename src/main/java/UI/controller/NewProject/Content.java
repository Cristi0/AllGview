package UI.controller.NewProject;

import Eroare.UnexpectedException;
import UI.controller.Controller;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Content extends Controller {
    @Override
    public void load() {
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Seteaza butoanelor de nex si back in functie de naviagrea curenta.
     * Se trimit parintelui valorile selectate
     * @param mouseEvent
     * @throws UnexpectedException
     */
    public void selected(MouseEvent mouseEvent) throws UnexpectedException {
        ProjectType parent = ((ProjectType)Creator);
        switch (parent.pozition) {
            case 0:
                parent.type = Integer.valueOf(((VBox) mouseEvent.getSource()).getId());
                parent.NextButton.setDisable(false);
                break;
            case 1:
                parent.template = Integer.valueOf(((VBox) mouseEvent.getSource()).getId());
                parent.NextButton.setDisable(false);
                break;
            default:
                throw new UnexpectedException("Counter gresit");
        }
    }
}
