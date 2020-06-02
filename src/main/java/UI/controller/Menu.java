package UI.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputControl;

import java.util.Arrays;
import java.util.List;

public class Menu {
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

        BooleanBinding undoable = Bindings.createBooleanBinding(()->t.isUndoable()==false, t.undoableProperty());
        BooleanBinding redoable = Bindings.createBooleanBinding(()->t.isRedoable()==false, t.redoableProperty());
        cut.disableProperty().bind(emptySelection);
        copy.disableProperty().bind(emptySelection);
        delete.disableProperty().bind(emptySelection);

        redo.disableProperty().bind(redoable);
        undo.disableProperty().bind(undoable);
        return Arrays.asList(undo, redo, cut, copy, paste, delete, new SeparatorMenuItem(), selectAll);
    }
}
