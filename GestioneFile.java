import java.io.*;

public class GestioneFile<E> implements Serializable{
    private String nomeFile;
    private File file;

    public GestioneFile(String nomeFile){
        this.nomeFile = "src/Files/" + nomeFile;
    }

    public void creaFile(){
        file = new File(nomeFile);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            System.out.println(ConsoleColors.RED_BRIGHT + "Errore durante la creazione del file." + ConsoleColors.RESET);
        }
    }

    public boolean salvaFile(E obj) {
        creaFile();

        try (ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream(file, false))) {
            write.writeObject(obj);
            return true;
        } catch (NotSerializableException nse) {
            return false;
        } catch (IOException eio) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public E caricaDati() {
        Object returnValue;

        try (ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(nomeFile))) {
            returnValue = inFile.readObject();
            inFile.close();
            return (E)returnValue;
        } catch (ClassNotFoundException cnfe) {
            return null;
        } catch (FileNotFoundException fnfe) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

}
