import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SaveGameProgress {
    public static void main(String[] args) {
        String savePath = "C:/Games/savegames/";
        GameProgress newGame1 = new GameProgress(100, 20, 4, 29.1);
        GameProgress newGame2 = new GameProgress(81, 45, 7, 51.4);
        GameProgress newGame3 = new GameProgress(68, 90, 9, 72.9);

        saveGame(savePath, List.of(newGame1, newGame2, newGame3));

        zipFiles(savePath);
        openZip(savePath);

        String pathToSavedGame = "C:/Games/savegames/unzip-save1.dat";
        System.out.println(openProgress(pathToSavedGame));
    }

    public static void saveGame(String savePath, List<GameProgress> game) {
        int saveCount = 1;
        for (GameProgress gameList : game) {
            try (BufferedOutputStream bus = new BufferedOutputStream(new FileOutputStream(savePath + "save" + saveCount + ".dat"));
                 ObjectOutputStream ous = new ObjectOutputStream(bus)) {
                ous.writeObject(gameList);
                saveCount++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void zipFiles(String savePath) {
        File files = new File(savePath);

        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(savePath + "zip.zip")))) {
            for (File f : files.listFiles()) {
                if (f.getName().contains("save")) {
                    try (FileInputStream fis = new FileInputStream(f)) {
                        ZipEntry zipEntry = new ZipEntry(f.getName());
                        zos.putNextEntry(zipEntry);
                        byte[] buffer = new byte[fis.available()];
                        fis.read(buffer);
                        zos.write(buffer);
                        zos.closeEntry();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void openZip(String savePath) {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(savePath + "zip.zip"))) {
            ZipEntry zipEntry;
            String name;

            while ((zipEntry = zis.getNextEntry()) != null) {
                name = zipEntry.getName();

                try (FileOutputStream fis = new FileOutputStream(savePath + "unzip-" + name);
                     BufferedOutputStream bos = new BufferedOutputStream(fis)) {

                    byte[] buffer = new byte[1024];
                    while (zis.read(buffer) != -1) {
                        bos.write(buffer);
                    }
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameProgress openProgress(String pathToSavedGame){
        try (FileInputStream fis = new FileInputStream(pathToSavedGame);
        ObjectInputStream ois = new ObjectInputStream(fis)){
            GameProgress gameProgress = (GameProgress) ois.readObject();
            return gameProgress;
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
}
