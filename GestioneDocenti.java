import java.io.Serializable;
import java.util.Vector;

public class GestioneDocenti implements Serializable{
    private int docentiCounter;
    private Vector<Docente> docenti;

    public GestioneDocenti(){
        docentiCounter = 1;
        docenti = new Vector<Docente>();
    }

    public void setDocenti(Vector<Docente> docenti){
        this.docenti = docenti;
    }

    public void addDocente(String cognome, String nome, String materia){
        docenti.add(new Docente(docentiCounter, cognome, nome, materia));
        docentiCounter++;
    }

    public void removeDocente(int docIndex){
        docenti.removeElementAt(docIndex);
    }

    public int getIndexFromCode(int codDoc){
        for(int i=0; i<docenti.size(); i++){
            if(codDoc==docenti.elementAt(i).getCode())
                return i;
        }
        return -1;
    }

    public boolean isEmpty(){
        return docenti==null || docenti.size()==0;
    }

    public int getCounter(){
        return docentiCounter;
    }

    public void setName(String nome, int index){
        docenti.elementAt(index).setName(nome);
    }

    public void setSurname(String cognome, int index){
        docenti.elementAt(index).setSurname(cognome);
    }

    public void setSubject(String materia, int index){
        docenti.elementAt(index).setSubject(materia);
    }

    public Vector<Docente> getDocenti(){
        return docenti;
    }

    @Override
    public String toString(){
        String retValue = "";

        for(int i=0; i<docenti.size(); i++){
            retValue += docenti.elementAt(i).getCode() + "; " + docenti.elementAt(i).getSurname() + "; ";
            retValue += docenti.elementAt(i).getName() + "; " + docenti.elementAt(i).getSubject() + "\n";
        }

        return retValue;
    }

    public boolean codeExists(int codDoc){
        for(int i=0; i<docenti.size(); i++){
            if(codDoc==docenti.elementAt(i).getCode())
                return true;
        }
        return false;
    }

    public String searchData(String value){
        String retValue = "";

        for(int i=0; i<docenti.size(); i++){
            if(value.equals(docenti.elementAt(i).getCode()+"") || value.equals(docenti.elementAt(i).getName()) || value.equals(docenti.elementAt(i).getSurname()) || value.equals(docenti.elementAt(i).getSubject())){
                retValue += docenti.elementAt(i).getCode() + "; " + docenti.elementAt(i).getSurname() + "; ";
                retValue += docenti.elementAt(i).getName() + "; " + docenti.elementAt(i).getSubject() + "\n";
            }
        }

        return retValue;
    }
}
