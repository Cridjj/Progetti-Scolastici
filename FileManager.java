import java.io.*;
import java.util.*;

public class FileManager {
    public File workingdir;
    private File file;
    private String fileName;

    public FileManager(String fileName) {
        this.fileName = fileName;
        workingdir = new File("Partite/");
    }

    public void createFile() {
        String pathName = workingdir.getName() + "/" + getSavingTime() + "_" + fileName + ".txt";
        file = new File(pathName);
    }

    public boolean saveGame(Player player, Vector<Enemy> enemies) {
        if (getFilesAmount() == 10) {
            File[] files = workingdir.listFiles();
            System.out.println(
                    "E' stato eliminato il file '" + ConsoleColors.WHITE_BOLD + files[0].getName() + ConsoleColors.WHITE
                            + "' dalla cartella, verrà sostituito dal salvataggio corrente.");
            files[0].delete();
        }

        createFile();

        try (ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream(file, false))) {
            write.writeObject(player);
            write.writeObject(enemies);
            return true;
        } catch (NotSerializableException nse) {
            return false;
        } catch (IOException eio) {
            return false;
        }
    }

    public void printPath() {
        System.out.println(file.getAbsolutePath());
    }

    public String getSavingTime() {
        Calendar date = Calendar.getInstance();
        return String.format("%02d", date.get(Calendar.DAY_OF_MONTH)) + "-"
                + String.format("%02d", date.get(Calendar.MONTH) + 1) + "-"
                + date.get(Calendar.YEAR) + "_" + String.format("%02d", date.get(Calendar.HOUR_OF_DAY)) + "-"
                + String.format("%02d", date.get(Calendar.MINUTE)) + "-"
                + String.format("%02d", date.get(Calendar.SECOND));
    }

    public Object[] readData(String fileName) {
        Object[] returnValue = new Object[2];
        String pathName = workingdir.getName() + "/" + fileName;

        try (ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(pathName))) {
            returnValue[0] = inFile.readObject();
            returnValue[1] = inFile.readObject();
            inFile.close();
            return returnValue;
        } catch (ClassNotFoundException cnfe) {
            return null;
        } catch (FileNotFoundException fnfe) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public String selectSaveFile() {
        int choice;
        Scanner input = new Scanner(System.in);
        String[] fileNames;

        fileNames = workingdir.list();

        for (int i = 0; i < fileNames.length; i++) {
            System.out.println((i + 1) + ". " + fileNames[i]);
        }

        System.out.print(">> Seleziona il file da caricare (1-" + fileNames.length + ", 0 per uscire): ");
        do {
            choice = input.nextInt();
            if (choice == 0)
                return "$exit";
            if (choice < 1 || choice > fileNames.length)
                System.out.print(">> Valore non valido. Riprova: ");
        } while (choice < 1 || choice > fileNames.length);
        choice--;

        return fileNames[choice];
    }

    public boolean isEmpty() {
        String[] nameList = workingdir.list();
        return nameList.length == 0;
    }

    public int getFilesAmount() {
        return workingdir.list().length;
    }

}
