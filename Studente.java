import java.io.Serializable;

public class Studente implements Serializable{
    private int codStu;
    private String cognome;
    private String nome;
    private String classe;

    public Studente(int codStu, String cognome, String nome, String classe){
        this.codStu = codStu;
        this.cognome = cognome;
        this.nome = nome;
        this.classe = classe;
    }

    public int getCode(){
        return codStu;
    }

    public String getName(){
        return nome;
    }

    public String getSurname(){
        return cognome;
    }

    public String getClasse(){
        return classe;
    }

    public void setName(String nome){
        this.nome = nome;
    }

    public void setSurname(String cognome){
        this.cognome = cognome;
    }

    public void setClasse(String classe){
        this.classe = classe;
    }
}
