package Controller;

import Model.MyFile;
import Model.SortMyFilesException;
import View.UserInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    UserInterface userInterface;
    String rootFolderStr;
    File rootFolder;
    ArrayList<MyFile> allMyFiles;

    ArrayList<MyFile> allMyFilesCopy;
    ArrayList<MyFile> allMyFilesSorted;

    boolean ableToSortAllMyFiles;

    String resultFileStr;
    File resultFile;

    public MainController() {
        userInterface = new UserInterface();
        allMyFiles = new ArrayList<>();
    }

    /*
    * главный метод в котором происходят все основные шаги программы
     */
    public void begin() {
        rootFolder = userInterface.askRootFolder();
        rootFolderStr = rootFolder.getAbsolutePath();
        fillMyFiles();
        fillFilesAboveInAllMyFiles();
        userInterface.printAllMyFiles(allMyFiles);


        sortAllMyFiles();
        if (ableToSortAllMyFiles) {
            userInterface.printAllMyFilesSorted(allMyFilesSorted);
            userInterface.printMyFilesJoined(allMyFilesSorted);
            createResultFile();
        } else {
            userInterface.printNotAbleToSort();
        }
    }

    /**
     * Рекурсивный метод, который находит все файлы в корневой папке и ее подпапках и добавляет их в список myFiles
     */
    private void fillMyFiles() {
        fillMyFiles(this.rootFolder);
    }

    /**
     * Рекурсивный метод, который находит все файлы в папке и ее подпапках и добавляет их в список myFiles
     */
    private void fillMyFiles(File folder) {
        fillMyFilesInFolder(folder);
        var dirs = folder.listFiles();

        for (var dir :
                dirs) {
            if (dir.isDirectory()) {
                fillMyFiles(dir);
            }
        }
    }


    /**
     * Метод, который находит все файлы только в данной папке и добавляет их в список myFiles
     */
    private void fillMyFilesInFolder(File folder) {
        var files = folder.listFiles();
        MyFile myFile;
        for (var file :
                files) {
            try {
                if (file.isFile()) {
                    myFile = MyFile.parseFileToMyFile(file);
                    allMyFiles.add(myFile);
                }
            } catch (IOException ex) {
                userInterface.notifyException(ex, "Exception in fillMyFilesInFolder");
            }
        }
    }

    private void fillFilesAboveInAllMyFiles() {
        for (var myFile :
                allMyFiles) {
            myFile.fillFilesAbove(allMyFiles, rootFolderStr);
        }
    }

    private void sortAllMyFiles() {
        ableToSortAllMyFiles = true;
        allMyFilesCopy = new ArrayList<>(allMyFiles);
        allMyFilesSorted = new ArrayList<>();

        int allMyFilesLength = allMyFiles.size();
        try {
            for (int i = 0; i < allMyFilesLength; i++) {
                addOneToAllMyFilesSorted();
            }
        } catch (SortMyFilesException ex) {
            ableToSortAllMyFiles = false;
        }
    }

    /**
     * Добавляет к AllMyFilesSorted снизу один подходящий myFile из allMyFilesCopy
     * А если не может добавить такой майфайл чтоб AllMyFilesSorted остался отсортированным то кидает исключение
     */
    private void addOneToAllMyFilesSorted() throws SortMyFilesException {
        for (var myFile :
                allMyFilesCopy) {
            if (myFile.isAbleToAddMyFile(allMyFilesSorted)) {
                allMyFilesSorted.add(myFile);
                MyFile.removeMyFile(allMyFilesCopy, myFile);
                return;
            }
        }
        throw new SortMyFilesException();
    }

    private void createResultFile() {
        ableToSortAllMyFiles = true;

        resultFileStr = rootFolderStr + File.separator + "result.txt";
        resultFile = new File(resultFileStr);

        try {
            resultFile.createNewFile();
            writeFileResult();
            userInterface.printResultFileCreate(resultFileStr);
        } catch (IOException e) {
            userInterface.notifyException(e, "Не получилось создать result.txt");
        }
    }

    private void writeFileResult() throws IOException {
        try(FileWriter writer = new FileWriter(resultFile, false))
        {
            for (var myFile:
                 allMyFilesSorted) {
                writer.write(myFile.getText());
                writer.write("\n");
            }

            writer.flush();
        }
        catch(IOException ex){
           throw new IOException("writeFileResult()");
        }
    }
}
