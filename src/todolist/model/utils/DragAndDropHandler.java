package todolist.model.utils;

import todolist.model.task.Task;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.*;

public class DragAndDropHandler {


// tworzy specjalny "magazynek" na schowek, do ktorego wklada indeks wybranego elementu i podpisuje go formatem dragAndDropu
    static public class DragDetectedHandler implements EventHandler<MouseEvent> {
        private ListView ListView;

        public DragDetectedHandler(ListView ListView) {
            this.ListView = ListView;
        }

        public void handle(MouseEvent e) {
            Dragboard db = ListView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();

            if (ListView.getSelectionModel().getSelectedIndex() < 0) {
                return;
            }
            clipboardContent.put(Task.dragAndDropFormat, ListView.getItems().get(ListView.getSelectionModel().getSelectedIndex()));
            db.setContent(clipboardContent);

            e.consume();
        }
    }


// jesli nastapilo poruszenie - usuwa element z listy
    static public class DragDoneHandler implements EventHandler<DragEvent> {

        private ListView ListView;

        public DragDoneHandler(ListView ListView) {
            this.ListView = ListView;
        }

        @Override
        public void handle(DragEvent e) {
            if (e.getTransferMode() == TransferMode.MOVE) {
                ListView.getItems().remove(ListView.getSelectionModel().getSelectedIndex());
            }
            e.consume();
        }
    }


// jesli wskazujemy na liste jako miejsce docelowe i nie wskazujemy na liste z ktorej wzielismy element - akceptujemy dzialanie
    static public class DragOverHandler implements EventHandler<DragEvent> {

        private ListView ListView;

        public DragOverHandler(ListView ListView) {
            this.ListView = ListView;
        }

        @Override
        public void handle(DragEvent e) {
            if (e.getSource() == null) {
                return;
            }
            if (e.getGestureSource() != ListView && e.getDragboard().hasContent(Task.dragAndDropFormat)) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        }
    }


// jesli magazynek zawiera obiekt klasy Task - to dodaje go do listy
    static public class DropHandler implements EventHandler<DragEvent> {

        private ListView ListView;

        public DropHandler(ListView ListView) {
            this.ListView = ListView;
        }

        @Override
        public void handle(DragEvent e) {
            Dragboard dragBoard = e.getDragboard();
            boolean done = false;
            if (dragBoard.hasContent(Task.dragAndDropFormat)) {
                Task task = (Task) dragBoard.getContent(Task.dragAndDropFormat);
                ListView.getItems().add(task);
                done = true;
            }
            e.setDropCompleted(done);
            e.consume();
        }
    }





}

