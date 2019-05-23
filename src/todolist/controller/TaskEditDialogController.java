package todolist.controller;

import todolist.Main;
import todolist.model.task.Task;
import todolist.model.task.TaskPriority;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TaskEditDialogController {

    @FXML
    private TextField titleTextField;
    @FXML
    private ComboBox<TaskPriority> priorityBox;
    @FXML
    private DatePicker expDatePicker;
    @FXML
    private TextArea descTextArea;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;
   // private boolean saveClicked = false;
    private Task task;
    private Main mainApp;

    @FXML
    private void initialize() {
        priorityBox.setItems(FXCollections.observableArrayList(TaskPriority.LOW, TaskPriority.MEDIUM, TaskPriority.HIGH));

    }

   // public boolean isSaveClicked() {        return saveClicked;    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            task.setTitle(titleTextField.getText());
            task.setPriority((TaskPriority) priorityBox.getValue());
            task.setExpDate(expDatePicker.getValue());
            task.setDescription(descTextArea.getText());

            if(!(mainApp.getToDoTaskList().contains(task) || mainApp.getInProgTaskList().contains(task) || mainApp.getDoneTaskList().contains(task))) {
                mainApp.addTaskToArray(task);
            }
            dialogStage.close();
        }
    }

    public void setMainApp(Main mainApp){
        this.mainApp = mainApp;
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleTextField.getText().length() == 0) {
            errorMessage += "No valid title!\n";
        }
        if (expDatePicker.getValue() == null) {
            errorMessage += "No valid date!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid data");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    public void setTaskData(Task task) {
        this.task = task;
        titleTextField.setText(task.getTitle());
        priorityBox.getSelectionModel().select(task.getPriority());
        if(task.getExpDate()!=null) expDatePicker.setValue(task.getExpDate());
        descTextArea.setText(task.getDescription());
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


}
