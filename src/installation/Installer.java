package installation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Installer {

    private String installationDirectory;
    private String logFileName;
    private StringBuilder logs;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");

    public Installer(String installationDirectory, String logFileName) {
        this.installationDirectory = installationDirectory;
        this.logFileName = logFileName;
        logs = new StringBuilder("");
    }

    public String getInstallationDirectory() {
        return installationDirectory;
    }

    public void setInstallationDirectory(String installationDirectory) {
        this.installationDirectory = installationDirectory;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    /**
     * Создание в папке установки новой директории по имени
     * @param dirName Относительное имя директории
     * @return true в случае успешного создания папки, иначе false
     */
    public boolean createDirectory(String dirName) {
        log("Создание директории \"" + installationDirectory + "\\" + dirName + "\"");
        File file = new File(installationDirectory + "\\" + dirName);
        if (file.mkdir()) {
            log("Директория \"" + installationDirectory + "\\" + dirName + "\"" + " успешно создана.");
            return true;
        } else {
            log("Не удалось создать директорию \"" + installationDirectory + "\\" + dirName + "\".");
            return false;
        }
    }

    /**
     * Создание в папке нового файла по имени
     * @param dirPath Имя папки
     * @param fileName имя файла
     * @return true в случае успешного создания файла, иначе false
     * @throws IOException в случае ошибки при создании файла
     */
    public boolean createFile(String dirPath, String fileName) throws IOException {
        log("Создание файла \"" + dirPath + "\\" + fileName + "\"");
        File file = new File(dirPath, fileName);
        if (file.createNewFile()) {
            log("Файл \"" + dirPath + "\\" + fileName + "\"" + " успешно создан.");
            return true;
        } else {
            log("Не удалось создать файл \"" + dirPath + "\\" + fileName + "\".");
            return false;
        }
    }

    /**
     * Добавление нового сообщения в строку логов
     * @param message Добавляемое сообщение
     */
    private void log(String message) {
        logs.append(dateFormat.format(new Date()))
                .append(": ")
                .append(message)
                .append("\n");
    }

    /**
     * Создание файла логов и запись в него текущих логов
     * @throws IOException в случае ошибки при создании файла или записи в него информации
     */
    public void writeLog() throws IOException {
        createFile(installationDirectory + "\\temp", logFileName);

        try(FileWriter fw = new FileWriter(installationDirectory + "\\temp\\" + this.logFileName, true);
            BufferedWriter bw = new BufferedWriter(fw))
        {
            bw.write(logs.toString());
            bw.flush();
        } catch (IOException e) {
            System.out.println("Не удалось записать логи в файл!" + e.getMessage());
        }

    }
}
