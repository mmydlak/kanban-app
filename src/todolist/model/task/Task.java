package todolist.model.task;

import javafx.scene.input.DataFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private TaskPriority priority;
    private LocalDate expDate;
    public static DataFormat dragAndDropFormat =  new DataFormat("task");

    public Task(String title, String description, TaskPriority priority, Date expDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.expDate = (expDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public Task() {
        this.title = "";
        this.description = "";
        this.priority = TaskPriority.LOW;
        this.expDate = (new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public LocalDate getExpDate() {
        return expDate;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public void setPriority(TaskPriority priority) {
        this.priority=priority;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate=expDate;
    }


    @Override
    public String toString() {
        return title;
    }
}
