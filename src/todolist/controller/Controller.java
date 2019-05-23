package todolist.controller;

import todolist.Main;
import todolist.model.file.CSVFileHandler;
import todolist.model.file.CSVHandler;
import todolist.model.file.SerFileHandler;
import todolist.model.task.Task;
import todolist.model.task.TaskPriority;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.util.Callback;
import todolist.model.utils.DragAndDropHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Controller {

    private Main mainApp;

    @FXML
    Button newTaskButton;
    @FXML
    private ListView toDoListView;
    @FXML
    private ListView inProgListView;
    @FXML
    private ListView doneListView;


    @FXML
    public void closeWindow() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(mainApp.getMainWindow());
        alert.setTitle("Exit");
        alert.setHeaderText("Do you want to save file before closing?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton){
            saveToFile();
            Platform.exit();
        } else if (result.get() == noButton) {
            Platform.exit();
        }
    }

    @FXML
    public void showAuthorInformation(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainApp.getMainWindow());
        alert.setTitle("Author Information");
        alert.setHeaderText(null);
        alert.setContentText("Name: M\nSurname: Mydlak");
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
//        toDoColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
//        inProgColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
//        doneColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());

        toDoListView.setCellFactory(new Callback() {
            @Override
            public Object call(Object param) {
                return new ListCell<Task>() {
                    @Override
                    protected void updateItem(Task paramT, boolean paramBoolean) {

                        super.updateItem(paramT, paramBoolean);

                        if (!isEmpty()) {
                            setCellColor(this, paramT);

                            Tooltip tooltip = new Tooltip();
                            tooltip.setText(paramT.getDescription());
                            setTooltip(tooltip);


                            ContextMenu contextMenu = new ContextMenu();
                            MenuItem deleteContMenuItem = new MenuItem("Delete");
                            deleteContMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    mainApp.getToDoTaskList().remove(paramT);
                                }
                            });
                            MenuItem editContMenuItem = new MenuItem("Edit");
                            editContMenuItem.setOnAction(e -> editExistingTask(paramT));
                            contextMenu.getItems().addAll(deleteContMenuItem, editContMenuItem);
                            setContextMenu(contextMenu);

                            getListView().refresh();

                        } else if (isEmpty()) {
                            setStyle(null);
                            setText(null);
                            setContextMenu(null);
                        }
                    }
                };
            }
        });



        inProgListView.setCellFactory(new Callback() {
            @Override
            public Object call(Object param) {
                return new ListCell<Task>() {
                    @Override
                    protected void updateItem(Task paramT, boolean paramBoolean) {

                        super.updateItem(paramT, paramBoolean);

                        if (!isEmpty()) {
                            setCellColor(this, paramT);

                            Tooltip tooltip = new Tooltip();
                            tooltip.setText(paramT.getDescription());
                            setTooltip(tooltip);


                            ContextMenu contextMenu = new ContextMenu();
                            MenuItem deleteContMenuItem = new MenuItem("Delete");
                            deleteContMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    mainApp.getInProgTaskList().remove(paramT);
                                }
                            });
                            MenuItem editContMenuItem = new MenuItem("Edit");
                            editContMenuItem.setOnAction(e -> editExistingTask(paramT));
                            contextMenu.getItems().addAll(deleteContMenuItem, editContMenuItem);
                            setContextMenu(contextMenu);

                            getListView().refresh();

                        } else if (isEmpty()) {
                            setStyle(null);
                            setText(null);
                            setContextMenu(null);
                        }
                    }
                };
            }
        });



        doneListView.setCellFactory(new Callback() {
            @Override
            public Object call(Object param) {
                return new ListCell<Task>() {
                    @Override
                    protected void updateItem(Task paramT, boolean paramBoolean) {

                        super.updateItem(paramT, paramBoolean);

                        if (!isEmpty()) {
                            setCellColor(this, paramT);

                            Tooltip tooltip = new Tooltip();
                            tooltip.setText(paramT.getDescription());
                            setTooltip(tooltip);

                            ContextMenu contextMenu = new ContextMenu();
                            MenuItem deleteContMenuItem = new MenuItem("Delete");
                            deleteContMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent e) {
                                    mainApp.getDoneTaskList().remove(paramT);
                                }
                            });
                            MenuItem editContMenuItem = new MenuItem("Edit");
                            editContMenuItem.setOnAction(e -> editExistingTask(paramT));
                            contextMenu.getItems().addAll(deleteContMenuItem, editContMenuItem);
                            setContextMenu(contextMenu);

                            getListView().refresh();

                        } else if (isEmpty()) {
                            setStyle(null);
                            setText(null);
                            setContextMenu(null);
                        }
                    }
                };
            }
        });

    }

    private void setCellColor(ListCell<Task> listCell, Task paramT) {
            listCell.setText(paramT.getTitle());
            if (paramT.getPriority() == TaskPriority.LOW) {
                String style = "-fx-background-color: #c1ffbb;";
                listCell.setStyle(style);
            } else if (paramT.getPriority() == TaskPriority.MEDIUM) {
                String style = "-fx-background-color: rgb(255,251,185);";
                listCell.setStyle(style);
            } else if (paramT.getPriority() == TaskPriority.HIGH) {
                String style = "-fx-background-color: rgb(255,211,207);";
                listCell.setStyle(style);
            }
    }

    @FXML
    public void addNewTask() {
        mainApp.showTaskEditDialog(new Task());
    }

    @FXML
    public void editExistingTask(Task task) {
        mainApp.showTaskEditDialog(task);
    }

    public void setMainApp(Main mainApp){
        this.mainApp = mainApp;
        mainApp.getMainWindow().setOnCloseRequest(e -> {e.consume(); closeWindow();});

        toDoListView.setItems(mainApp.getToDoTaskList());
        inProgListView.setItems(mainApp.getInProgTaskList());
        doneListView.setItems(mainApp.getDoneTaskList());

        toDoListView.setOnDragDetected(new DragAndDropHandler.DragDetectedHandler(toDoListView));
        inProgListView.setOnDragDetected(new DragAndDropHandler.DragDetectedHandler(inProgListView));
        doneListView.setOnDragDetected(new DragAndDropHandler.DragDetectedHandler(doneListView));

        toDoListView.setOnDragDone(new DragAndDropHandler.DragDoneHandler(toDoListView));
        inProgListView.setOnDragDone(new DragAndDropHandler.DragDoneHandler(inProgListView));
        doneListView.setOnDragDone(new DragAndDropHandler.DragDoneHandler(doneListView));

        toDoListView.setOnDragOver(new DragAndDropHandler.DragOverHandler(toDoListView));
        inProgListView.setOnDragOver(new DragAndDropHandler.DragOverHandler(inProgListView));
        doneListView.setOnDragOver(new DragAndDropHandler.DragOverHandler(doneListView));

        toDoListView.setOnDragDropped(new DragAndDropHandler.DropHandler(toDoListView));
        inProgListView.setOnDragDropped(new DragAndDropHandler.DropHandler(inProgListView));
        doneListView.setOnDragDropped(new DragAndDropHandler.DropHandler(doneListView));

//        FileOutputStream fs;
//        FileInputStream fs2;
//        try {
//            fs = new FileOutputStream("osoba.txt");
//            fs2 = new FileInputStream("osoba.txt");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return;
//        }
//        DataOutputStream ds = new DataOutputStream(fs);
//        DataInputStream ds2 = new DataInputStream(fs2);
//        String save = "\"koma\nkoko";
//        if(save.contains("\n")) System.out.println("zawiera");
//        try {
//            ds.writeBytes(save);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    @FXML
    public void saveToFile() {
        List<List<Task>> toSave = new ArrayList<>();
        toSave.add(new ArrayList<>(mainApp.getToDoTaskList()));
        toSave.add(new ArrayList<>(mainApp.getInProgTaskList()));
        toSave.add(new ArrayList<>(mainApp.getDoneTaskList()));

        try {
            if (toSave.get(0).size()==0 && toSave.get(1).size()==0 && toSave.get(2).size()==0) {
                throw new Exception("There is no data to save.");
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save to file");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"),
                    new FileChooser.ExtensionFilter("Ser Files (*.ser)", "*.ser"));

            File file = fileChooser.showSaveDialog(mainApp.getMainWindow());

            if (file != null) {
                String extension = fileChooser.selectedExtensionFilterProperty().get().getExtensions().get(0).substring(1);

                List<String> list = new ArrayList<>();
                for (List<Task> li : toSave) {
                    list.add(CSVHandler.getCSV(li));
                }
                if (extension.equals(".csv")) {
                    CSVFileHandler.saveTo(file.getAbsolutePath(), list);
                }
                else if (extension.equals(".ser")) {
                    SerFileHandler.saveTo(file.getAbsolutePath(), toSave);
                }
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainApp.getMainWindow());
            alert.setTitle("Error");
            alert.setHeaderText("Cannot save to file.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



    @FXML
    public void openFromFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open from file");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("Ser Files (*.ser)", "*.ser"));

        File file = fileChooser.showOpenDialog(mainApp.getMainWindow());

        if (file!=null) {
            try {
                String extension = fileChooser.selectedExtensionFilterProperty().get().getExtensions().get(0).substring(1);

                if (extension.equals(".csv")) {
                    List<List<Task>> opened = CSVHandler.rebuildTaskListsFrom(file.getAbsolutePath());
                    mainApp.getToDoTaskList().clear();
                    mainApp.getToDoTaskList().addAll(opened.get(0));
                    mainApp.getInProgTaskList().clear();
                    mainApp.getInProgTaskList().addAll(opened.get(1));
                    mainApp.getDoneTaskList().clear();
                    mainApp.getDoneTaskList().addAll(opened.get(2));
                }
                else if (extension.equals(".ser")) {
                    List<List<Task>> opened = SerFileHandler.rebuildFrom(file.getAbsolutePath());
                    mainApp.getToDoTaskList().clear();
                    mainApp.getToDoTaskList().addAll(opened.get(0));
                    mainApp.getInProgTaskList().clear();
                    mainApp.getInProgTaskList().addAll(opened.get(1));
                    mainApp.getDoneTaskList().clear();
                    mainApp.getDoneTaskList().addAll(opened.get(2));
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getMainWindow());
                alert.setTitle("Error");
                alert.setHeaderText("Cannot open file.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }

    }



}
