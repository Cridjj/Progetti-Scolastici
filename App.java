import java.util.Scanner;

public class App {
    public static void main(String[] args){
        Scanner input;
        int menuSelection;

        input = new Scanner(System.in);
        do{
            clearScreen();
            showMenu();
            do{     
                menuSelection = input.nextInt();
                if(menuSelection < 0 || menuSelection > 4)
                    System.out.print(">> Valore non valido. Riprova: ");
            }while(menuSelection < 0 || menuSelection > 4);

            getOption(menuSelection);
        }while(menuSelection != 0);

        input.close();
    }

    public static void showMenu(){
        String retValue = "";

        retValue += "----- APPLICAZIONE DI UTILIZZO ALGORITMI DIJKSTRA E BELLMAN-FORD -----\n";
        retValue += "1. Dijkstra | Grafo orientato a 5 nodi.\n";
        retValue += "2. Dijkstra | Grafo orientato a 10 nodi.\n";
        retValue += "3. Dijkstra | Grafo qualsiasi.\n";
        retValue += "4. Bellman Ford | Algoritmo di iterazione.\n";
        retValue += "0. Uscita dal programma.\n";
        retValue += "\n>> Scegli un'opzione: ";

        System.out.print(retValue);
    }

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
    }

    public static void confirmExit() {
        Scanner input;

        input = new Scanner(System.in);

        System.out.print("Premi INVIO per continuare...");
        input.nextLine();
    }

    public static void getOption(int menuSelection){
        Scanner input = new Scanner(System.in);
        String replyString;
        boolean isDefault;
        int nodeAmount;

        switch(menuSelection){
            case 1:
                System.out.print(">> Visualizzare la soluzione di default? (Y/N): ");
                do{
                    replyString = input.nextLine();
                    if(!replyString.equals("Y") && !replyString.equals("y") && !replyString.equals("N") && !replyString.equals("n"))
                        System.out.print(">> Valore non valido. Riprova: ");
                }while(!replyString.equals("Y") && !replyString.equals("y") && !replyString.equals("N") && !replyString.equals("n"));
                if(replyString.equals("Y") || replyString.equals("y"))
                    isDefault = true;
                else isDefault = false;

                Dijkstra.algorithm(isDefault, true, 5);
                confirmExit();
                break;
            case 2:
                System.out.print(">> Visualizzare la soluzione di default? (Y/N): ");
                do{
                    replyString = input.nextLine();
                    if(!replyString.equals("Y") && !replyString.equals("y") && !replyString.equals("N") && !replyString.equals("n"))
                        System.out.print(">> Valore non valido. Riprova: ");
                }while(!replyString.equals("Y") && !replyString.equals("y") && !replyString.equals("N") && !replyString.equals("n"));
                if(replyString.equals("Y") || replyString.equals("y"))
                    isDefault = true;
                else isDefault = false;

                Dijkstra.algorithm(isDefault, true, 10);
                confirmExit();
                break;
            case 3:
                System.out.print(">> Visualizzare la soluzione di default? (Y/N): ");
                do{
                    replyString = input.nextLine();
                    if(!replyString.equals("Y") && !replyString.equals("y") && !replyString.equals("N") && !replyString.equals("n")){
                        System.out.print(">> Valore non valido. Riprova: ");
                    }
                }while(!replyString.equals("Y") && !replyString.equals("y") && !replyString.equals("N") && !replyString.equals("n"));
                if(replyString.equals("Y") || replyString.equals("y"))
                    isDefault = true;
                else isDefault = false;

                nodeAmount = 6;
                if(!isDefault){
                    System.out.print(">> Inserire il numero di nodi (> 2): ");
                    do{
                        nodeAmount = input.nextInt();
                        input.nextLine();
                        if(nodeAmount < 3)
                            System.out.print(">> Valore non valido. Riprova: ");
                    }while(nodeAmount < 3);
                }

                Dijkstra.algorithm(isDefault, false, nodeAmount);
                confirmExit();
                break;
            case 4:
                System.out.print(">> Visualizzare la soluzione di default? (Y/N): ");
                do{
                    replyString = input.nextLine();
                    if(!replyString.equals("Y") && !replyString.equals("y") && !replyString.equals("N") && !replyString.equals("n")){
                        System.out.print(">> Valore non valido. Riprova: ");
                    }
                }while(!replyString.equals("Y") && !replyString.equals("y") && !replyString.equals("N") && !replyString.equals("n"));
                if(replyString.equals("Y") || replyString.equals("y"))
                    isDefault = true;
                else isDefault = false;

                nodeAmount = 6;
                if(!isDefault){
                    System.out.print(">> Inserire il numero di nodi (> 2): ");
                    do{
                        nodeAmount = input.nextInt();
                        input.nextLine();
                        if(nodeAmount < 3)
                            System.out.print(">> Valore non valido. Riprova: ");
                    }while(nodeAmount < 3);
                }

                BellmanFord.algorithm(isDefault, nodeAmount);
                confirmExit();
                break;
            default:
                break;
        }
    }
}