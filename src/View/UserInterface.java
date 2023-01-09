package View;

import Model.MyFile;

import java.util.List;
import java.util.Scanner;
import java.io.File;

public class UserInterface {
    final Scanner in = new Scanner(System.in);

    public File askRootFolder() {
        String rootFolderStr;
        File rootFolder;

        while (true) {
            System.out.println("Введите путь до корневой папки: ");
            rootFolderStr = in.next();
            rootFolder = new File(rootFolderStr);
            if (rootFolder.isDirectory()) {
                break;
            } else {
                System.out.println("Данного католога не существует, попробуйте еще раз.");
            }
        }
        return rootFolder;
    }

    public void notifyException(Exception ex, String message) {
        System.out.println("Произошла ошибочка: " + ex + " " + message);
    }


    public void printAllMyFiles(List<MyFile> allMyFiles) {
        System.out.println("Список файлов: ");
        printMyFiles(allMyFiles);
    }

    public void printAllMyFilesSorted(List<MyFile> allMyFilesSorted) {
        System.out.println("Отсортированный список файлов: ");
        printMyFiles(allMyFilesSorted);
    }

    private void printMyFiles(List<MyFile> myFiles){
        int lenAllMyFile = myFiles.size();
        for (int i = 0; i < lenAllMyFile; i++) {
            System.out.println(i + 1 + ". " + myFiles.get(i).getPath());
            //myFiles.get(i).printMyFile();
        }
        System.out.println();
    }


    public void printMyFilesJoined(List<MyFile> allMyFiles) {
        System.out.println("Конкатенация файлов: ");
        for (MyFile myFile : allMyFiles) {
            System.out.println(myFile.getText());
        }
        System.out.println();
    }


    public void printNotAbleToSort() {
        System.out.println("Отсортированный список построить невозможно (существует циклическая зависимость)");
    }

    public void printResultFileCreate(String resultFileStr){
        System.out.println("Результат конкатенации файлов помещен в " + resultFileStr);
    }

}
