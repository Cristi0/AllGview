package UI.configuration;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Animations {
    static boolean isCollapsed = false;

    /**
     * Inchide si deschide meniul, schimband iconita butonului
     * @param pane Panoul care va fi deschis sau inchis
     * @param icon Butonul caruia i se va modicia iconitia de la > la < sau invers
     * @param openTo numraul de pixeli pentru a se vizualiza cand este deschis
     * @param closeTo numarul de pixeli pentru a se vizualiza cand este inchis
     */
    public void slideOpenOrClosePanel(VBox pane, FontAwesomeIconView icon, int openTo, int closeTo){
        isCollapsed=!isCollapsed;
        final Timeline timeline = new Timeline();
        ObservableList<Node> list=pane.getChildren();
        if(isCollapsed){

            setTextVisible(list,openTo,false);
            final KeyValue kv = new KeyValue(pane.prefWidthProperty(),closeTo, Interpolator.EASE_IN);
            final KeyFrame kf = new KeyFrame(Duration.millis(500),kv);
            timeline.getKeyFrames().add(kf);
            timeline.play();
            timeline.setOnFinished(event -> {
                icon.setIcon(FontAwesomeIcon.ARROW_RIGHT);
//                icon.getStylesheets().remove(0);
//                icon.getStylesheets().add(getClass().getResource("/css/icon-button-show.css").toExternalForm());
            });
        }else{

            final KeyValue kv = new KeyValue(pane.prefWidthProperty(),openTo, Interpolator.EASE_IN);
            final KeyFrame kf = new KeyFrame(Duration.millis(500),kv);
            timeline.getKeyFrames().add(kf);
            timeline.play();
            timeline.setOnFinished(event -> {
                icon.setIcon(FontAwesomeIcon.ARROW_LEFT);
                setTextVisible(list,closeTo,true);
//                icon.getStylesheets().remove(0);
//                icon.getStylesheets().add(getClass().getResource("/css/icon-button-hide.css").toExternalForm());
            });
        }
    }

    /**
     * Afiseaza informatiile despre butoane sau le ascunde
     * @param list obiectele pentru care se vor ascunde sau afisa informatiile
     * @param prefWidth dimensiunea, in numar de pixeli, aobiectelor cand se afiseaza sau se ascund
     * @param visible true - se afiseaza; false - se ascund
     */
    private void setTextVisible( ObservableList<Node> list, double prefWidth, boolean visible){
        for (int i = 1; i < list.size()-1; i++) {
            list.get(i).prefWidth(prefWidth);
            ((HBox) list.get(i)).getChildren().get(1).setVisible(visible);
        }
        ((VBox)list.get(list.size()-1)).getChildren().forEach(child->{
            child.prefWidth(prefWidth);
            ((HBox) child).getChildren().get(1).setVisible(visible);
        });
        list.get(list.size()-1).prefWidth(prefWidth);
    }
}
