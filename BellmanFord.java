import java.util.Arrays;
import java.util.Scanner;

public class BellmanFord {
    public static void algorithm(boolean isDefault, int nodeAmount){
        Scanner input;

        Nodo[] nodes = new Nodo[nodeAmount];
        int userChoice;

        int startingNode; // Indice nodo da cui partire
        int endingNode;   // Indice nodo a cui arrivare

        input = new Scanner(System.in);

        /* FASE DI INPUT - L'utente, in base alle opzioni inserite in precedenza, ha la possibilità
         * di scegliere se inserire i dati manualmente o utilizzando una matrice di default.
         */

        // Input / generazione matrice
        if(isDefault){
            int[][] nodeCosts = new int[][] {{0, 1, 0, 0, 0, 3},
                                             {1, 0, 3, 0, 5, 1},
                                             {0, 3, 0, 2, 0, 0},
                                             {0, 0, 2, 0, 1, 6},
                                             {0, 5, 0, 1, 0, 2},
                                             {3, 1, 0, 6, 2, 0}};

            for(int i=0; i<nodeAmount; i++){
                nodes[i] = new Nodo(i, nodeAmount, nodeCosts[i]);
            }
        }
        else{
            for(int i=0; i<nodeAmount; i++){
                int[] nodeCosts = new int[nodeAmount];
                for(int j=0; j<nodeAmount; j++){
                    if(j==i)
                        nodeCosts[j] = 0;
                    else{
                        System.out.print(">> Inserisci il costo per i nodi " + Nodo.getName(i, nodeAmount) + "-" + Nodo.getName(j, nodeAmount) + " (0 per indicare nessun collegamento): ");
                        userChoice = input.nextInt();
                        nodeCosts[j] = userChoice;
                    }
                }
                nodes[i] = new Nodo(i, nodeAmount, nodeCosts);
            }
        }

        // Input / generazione nodi di partenza e arrivo
        if(isDefault){
            startingNode = 0;
            endingNode = 3;
        }
        else{
            for(int i=0; i<nodeAmount; i++){
                System.out.println((i+1) + ". " + Nodo.getName(i, nodeAmount));
            }

            System.out.print(">> Scegli il nodo di partenza (1-"+ nodeAmount +"): ");
            do{
                userChoice = input.nextInt();
                if(userChoice < 1 || userChoice > nodeAmount)
                    System.out.print(">> Valore non valido. Riprova: ");
            }while(userChoice < 1 || userChoice > nodeAmount);
            
            startingNode = userChoice-1;
            
            for(int i=0; i<nodeAmount; i++){
                if(i != startingNode)
                    System.out.println((i+1) + ". " + Nodo.getName(i, nodeAmount));
            }

            System.out.print(">> Scegli il nodo di arrivo (1-"+ nodeAmount +"): ");
            do{
                userChoice = input.nextInt();
                if(userChoice == (startingNode+1) || userChoice < 1 || userChoice > nodeAmount)
                    System.out.print(">> Valore non valido. Riprova: ");
            }while(userChoice == (startingNode+1) || userChoice < 1 || userChoice > nodeAmount);
            
            endingNode = userChoice-1;
        }

        // La distanza del nodo di partenza è 0
        nodes[startingNode].distance = 0;

        // Stampa matrice di costi per tutti i nodi
        System.out.println("\nMatrice di costi: ");
        for(int i=0; i<nodes.length; i++){
            System.out.println(Nodo.getName(i, nodeAmount) + " " + Arrays.toString(nodes[i].costs));
        }

        // Stampa nodi di partenza e arrivo
        System.out.println("\nNodo di partenza: " + Nodo.getName(startingNode, nodeAmount));
        System.out.println("Nodo di arrivo: " + Nodo.getName(endingNode, nodeAmount));

        /* L'algoritmo si basa su iterazioni, fatte per 2*nodi-1 volte,
         * in modo tale da ripetere l'aggiornamente delle distanze per una quantità
         * ragionevole di volte. Se, però, c'è una ripetizione, allora l'iterazione
         * viene interrotta.
         */
        for(int j=0; j<2*nodeAmount-1; j++){
            System.out.println("\n--- Iterazione #" + (j+1) + " ---");
            for(int nodeIndex = 0; nodeIndex<nodeAmount; nodeIndex++){
                for(int i=0; i<nodeAmount; i++){
                    if(nodes[nodeIndex].costs[i] != 0 && nodes[nodeIndex].distance != Integer.MAX_VALUE){
                        if(nodes[i].distance > (nodes[nodeIndex].costs[i]+nodes[nodeIndex].distance)){
                            nodes[i].distance = nodes[nodeIndex].costs[i]+nodes[nodeIndex].distance;
                            nodes[i].previousNode = nodeIndex;
                        }
                    }
                }
                System.out.println(nodes[nodeIndex].toString(nodeAmount));
            }
        }

        // Controllo della presenza di cicli negativi nel grafo
        for(int nodeIndex = 0; nodeIndex<nodeAmount; nodeIndex++){
            for(int i=0; i<nodeAmount; i++){
                if(nodes[nodeIndex].costs[i] != 0 && nodes[nodeIndex].distance != Integer.MAX_VALUE){
                    if(nodes[i].distance > (nodes[nodeIndex].costs[i]+nodes[nodeIndex].distance)){
                        System.out.println("\nIl grafo contiene un ciclo con peso negativo.");
                        return;
                    }
                }
            }
        }

        // Array che contiene gli indici dei nodi che compongono il percorso più breve
        int[] minPath = new int[nodeAmount];

        // Indici dell'array di nodi e di minPath
        int j = endingNode;
        int i = nodeAmount-1;

        // Il primo elemento dell'array è il nodo di arrivo, inserito nell'ultima posizione dell'array
        minPath[i] = j;

        do{
            i--;
            j = nodes[j].previousNode;
            if(j==-1){
                System.out.println("Impossibile trovare un collegamento tra " + Nodo.getName(startingNode, nodeAmount) + " e " + Nodo.getName(endingNode, nodeAmount) + ".");
                return;
            }
            minPath[i] = j;
        }while(nodes[j].previousNode != -1);
        
        // Stampa del percorso più breve e del suo costo
        System.out.println("\nIl cammino minimo per andare da " + Nodo.getName(startingNode, nodeAmount) + " a " + Nodo.getName(endingNode, nodeAmount) + " è composto da:");

        for(j=i; j<nodeAmount; j++){
            System.out.print(Nodo.getName(minPath[j], nodeAmount));
            if(j<nodeAmount-1)
                System.out.print(", ");
        }
        System.out.println(" - Costo: " + nodes[endingNode].distance);
    }
}