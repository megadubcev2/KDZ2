package Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.readString;

import java.nio.file.Path;
import java.util.Objects;

public class MyFile {

    public static MyFile parseFileToMyFile(File file) throws IOException {
        String path = file.getAbsolutePath();
        String text = readString(Path.of(path));

        return new MyFile(path, text);
    }

    /**
     * находит майфайл по его пути среди списка
     * возвращает null если такого файла нет
     */
    private static MyFile findMyFile(List<MyFile> myFiles, String path) {
        for (var myFile :
                myFiles) {
            //System.out.println(path + " ? " + myFile.path);
            if (myFile.path.equals(path)) {
                return myFile;
            }
        }
        return null;
    }

    public static void removeMyFile(List<MyFile> myFiles, MyFile myFile) {
        int myFilesLen = myFiles.size();
        for (int i = 0; i < myFilesLen; i++) {
            if (myFiles.get(i).myEquals(myFile)) {
                myFiles.remove(i);
                break;
            }
        }
    }


    private String path;
    private String text;

    // не абсолютные пути  без .txt и не факт что вообще такие файлы существуют
    private ArrayList<String> pathsFilesAbove;

    // файлы которые точно существуют
    private ArrayList<MyFile> filesAbove;

    public MyFile() {
        pathsFilesAbove = new ArrayList<>();
        filesAbove = new ArrayList<>();
    }


    private MyFile(String path, String text) {
        pathsFilesAbove = new ArrayList<>();
        filesAbove = new ArrayList<>();
        this.path = path;
        this.text = text;
        String[] lines = text.split("\n");

        for (String line :
                lines) {
            String[] words = line.split(" ‘|’");


            if (words.length >= 2) {
                if (words[0].equals("require")) {
                    pathsFilesAbove.add(words[1]);
                }
            }
        }
    }

    public boolean myEquals(MyFile otherMyFile) {
        return this.path.equals(otherMyFile.path);
    }

    private boolean isMyFileInArray(ArrayList<MyFile> myFiles) {
        for (var myfile :
                myFiles) {
            if (this.myEquals(myfile)) {
                return true;
            }
        }
        return false;
    }


    public void fillFilesAbove(List<MyFile> allMyFiles, String rootFolderStr) {
        MyFile myFile;
        for (var pathFileAbove :
                pathsFilesAbove) {
            myFile = findMyFile(allMyFiles, rootFolderStr + File.separator + pathFileAbove + ".txt");
            if (myFile != null) {
                filesAbove.add(myFile);
            }
        }
    }

    public boolean isAbleToAddMyFile(ArrayList<MyFile> allMyFilesSorted) {
        for (var fileAbove :
                filesAbove) {
            if (!fileAbove.isMyFileInArray(allMyFilesSorted)) {
                return false;
            }
        }
        return true;
    }


    public String getPath() {
        return path;
    }

    public String getText() {
        return text;
    }
/*
    public void printMyFile() {
        System.out.println(getPath() + ":");
        for (var fileAbove :
                filesAbove) {
            System.out.println(fileAbove.getPath());
        }
    }
*/

}