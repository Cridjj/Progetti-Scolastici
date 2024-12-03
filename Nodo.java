public class Nodo {
    public final int INF = Integer.MAX_VALUE;

    //nome del singolo nodo, composto dalla struttura LETTERA (A-Z) + NUMERO (0-INF)
    //in modo da garantire una disponibilità illimitata di nomi ai nodi
    protected String name;

    //costi del nodo per tutti gli altri nodi
    protected int[] costs;

    //distanza dal nodo di partenza al nodo inizializzato
    protected int distance;

    //indice del nodo precedente seguendo il percorso del nodo di partenza
    protected int previousNode;

    //variabile che indica se il nodo è stato esplorato o no
    protected boolean isExplored;

    public Nodo(int nodeIndex, int nodeAmount, int[] costs){
        name = getName(nodeIndex, nodeAmount);
        isExplored = false;
        this.costs = costs;
        distance = INF;
        previousNode = -1;
    }

    public static String getName(int nodeIndex, int nodeAmount){
        String name;
        //assegnazione nome: se sono presenti meno nodi di quante lettere esistono,
        //allora il nome del nodo sarà caratterizzato dalla singola lettera, altrimenti
        //alla lettera viene attribuito un numero che aumenterà ogni volta che viene
        //superata la Z
        if(nodeAmount<26)
            name=""+(char)(65+nodeIndex);
        else
            name=""+(char)(65+nodeIndex%26)+nodeIndex/26;

        return name;
    }

    public String toString(int nodeAmount){
        String retValue = name;

        if(distance == INF)
            retValue += " | INF | ";
        else retValue += " | " + distance + " | ";

        if(previousNode == -1)
            retValue += "";
        else retValue += getName(previousNode, nodeAmount);

        return retValue;
    }
}