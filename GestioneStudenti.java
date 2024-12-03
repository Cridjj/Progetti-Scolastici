import java.io.Serializable;
import java.util.Vector;

public class GestioneStudenti implements Serializable{
    private int studentiCounter;
    private Vector<Studente> studenti;

    public GestioneStudenti(){
        studentiCounter = 1;
        studenti = new Vector<Studente>();
    }

    public void setStudenti(Vector<Studente> studenti){
        this.studenti = studenti;
    }

    public void addStudente(String cognome, String nome, String classe){
        studenti.add(new Studente(studentiCounter, cognome, nome, classe));
        studentiCounter++;
    }

    public void removeStudente(int stuIndex){
        studenti.removeElementAt(stuIndex);
    }

    public int getIndexFromCode(int codStu){
        for(int i=0; i<studenti.size(); i++){
            if(codStu==studenti.elementAt(i).getCode())
                return i;
        }
        return -1;
    }

    public boolean isEmpty(){
        return studenti==null || studenti.size()==0;
    }

    public int getCounter(){
        return studentiCounter;
    }

    public Vector<Studente> getStudenti(){
        return studenti;
    }

    public boolean codeExists(int codStu){
        for(int i=0; i<studenti.size(); i++){
            if(codStu==studenti.elementAt(i).getCode())
                return true;
        }
        return false;
    }

    @Override
    public String toString(){
        String retValue = "";

        for(int i=0; i<studenti.size(); i++){
            retValue += studenti.elementAt(i).getCode() + "; " + studenti.elementAt(i).getSurname() + "; ";
            retValue += studenti.elementAt(i).getName() + "; " + studenti.elementAt(i).getClasse() + "\n";
        }

        return retValue;
    }

    public void setName(String nome, int index){
        studenti.elementAt(index).setName(nome);
    }

    public void setSurname(String cognome, int index){
        studenti.elementAt(index).setSurname(cognome);
    }

    public void setClasse(String classe, int index){
        studenti.elementAt(index).setClasse(classe);
    }

    public String searchData(String value){
        String retValue = "";

        for(int i=0; i<studenti.size(); i++){
            if(value.equals(studenti.elementAt(i).getCode()+"") || value.equals(studenti.elementAt(i).getName()) || value.equals(studenti.elementAt(i).getSurname()) || value.equals(studenti.elementAt(i).getClasse())){
                retValue += studenti.elementAt(i).getCode() + "; " + studenti.elementAt(i).getSurname() + "; ";
                retValue += studenti.elementAt(i).getName() + "; " + studenti.elementAt(i).getClasse() + "\n";
            }
        }

        return retValue;
    }
}
