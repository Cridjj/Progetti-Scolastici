import java.io.Serializable;

public class Insegna implements Serializable{
    private int codIns;
    private int codDoc;
    private int codStu;

    public Insegna(int codIns, int codDoc, int codStu){
        this.codIns = codIns;
        this.codDoc = codDoc;
        this.codStu = codStu;
    }

    public int getCode(){
        return codIns;
    }

    public int getCodDoc(){
        return codDoc;
    }

    public int getCodStu(){
        return codStu;
    }

    public void setCodDoc(int codDoc){
        this.codDoc = codDoc;
    }

    public void setCodStu(int codStu){
        this.codStu = codStu;
    }

}
