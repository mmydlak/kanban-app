package todolist.model.file;

import todolist.model.task.Task;

import java.io.*;
import java.util.List;

public class SerFileHandler {

    public static void saveTo(String path, List<List<Task>> list) throws Exception{
        FileOutputStream fOutStm = new FileOutputStream(path);
        ObjectOutputStream objOutStream = new ObjectOutputStream(fOutStm);
        objOutStream.writeObject(list);

        fOutStm.flush();
        fOutStm.close();
        objOutStream.flush();
        objOutStream.close();
    }

    public static List<List<Task>> rebuildFrom(String path) throws Exception{
        FileInputStream fInStm = new FileInputStream(path);
        ObjectInputStream objInStream = new ObjectInputStream(fInStm);
        List<List<Task>> result = (List<List<Task>>) objInStream.readObject();

        fInStm.close();
        objInStream.close();

        return result;
    }


}
