package UI.Drawing.Graphs.Weights;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Objects;

public class Weight {

    /**
     * Creeaza o pondere de valoare "1" care se va pozitiona la mijlocul unei conexiuni (linii), intr-un pane.
     * Se seteaza ca in cazul in care se va apasa de doua ori pe acesta  sa se creeaza un TextField in care se va schimba valoarea sa
     * @param shape, conexiunea unde ponderea se va pozitiona la mijlocul acesteia
     * @param currentPane, locatia de afisare pentru modificarea valorii
     * @return Text, un numar, ponderea
     */
    public Text createWeight(Line shape, Pane currentPane){
        Text weight =new Text("1");
        weight.xProperty().bind(shape.startXProperty().add(shape.endXProperty()).divide(2));
        weight.yProperty().bind(shape.startYProperty().add(shape.endYProperty()).divide(2));
        weight.setOnMouseClicked(mouseEvent->{
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    TextField input = new TextField(weight.getText());
                    input.setTranslateX(weight.getX());
                    input.setTranslateY(weight.getY());
                    currentPane.getChildren().add(input);
                    input.requestFocus();
                    input.setOnKeyPressed(event->{
                        if (event.getCode().equals(KeyCode.ENTER)) {
                            if(!(input.getText()==null || input.getText().equals(""))){
                                try{
                                    Double.valueOf(Objects.requireNonNull(input.getText()));
                                    weight.setText(input.getText());
                                }catch (Exception e){
                                }
                            }
                            currentPane.getChildren().remove(input);
                        }
                    });
                }
            }
        });
        return weight;
    }
}
