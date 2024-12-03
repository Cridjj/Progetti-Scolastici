import java.util.Arrays;
import java.util.Scanner;

public class Dijkstra {
    public static void algorithm(boolean isDefault, boolean isOriented, int nodeAmount){
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
            /* Nel caso delle soluzioni di default, ad ogni selezione fatta nel menù di partenza
             * si avrà una matrice con i rispettivi nodi. Nel caso di qualsiasi nodo, si sceglie
             * una soluzione con 6 nodi.
            */
            int[][] nodeCosts = new int[nodeAmount][nodeAmount];

            switch(nodeAmount){
                case 5:
                                            //A, B, C, D, E
                    nodeCosts = new int[][] {{0, 2, 0, 0, 0},
                                             {0, 0, 3, 1, 6},
                                             {0, 0, 0, 0, 4},
                                             {0, 0, 0, 0, 2},
                                             {0, 0, 0, 0, 0}};
                    break;
                case 6:
                                            //A, B, C, D, E, F
                    nodeCosts = new int[][] {{0, 1, 0, 0, 0, 3},
                                             {1, 0, 3, 0, 5, 1},
                                             {0, 3, 0, 2, 0, 0},
                                             {0, 0, 2, 0, 1, 6},
                                             {0, 5, 0, 1, 0, 2},
                                             {3, 1, 0, 6, 2, 0}};
                    break;
                case 10:
                                            //A, B, C, D, E, F, G, H, I, J
                    nodeCosts = new int[][] {{0, 3, 0, 0, 0, 0, 0, 2, 0, 0},
                                             {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                                             {0, 0, 0, 0, 0, 1, 5, 0, 0, 0},
                                             {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                             {0, 0, 2, 0, 0, 3, 0, 2, 0, 0},
                                             {0, 0, 0, 0, 0, 0, 1, 0, 2, 0},
                                             {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                                             {0, 0, 0, 0, 0, 0, 0, 0, 4, 0},
                                             {0, 0, 0, 0, 0, 0, 0, 0, 0, 7},
                                             {0, 0, 0, 0, 0, 0, 2, 0, 0, 0}};
                    break;
                default:
                    System.out.println("Errore in fase di input. Riprova.");
                    return;
            }

            // Inizializzazione della matrice dei costi di default nel vari nodi
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
                        if(!isOriented || nodes[j] == null || nodes[j].costs[i] == 0){
                            System.out.print(">> Inserisci il costo per i nodi " + Nodo.getName(i, nodeAmount) + "-" + Nodo.getName(j, nodeAmount) + " (0 per indicare nessun collegamento): ");
                            do{
                                userChoice = input.nextInt();
                                if(userChoice < 0)
                                    System.out.print(">> Valore non valido. Riprova: ");
                            }while(userChoice < 0);
                            nodeCosts[j] = userChoice;
                        }
                        else nodeCosts[j] = 0;
                    }
                }
                nodes[i] = new Nodo(i, nodeAmount, nodeCosts);
            }
        }

        // Input / generazione nodi di partenza e arrivo
        if(isDefault){
            switch(nodeAmount){
                case 5:
                    startingNode = 0;
                    endingNode = 4;
                    break;
                case 6:
                    startingNode = 0;
                    endingNode = 3;
                    break;
                case 10:
                    startingNode = 0;
                    endingNode = 3;
                    break;
                default:
                    System.out.println("Errore in fase di input. Riprova.");
                    return;
            }
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

        /* Finché sono presenti nodi da esplorare l'algoritmo
         * analizzerà tutti costi del nodo con distanza minore tra quelli
         * non esplorati e aggiornerà le distanze degli altri nodi se
         * possono essere migliorate
        */
        int exploredNodes = 0;
        int indexMinDistance = 0;
        while(exploredNodes != nodeAmount){
            int minDistance = Integer.MAX_VALUE;

            for(int i=0; i<nodeAmount; i++){
                if(nodes[i].distance<minDistance && !nodes[i].isExplored){
                    minDistance = nodes[i].distance;
                    indexMinDistance = i;
                }
            }

            if(minDistance == Integer.MAX_VALUE){
                break;
            }

            System.out.println("\nTra i nodi non ancora esplorati, " + Nodo.getName(indexMinDistance, nodeAmount) + " è quello con costo minore: " + minDistance);
            for(int i=0; i<nodeAmount; i++){
                if(nodes[indexMinDistance].costs[i] != 0 && !nodes[i].isExplored){
                    if(nodes[i].distance > (nodes[indexMinDistance].costs[i]+nodes[indexMinDistance].distance)){
                        nodes[i].distance = nodes[indexMinDistance].costs[i]+nodes[indexMinDistance].distance;
                        nodes[i].previousNode = indexMinDistance;
                    }
                }
                System.out.println(nodes[i].toString(nodeAmount));
            }
            nodes[indexMinDistance].isExplored = true;
            exploredNodes++;
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