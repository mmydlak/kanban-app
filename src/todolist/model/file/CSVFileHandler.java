package todolist.model.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFileHandler {

    static final private String cvsSplitBy = ",";

    public static List<String[]> readLineByLineFrom(String path) throws Exception {

          BufferedReader bufReader = new BufferedReader(new FileReader(path));
          String line;
          List<String[]> list = new ArrayList<>();
          while ((line = bufReader.readLine()) != null) {
              String[] data = parseLine(line+"\n");
              if(data!=null) {
                  list.add(data);
              }
          }
          bufReader.close();
          return list;
    }


    public static String[] readFrom(String path) throws Exception {

        StringBuilder text = new StringBuilder();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNext()) {
            text.append(scanner.nextLine());
            text.append("\n");
        }
        scanner.close();
        return parseLine(text.toString());
    }


    private static String[] parseLine(String csvLine/*, char separators, char customQuote*/) {

        List<String> result = new ArrayList<>();

        if (csvLine == null || csvLine.isEmpty()) {
            return null;
        }
//        if (customQuote == ' ') {
//            customQuote = DEFAULT_QUOTE;
//        }
//
//        if (separators == ' ') {
//            separators = DEFAULT_SEPARATOR;
//        }

        StringBuffer curVal = new StringBuffer();
        char[] chars = csvLine.toCharArray();

        boolean inQuotes = false;
        boolean doubleQuotes = false;
        boolean newColumn = true;

        for (int i = 0; i < chars.length; i++) {
            // jesli rozpoczeto czytanie nowej kolumny i zaczyna sie od znaku ",
            // to nie zapisujemy tylko inforujemy ze kolumna jest w cudzyslowach
            // w przeciwnym wypadku zapisujemy znak
            // po wszystkim ustawiamy zmienna newColumn na false
            if (newColumn) {
                newColumn = false;
                if (chars[i] == '"') {
                    inQuotes = true;
                    doubleQuotes = true;
                }
                else if (chars[i] == '\n' || chars[i] == ',') {
                    result.add("");
                    newColumn = true;
                }
                else {
                    curVal.append(chars[i]);
                }
            }
            // jesli nie rozpoczeto czytania nowej kolumny
            // tylko kontynuujemy
            else {
                //jesli kolumna jest w  cudzyslowach
                if (inQuotes) {
                    // jesli odczytany znak to " i poprzedni to też "
                    // zapisujemy jeden " i ustawiawiamy doubleQuotes na true
                    // na wypadek gdyby po cudzslowie wystepowal , zeby tez go zapisac
                    // jesli natomiast " nie byl poprzedzony "
                    // doubleQuotes ustawiamy na false i nie zapisujemy cudzyslowu
                    // zeby , zinterpetowano jako koniec kolumny
                    if (chars[i] == '"') {
                        if(chars[i-1]=='"') {
                            // zzabezpieczenie by nie zapisywalo zbednych " gdy nastepuja po sobie a ich liczba jest nieparzysta
                            if(doubleQuotes == false) {
                                doubleQuotes = true;
                                curVal.append(chars[i]);
                            }
                            else {
                                doubleQuotes = false;
                            }
                        }
                        else {
                            doubleQuotes = false;
                        }
                    }
                    // jesli przed przecinkiem wystepowal podwojny cudzyslow
                    // dodajemy przecinek
                    // jesli nie - intepretujemy jako koniec linii
                    else if ((chars[i]==',' || chars[i]=='\n') && chars[i - 1] == '"') {
                        if (doubleQuotes) {
                            curVal.append(chars[i]);
                        } else {
                            newColumn = true;
                            inQuotes = false;
                            result.add(curVal.toString());
                            curVal.delete(0,curVal.length());
                        }
                    }
                    // jesli odczytany znak nie jest cudzyslowem ani przecinkiem przed ktorym stoi cudzyslow
                    // ale (jak mowi pierwszy warunek) kolumna jest w cudzyslowach
                    // zapisujemy odczytany znak
                    else if (chars[i] != '"') {
                        curVal.append(chars[i]);
                    }

                }
                //jesli kolumna NIE jest w cudzyslowach
                else {

                    if (chars[i] == ',' || chars[i] == '\n') {
                        newColumn = true;
                        result.add(curVal.toString());
                        curVal.delete(0,curVal.length());
                    } else {
                        curVal.append(chars[i]);
                    }
                }
            }
        }

        return result.toArray(new String[0]);
    }



    public static void saveTo(String path, List<String> list) throws Exception{

        BufferedWriter bufWriter = new BufferedWriter(new FileWriter(path));
        for(String csvLine : list) {
            bufWriter.write(csvLine);
            bufWriter.newLine();
        }
        bufWriter.flush();
        bufWriter.close();
    }



}
