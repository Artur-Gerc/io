import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        StringBuilder log = new StringBuilder();
        String gamePath = "C:/Games/";

        createNewDirectory(gamePath,
                List.of("src", "res", "savegames", "temp"), log);

        createNewDirectory(gamePath + "src/",
                List.of("main", "test"), log);

        createNewDirectory(gamePath + "res/",
                List.of("drawables", "vectors", "icons"), log);

        createNewFile(gamePath + "src/main/", List.of("Main.java", "Utils.java"), log);
        createNewFile(gamePath + "temp/", List.of("temp.txt"), log);

        writeLog(log);
        System.out.println(log);
    }

    private static void createNewFile(String path, List<String> name, StringBuilder log) {
        for (String nameOfFiles : name) {
            File newFile = new File(path + nameOfFiles);
            try {
                if (newFile.createNewFile()) {
                    log.append("#INFO" + " [" + LocalDateTime.now() + "] " +
                            "file " + newFile.getPath() + " is successfully created\n");
                } else {
                    log.append("#ERROR" + " [" + LocalDateTime.now() + "] " +
                            "file " + newFile.getPath() + " not created\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void createNewDirectory(String gamePath, List<String> nameOfDirectory, StringBuilder log) {
        for (String name : nameOfDirectory) {
            File newDir = new File(gamePath + name);
            if (newDir.mkdir()) {
                log.append("#INFO" + " [" + LocalDateTime.now() + "] " +
                        "directory " + newDir.getPath() +
                        " is successfully created\n");
            } else {
                log.append("#ERROR" + " [" + LocalDateTime.now() + "] " +
                        "directory " + newDir.getPath() +
                        " not created\n");
            }
        }
    }

    public static void writeLog(StringBuilder sb) {
        try (BufferedWriter bufferedWriter = new BufferedWriter
                (new FileWriter("C:/Games/temp/temp.txt"))) {
            String resulLog = sb.toString();
            bufferedWriter.write(resulLog);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

