package todolist;
//ctrl+shift+f8 - usuwa breakpointy

import todolist.controller.Controller;
import todolist.controller.TaskEditDialogController;
import todolist.model.task.Task;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage mainWindow;
    private static AnchorPane rootLayout;
    private ObservableList<Task> toDoTaskList = FXCollections.observableArrayList();
    private ObservableList<Task> inProgTaskList = FXCollections.observableArrayList();
    private ObservableList<Task> doneTaskList = FXCollections.observableArrayList();


    public Main() { }

    @Override
    public void start(Stage primaryStage) {
        mainWindow = primaryStage;
        primaryStage.setTitle("Kanban");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = fxmlLoader.load();

            Controller controller = fxmlLoader.getController();
            controller.setMainApp(this);

            mainWindow.setScene(new Scene(rootLayout, 700, 500));
            mainWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }



    public Stage getMainWindow() {
        return mainWindow;
    }

    public ObservableList<Task> getToDoTaskList() {
        return toDoTaskList;
    }

    public ObservableList<Task> getInProgTaskList() {
        return inProgTaskList;
    }

    public ObservableList<Task> getDoneTaskList() {
        return doneTaskList;
    }

    public void addTaskToArray(Task task) {
        toDoTaskList.add(task);
    }

    public void showTaskEditDialog(Task task) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("view/TaskEditDialog.fxml"));
            AnchorPane layout = fxmlLoader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainWindow);
            Scene scene = new Scene(layout, 400, 300);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            TaskEditDialogController controller = fxmlLoader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setTaskData(task);

            dialogStage.showAndWait();

          //  return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
