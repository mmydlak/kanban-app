package todolist.model.file;

import todolist.model.task.Task;
import todolist.model.task.TaskPriority;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {

    public static String getCSV(List<Task> list) {
        StringBuilder result = new StringBuilder(list.size() + ",");
        for(Task t : list) {
            result.append(getCSV(t));
            result.append(",");
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }

    @SuppressWarnings("ALL")
    public static String getCSV(Task t) {

        StringBuffer stringToFormat = new StringBuffer("");
        String result = "";
        String tmp = "";

        stringToFormat = new StringBuffer(t.getTitle());
        if (t.getTitle().contains(",") || t.getTitle().contains("\"") || t.getTitle().contains("\n")) {
            for (int i=0; i<stringToFormat.length(); i++) {
                if(stringToFormat.charAt(i) == '"') {
                    stringToFormat.insert(i,'"');
                    i++;
                }
            }
            stringToFormat.insert(0,'"');
            stringToFormat.append('"');
        }
        result += (stringToFormat.toString() + ",");

        result += (t.getPriority().toString() + ",");
        result += (t.getExpDate().toString() + ",");

        stringToFormat = new StringBuffer(t.getDescription());
        if (t.getDescription().contains(",") || t.getDescription().contains("\"") || t.getDescription().contains("\n")) {
            for (int i=0; i<stringToFormat.length(); i++) {
                if(stringToFormat.charAt(i) == '"') {
                    stringToFormat.insert(i,'"');
                    i++;
                }
            }
            stringToFormat.insert(0,'"');
            stringToFormat.append('"');
        }
        result += stringToFormat.toString();

        return result;
    }

    public static List<Task> rebuildTasksFrom(String path) throws Exception{

        List<Task> result = new ArrayList<>();
        String[] data = CSVFileHandler.readFrom(path);
        if(data!=null && data.length>0 && data.length%4==0) {
            for (int i=0; i<data.length-3; i+=4) {
                //try {
                result.add(new Task(data[i], data[i+3], TaskPriority.valueOf(data[i+1]), new SimpleDateFormat("yyyy-MM-dd").parse(data[i+2])));
                //}
                //catch (Exception e) {
                //    throw e;
                //}
            }
        }
        return result;
    }

    public static List<List<Task>> rebuildTaskListsFrom(String path) throws Exception{

        List<List<Task>> result = new ArrayList<>();
        String[] data = CSVFileHandler.readFrom(path);

        if(data!=null && data.length>0) {

            int jump = 1;
            int howManyInList = 0;
            int iteration = -1;
            for(int j=0; j<data.length; j+=jump) {

                iteration++;
                howManyInList= Integer.parseInt(data[j]);
                jump = 1+howManyInList*4;
                result.add(new ArrayList<Task>());

                for (int i=j+1; i<j+jump; i+=4) {
                    result.get(iteration).add(new Task(data[i], data[i+3], TaskPriority.valueOf(data[i+1]), new SimpleDateFormat("yyyy-MM-dd").parse(data[i+2])));
                }

            }

        }
        return result;
    }

}
