package game;

import java.io.*;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Game {

    /**
     * Запись сохранения игры в бинарный файл
     * @param fileName Имя файла для записи
     * @param game Объект прогресса игры для записи
     * @return true в случае успешной записи, иначе false
     */
    public static boolean saveGame(String fileName, GameProgress game){
        try (FileOutputStream fos = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Неверно указано имя файла сохранения!" + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Произошла ошибка при записи сохранения!" + e.getMessage());
            return false;
        }
    }

    /**
     * Архивация файлов сохранений в один архив
     * @param filePath Имя архива
     * @param files Список абсолютных имен файлов для архивации
     * @return true в случае успешной архивации, иначе false
     */
    public static boolean zipFiles(String filePath, String[] files) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ZipOutputStream zout = new ZipOutputStream(fos)) {
            for (String item : files) {
                try (FileInputStream fis = new FileInputStream(item)) {
                    ZipEntry entry = new ZipEntry(item);
                    zout.putNextEntry(entry);
                    // считываем содержимое файла в массив byte
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    // добавляем содержимое к архиву
                    zout.write(buffer);
                    // закрываем текущую запись для новой записи
                    zout.closeEntry();
                } catch (FileNotFoundException e) {
                    System.out.println("Не найден файл для архивирования!" + e.getMessage());
                    return false;
                }
            }
            return true;
        }catch (FileNotFoundException e) {
            System.out.println("Не найден файл для создания архива!" + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Ошибка при архивировании!" + e.getMessage());
            return false;
        }
    }

    /**
     * Удалении не .zip файлов из указанной папки
     * @param dirName Имя папки,из которой удаляются файлы
     */
    public static void removeNonZippedFiles(String dirName) {
        File dir = new File(dirName);
        if (dir.isDirectory()) {
            // получаем все вложенные объекты в каталоге
            for (File item : dir.listFiles()) {
                // проверим, является ли объект файлом
                if (!item.isDirectory() && !isZipFile(item.getName())) {
                    item.delete();
                }
            }
        }
    }

    /**
     * Проверка является ли имя файла именем zip-архива
     * @param fileName Имя файла
     * @return true если имя файла - это имя zip-архива, иначе false
     */
    private static boolean isZipFile(String fileName) {
        return fileName.substring(fileName.length() - 3, fileName.length())
                .toLowerCase()
                .equals("zip");
    }


    /**
     * Разархивирование файла с сохранениями игры
     * @param fileName Имя архива
     * @param dirName Имя папки, куда помещаются разархивированные файлы
     * @return true в случае успешной разархивации файлов, иначе false
     */
    public static boolean openZip(String fileName, String dirName) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(fileName))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                    name = entry.getName();

                try (FileOutputStream fout = new FileOutputStream(name)) {
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }
                    fout.flush();
                    zin.closeEntry();
                } catch (FileNotFoundException e) {
                    System.out.println("Ошибка считывания файла из архива!" + e.getMessage());
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Файл архива не удалось открыть!" + ex.getMessage());
            return false;
        }
    }

    /**
     * Получение объекта состояния игры из бинарного файла
     * @param fileName Имя файла
     * @return true в случае успешного восстановления состояния игры, иначе false
     */
    public static GameProgress openProgress(String fileName){
        GameProgress game = null;
        try(FileInputStream fis = new FileInputStream(fileName);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            game = (GameProgress) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Не верно задан файл с сохранением!" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла с сохранением!" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Содержимое файла сохранения не соответствует объекту игры!" + e.getMessage());
        }
        return game;
    }
}
