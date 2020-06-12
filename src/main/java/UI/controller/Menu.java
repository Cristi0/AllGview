package UI.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.List;

public class Menu {

    /**
     * Returneaza un meniu default pentru editare de text.
     * El contine: Undo, Redo, Cut, Copy, Paste, Delete, Select all
     *
     * @param t
     * @return
     */
    public static List<MenuItem> createDefaultMenuItems(TextInputControl t) {
        MenuItem undo = new MenuItem("Undo");
        undo.setOnAction(e -> t.undo());
        MenuItem redo = new MenuItem("Redo");
        redo.setOnAction(e -> t.redo());
        MenuItem cut = new MenuItem("Cut");
        cut.setOnAction(e -> t.cut());
        MenuItem copy = new MenuItem("Copy");
        copy.setOnAction(e -> t.copy());
        MenuItem paste = new MenuItem("Paste");
        paste.setOnAction(e -> t.paste());
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> t.deleteText(t.getSelection()));
        MenuItem selectAll = new MenuItem("Select All");
        selectAll.setOnAction(e -> t.selectAll());

        BooleanBinding emptySelection = Bindings.createBooleanBinding(() ->
                        t.getSelection().getLength() == 0,
                t.selectionProperty());

        BooleanBinding undoable = Bindings.createBooleanBinding(() -> t.isUndoable() == false, t.undoableProperty());
        BooleanBinding redoable = Bindings.createBooleanBinding(() -> t.isRedoable() == false, t.redoableProperty());
        cut.disableProperty().bind(emptySelection);
        copy.disableProperty().bind(emptySelection);
        delete.disableProperty().bind(emptySelection);

        redo.disableProperty().bind(redoable);
        undo.disableProperty().bind(undoable);
        return Arrays.asList(undo, redo, cut, copy, paste, delete, new SeparatorMenuItem(), selectAll);
    }

    public static void getHintsForDefaultMenuItems(double y) {
        if (0 < y && y < 33) {
            Platform.runLater(() -> {
                MainController.Hint.setText("Hint: Returns text to a previous state.");
            });
        }
        if (32 < y && y < 58) {
            Platform.runLater(() -> {
                MainController.Hint.setText("Hint: Returns text to a following state.");
            });
        }
        if (57 < y && y < 83) {
            Platform.runLater(() -> {
                MainController.Hint.setText("Hint: Copy selected text in clipboard then deletes it.");
            });
        }
        if (82 < y && y < 108) {
            Platform.runLater(() -> {
                MainController.Hint.setText("Hint: Copy selected text in clipboard.");
            });
        }
        if (107 < y && y < 133) {
            Platform.runLater(() -> {
                MainController.Hint.setText("Hint: Paste text at the indicator.");
            });
        }
        if (132 < y && y < 159) {
            Platform.runLater(() -> {
                MainController.Hint.setText("Hint: Delete selected text.");
            });
        }if (158 < y && y < 186) {
            Platform.runLater(() -> {
                MainController.Hint.setText("Hint: Select all the text in this zone.");
            });
        }
    }
}
