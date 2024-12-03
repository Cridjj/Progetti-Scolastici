import java.io.Serializable;

public class Docente implements Serializable{
    private int codDoc;
    private String cognome;
    private String nome;
    private String materia;

    public Docente(int codDoc, String cognome, String nome, String materia){
        this.codDoc = codDoc;
        this.cognome = cognome;
        this.nome = nome;
        this.materia = materia;
    }

    public String getSurname(){
        return cognome;
    }

    public String getName(){
        return nome;
    }

    public String getSubject(){
        return materia;
    }

    public int getCode(){
        return codDoc;
    }

    public void setName(String nome){
        this.nome = nome;
    }

    public void setSurname(String cognome){
        this.cognome = cognome;
    }

    public void setSubject(String materia){
        this.materia = materia;
    }
}
