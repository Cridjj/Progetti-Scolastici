import java.io.Serializable;
import java.util.Vector;

public class GestioneInsegna implements Serializable{
    private int insegnaCounter;
    private Vector<Insegna> insegna;

    public GestioneInsegna(){
        insegnaCounter = 1;
        insegna = new Vector<Insegna>();
    }

    public void setInsegna(Vector<Insegna> insegna){
        this.insegna = insegna;
    }

    public void addInsegna(int codDoc, int codStu){
        insegna.add(new Insegna(insegnaCounter, codDoc, codStu));
        insegnaCounter++;
    }

    public void removeInsegna(int insIndex){
        insegna.removeElementAt(insIndex);
    }

    public int getIndexFromCode(int codIns){
        for(int i=0; i<insegna.size(); i++){
            if(codIns==insegna.elementAt(i).getCode())
                return i;
        }
        return -1;
    }

    public boolean isEmpty(){
        return insegna==null || insegna.size()==0;
    }

    public int getCounter(){
        return insegnaCounter;
    }

    public Vector<Insegna> getInsegna(){
        return insegna;
    }

    @Override
    public String toString(){
        String retValue = "";

        for(int i=0; i<insegna.size(); i++){
            retValue += insegna.elementAt(i).getCode() + "; " + insegna.elementAt(i).getCodDoc() + "; ";
            retValue += insegna.elementAt(i).getCodStu() + "\n";
        }

        return retValue;
    }

    public boolean codeExists(int codIns){
        for(int i=0; i<insegna.size(); i++){
            if(codIns==insegna.elementAt(i).getCode())
                return true;
        }
        return false;
    }

    public void setCodDoc(int codDoc, int index){
        insegna.elementAt(index).setCodDoc(codDoc);
    }

    public void setCodStu(int codStu, int index){
        insegna.elementAt(index).setCodDoc(codStu);
    }

    public int deleteLinkedDoc(int codDoc){
        int deletedRelationships = 0;

        for(int i=0; i<insegna.size(); i++){
            if(codDoc==insegna.elementAt(i).getCodDoc()){
                insegna.remove(i);
                deletedRelationships++;
            }
        }

        return deletedRelationships;
    }

    public int deleteLinkedStu(int codStu){
        int deletedRelationships = 0;

        for(int i=0; i<insegna.size(); i++){
            if(codStu==insegna.elementAt(i).getCodStu()){
                insegna.remove(i);
                deletedRelationships++;
            }
        }

        return deletedRelationships;
    }

    public String searchData(int value){
        String retValue = "";

        for(int i=0; i<insegna.size(); i++){
            if(value==insegna.elementAt(i).getCode() || value==insegna.elementAt(i).getCodDoc() || value==insegna.elementAt(i).getCodStu()){  
                retValue += insegna.elementAt(i).getCode() + "; " + insegna.elementAt(i).getCodDoc() + "; ";
                retValue += insegna.elementAt(i).getCodStu() + "\n";
            }
        }

        return retValue;
    }
}
