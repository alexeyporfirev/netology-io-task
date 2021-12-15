import game.Game;
import game.GameProgress;
import installation.Installer;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static String logFileName = "temp.txt";

    private static String[] gamesDirectory = {"src",
        "res",
        "savegames",
        "temp"
    };

    private static String[] srcDirectory = {"src\\main",
        "src\\test"
    };

    private static String[] resDirectory = {"res\\drawables",
            "res\\vectors",
            "res\\icons"
    };

    private static String[] mainFiles = {"Main.java",
        "Utils.java"
    };



    public static void main(String[] args){
        System.out.println("Введите название директории для создания папок и файлов:");
        //Формат ввода F:\\Games
        String directory = scanner.nextLine();

        Installer inst = new Installer(directory, logFileName);

        //Создаем папки в Games
        for(String item: gamesDirectory) {
            inst.createDirectory(item);
        }

        //Создаем папки в src
        for(String item: srcDirectory) {
            inst.createDirectory(item);
        }

        //Создаем папки в res
        for(String item: resDirectory) {
            inst.createDirectory(item);
        }

        //Создаем файлы в main
        for(String item: mainFiles) {
            try {
                inst.createFile(directory + "\\src\\main", item);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        // Файл temp.txt создается перед записью логов в файл
        try {
            inst.writeLog();
        } catch (IOException e) {
            System.out.println("Не удалось создать файл логов!" + e.getMessage());
        }


        // Создаем три сохранения
        GameProgress game1 = new GameProgress(10, 10, 10, 100.);
        GameProgress game2 = new GameProgress(20, 20, 20, 200.);
        GameProgress game3 = new GameProgress(30, 30, 30, 300.);

        //Массив с названиями сохранений
        String[] savesNames = {directory + "\\savegames\\save1.dat",
                directory + "\\savegames\\save2.dat",
                directory + "\\savegames\\save3.dat"};

        // Создаем файлы сохранений
        if (Game.saveGame(savesNames[0], game1)) {
            System.out.println("Сохранение " + game1 + " успешно записано в файл!");
        } else {
            System.out.println("Сохранение " + game1 + " не удалось записать в файл!");
        }

        if (Game.saveGame(savesNames[1], game2)) {
            System.out.println("Сохранение " + game2 + " успешно записано в файл!");
        } else {
            System.out.println("Сохранение " + game2 + " не удалось записать в файл!");
        }

        if (Game.saveGame(savesNames[2], game3)) {
            System.out.println("Сохранение " + game2 + " успешно записано в файл!");
        } else {
            System.out.println("Сохранение " + game2 + " не удалось записать в файл!");
        }

        // Помещаем файлы сохранений в архив
        if (Game.zipFiles(directory + "\\savegames\\zip.zip", savesNames)) {
            System.out.println("Файлы успешно заархивированы!");
        } else {
            System.out.println("Произошла ошибка архивации файлов!");
        }

        // Удаляем все не .zip файлы
        Game.removeNonZippedFiles(directory + "\\savegames");

        // Разархивируем архив с сохранениями
        if (Game.openZip(directory + "\\savegames\\zip.zip", directory + "\\savegames")) {
            System.out.println("Файл " + directory + "\\savegames\\zip.zip" + " успешно разархивирован!");
        } else {
            System.out.println("Не удалось разархивировать файл " + directory + "\\savegames\\zip.zip");
        }

        // восстанавливаем состояние прогресса игры из указанного файла
        GameProgress gameRestored = Game.openProgress(savesNames[2]);
        // смотрим, как объект был восстановлен
        System.out.println(gameRestored);

    }
}
