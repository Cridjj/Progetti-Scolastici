import java.util.Scanner;
import java.util.Random;
import java.lang.InterruptedException;
import java.util.Vector;

public class Menu {
    public static final int playerUpPrice = 10;
    public static final int artifactPrice = 3;

    public static int showMenu() {
        int playerInput;
        String retValue = "";

        retValue += "---------------------------\n";
        retValue += "      CONTROLLI GIOCO     \n";
        retValue += "---------------------------\n";
        retValue += "1. Visualizza statistiche personali.\n";
        retValue += "2. Visualizza statistiche enemy vivi.\n";
        retValue += "3. Visualizza statistiche enemy per nome.\n";
        retValue += "4. Acquista potenziamento (costo: " + playerUpPrice + " monete).\n";
        retValue += "5. Acquista artefatto Totem (costo: " + artifactPrice + " monete).\n";
        retValue += "6. Attacca un enemy!\n";
        retValue += "7. Recupera vita.\n";
        retValue += "8. Salva la partita.\n";
        retValue += "---------------------------\n";
        retValue += "0. Esci dal gioco\n\n";
        retValue += ">> Seleziona un'opzione: ";

        Scanner input = new Scanner(System.in);
        System.out.print(retValue);

        do {
            playerInput = input.nextInt();
            if (playerInput < 0 || playerInput > 8)
                System.out.print("Valore non valido. Riprova: ");
        } while (playerInput < 0 || playerInput > 8);

        return playerInput;
    }

    public static Player selectChoice(int playerMenuInput, Player player, Vector<Enemy> enemies, FileManager file) {
        switch (playerMenuInput) {
            case 1:
                System.out.println(player.toString(player.artifacts));
                break;
            case 2:
                Menu.printEnemyList(enemies);
                break;
            case 3:
                Menu.printEnemyByName(enemies);
                break;
            case 4:
                player = Menu.buyUpgrade(player);
                break;
            case 5:
                Menu.buyArtifact(player, enemies);
                break;
            case 6:
                Menu.attackEnemy(player, enemies);
                break;
            case 7:
                Menu.restPlayer(player);
                break;
            case 8:
                Menu.saveGame(file, player, enemies);
                break;
            default:
                return player;
        }
        confirmExit();
        clearScreen();
        return player;
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
    }

    private static void confirmExit() {
        Scanner input = new Scanner(System.in);
        System.out.print("\nPremi invio per continuare..");
        input.nextLine();
    }

    private static void sleep(int msTime) {
        // mette in pausa per msTime millisecondi l'esecuzione del programma
        try {
            Thread.sleep(msTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // FUNZIONI DEL MENU DI GIOCO (2-6)
    private static void printEnemyList(Vector<Enemy> enemies) {
        String retValue = "";

        for (int i = 0; i < enemies.size(); i++) {
            retValue += "--- Informazioni Enemy #" + (i + 1) + " ---\n";
            retValue += enemies.get(i).toString() + "\n";
        }

        System.out.print(retValue);
    }

    private static Player buyUpgrade(Player player) {
        if (player.isSpecialized) {
            System.out.println("Hai già sbloccato il potenziamento. Non puoi diventare più forte di così.. per ora.");
        } else if (player.playerMoney < playerUpPrice) {
            System.out.println("Non hai abbastanza monete. Ottienine di più uccidendo nemici specializzati! Costo: "
                    + playerUpPrice + " monete.");
        } else {
            System.out.println(
                    "Potenziamento acquistato. Ora sei specializzato come Cartomante: visualizza le tue statistiche per ottenere maggiori informazioni!");
            player.playerMoney -= playerUpPrice;

            player = new Cartomante(player.playerPower, player.playerLife, player.playerMoney, player.playerName,
                    player.maxPlayerArtifacts);
        }
        return player;

    }

    private static void buyArtifact(Player player, Vector<Enemy> enemies) {
        if (enemies.capacity() == enemies.size()) {
            System.out.println("Uccidi almeno un nemico per poter acquistare un Totem!");
            return;
        }

        if (player.artifacts.capacity() == player.artifacts.size()) {
            System.out.println("Hai accumulato troppi Totem. Utilizzane almeno uno prima di acquistarne un altro!");
            return;
        }

        if (player.playerMoney < artifactPrice) {
            System.out.println(
                    "Non hai le monete necessarie per poter acquistare un Totem. Costo: " + artifactPrice + " monete.");
            return;
        }

        player.artifacts.add(new Totem());
        player.playerMoney -= artifactPrice;
        System.out.println(
                "Totem acquistato correttamente. Utilizzalo durante gli attacchi, se non hai abbastanza vita.");
    }

    private static void printEnemyByName(Vector<Enemy> enemies) {
        String retValue = "";
        int playerInput;

        Scanner input = new Scanner(System.in);

        for (int i = 0; i < enemies.size(); i++) {
            retValue += (i + 1) + ". " + enemies.get(i).enemyName + "\n";
        }

        // stampa lista nomi enemies
        System.out.println(retValue);

        // input numerico corrispondente al nome di un enemy
        System.out.print(">> Seleziona l'enemy da visualizzare (1-" + enemies.size() + "): ");
        do {
            playerInput = input.nextInt();
            if (playerInput < 1 || playerInput > enemies.size())
                System.out.print("Valore non valido. Riprova: ");
        } while (playerInput < 1 || playerInput > enemies.size());

        // chiamata del toString dell'enemy selezionato
        System.out.print(enemies.get(playerInput - 1));
    }

    private static void attackEnemy(Player player, Vector<Enemy> enemies) {
        Scanner input = new Scanner(System.in);
        String playerReply;

        // se la vita del player è minore di 10, viene inviato un avvertimento
        if (player.playerLife < 10) {
            System.out.println(
                    "\nAttento! Hai poca vita (" + player.playerLife + " hp), vuoi davvero andare all'attacco?");

            // per proseguire, basterà premere invio, altrimenti bisognerà premere 0 (e
            // successivamente invio)
            System.out.print(">> Premi INVIO per continuare, '0' per tornare al menù di gioco: ");

            playerReply = input.nextLine();

            if (playerReply.equals("0"))
                return;
        }

        Random rand = new Random();
        int whoToAttack;
        int whoAttacks;
        boolean artifactUsed = false;

        System.out.println("Il dado è stato lanciato..");
        sleep(1000);

        // ricerca casuale di un nemico da affrontare (tra quelli vivi)
        whoToAttack = rand.nextInt(enemies.size());

        System.out.println("Il tuo sfidante sarà.. " + enemies.get(whoToAttack).enemyName + "!");
        if (enemies.get(whoToAttack).isSpecialized) {
            System.out.print("Attento! Potresti ricevere 5 monete se uccidi questo nemico, perché è specializzato! ");
            if (enemies.get(whoToAttack).getClass().getSimpleName().equals("HasArmor"))
                System.out.println("Specializzazione ARMATURA.");
            else
                System.out.println("Specializzazione SPADA.");
        }
        sleep(500);

        if (player.artifacts.size() > 0) {
            System.out.print(
                    "Vuoi utilizzare un Totem? Attualmente, ne possiedi " + player.artifacts.size() + ". (Y/N) ");
            do {
                playerReply = input.nextLine();
                if (!playerReply.equals("Y") && !playerReply.equals("N"))
                    System.out.print("Valore non riconosciuto. Riprova: ");
            } while (!playerReply.equals("Y") && !playerReply.equals("N"));
            if (playerReply.equals("Y"))
                artifactUsed = true;
        }

        sleep(500);
        System.out.println("\nChi inizierà ad attaccare? Consultiamo il dado..");
        sleep(1000);

        // estrazione casuale tra 0 e 1 per chi inizia
        whoAttacks = rand.nextInt(2);
        // whoAttacks = 0 > inizia player - whoAttacks = 1 > inizia enemy

        // se il player è specializzato, ha abbastanza soldi e l'esito del rand è
        // "enemy" (1), avrà
        // la possibilità di invertire l'esito, dunque di iniziare attaccando invece che
        // difendendo
        if (whoAttacks == 1 && player.isSpecialized && player.playerMoney > 1) {
            System.out.print("Il dato ha scelto: Enemy! Vuoi invertire la sorte? Costo: 2 monete. (Y/N) ");
            do {
                playerReply = input.nextLine();
                if (!playerReply.equals("Y") && !playerReply.equals("N"))
                    System.out.print("Valore non riconosciuto. Riprova: ");
            } while (!playerReply.equals("Y") && !playerReply.equals("N"));

            if (playerReply.equals("Y")) {
                System.out.println("Sorte cambiata, tocca a te combattere!");
                player.playerMoney -= 2;
                whoAttacks = 0;
            }
        }

        if (whoAttacks == 0) {
            System.out.println("Sarai tu il primo ad attaccare!\n");

            while (true) {
                enemies.get(whoToAttack).takeDamage(player.playerPower);
                System.out.println("- Hai attaccato l'enemy, ora ha " + enemies.get(whoToAttack).enemyLife + " hp!");

                // se l'attacco del player rende la vita dell'enemy 0 o negativa, allora il
                // player ha vinto la potenza ATTUALE dell'enemy viene trasferita al player
                if (enemies.get(whoToAttack).enemyLife <= 0) {
                    System.out.println("\nHai vinto! Hai ricevuto " + enemies.get(whoToAttack).enemyPower
                            + " di potenza da questo scontro!");
                    if (enemies.get(whoToAttack).isSpecialized) {
                        System.out.println("Hai ucciso un nemico specializzato! Ti sei guadagnato 5 monete!");
                        player.playerMoney += 5;
                    }

                    player.playerPower += enemies.get(whoToAttack).enemyPower;
                    enemies.remove(whoToAttack);
                    if (artifactUsed)
                        player.artifacts.remove(player.artifacts.lastElement());
                    return;
                }

                enemies.get(whoToAttack).enemyPower /= 2; // se l'attacco del player non uccide l'enemy, allora la
                                                          // potenza
                                                          // dell'enemy viene dimezzata
                sleep(500);

                // attacca l'enemy, che riduce la vita del player di tanto quanto la potenza
                // ATTUALE dell'enemy
                enemies.get(whoToAttack).attackPlayer(player);

                if (player.playerLife <= 0 && artifactUsed) {
                    System.out.println("Stavi per morire.. il Totem ti ha salvato!");
                    player.artifacts.lastElement().activate(player);
                    player.artifacts.remove(player.artifacts.lastElement());
                    artifactUsed = false;
                }

                System.out.println("- L'enemy controbatte, hai " + player.playerLife + " hp!");

                // se l'attacco dell'enemy rende la vita del player nulla o negativa, allora
                // l'enemy ha vinto
                if (player.playerLife <= 0 && !artifactUsed) {
                    System.out.println("\nSei stato sconfitto! Sei stato sfortunato..");
                    return;
                }

                sleep(500);
            }

        } else {
            // funzionamento identico anche nel caso in cui whoAttacks vale 1
            System.out.println("Sarà l'enemy il primo ad attaccare!\n");

            while (true) {
                enemies.get(whoToAttack).attackPlayer(player);

                if (player.playerLife <= 0 && artifactUsed) {
                    System.out.println("Stavi per morire.. il Totem ti ha salvato!");
                    player.artifacts.lastElement().activate(player);
                    player.artifacts.remove(player.artifacts.lastElement());
                    artifactUsed = false;
                }

                System.out.println("- L'enemy attacca, hai " + player.playerLife + " hp!");

                if (player.playerLife <= 0 && !artifactUsed) {
                    System.out.println(
                            ConsoleColors.RED + "\nHai perso!" + ConsoleColors.WHITE + " Sei stato sfortunato..");
                    return;
                }

                sleep(500);

                enemies.get(whoToAttack).takeDamage(player.playerPower);
                System.out.println("- Hai risposto all'enemy, ora ha " + enemies.get(whoToAttack).enemyLife + " hp!");

                if (enemies.get(whoToAttack).enemyLife <= 0) {
                    System.out.println(ConsoleColors.GREEN + "\nHai vinto!" + ConsoleColors.WHITE + " Hai ricevuto "
                            + enemies.get(whoToAttack).enemyPower
                            + " di potenza da questo scontro!");
                    if (enemies.get(whoToAttack).isSpecialized) {
                        System.out.println("Hai ucciso un nemico specializzato! Ti sei guadagnato 5 monete!");
                        player.playerMoney += 5;
                    }
                    player.playerPower += enemies.get(whoToAttack).enemyPower;
                    enemies.remove(whoToAttack);
                    if (artifactUsed)
                        player.artifacts.remove(player.artifacts.lastElement());
                    return;
                }

                enemies.get(whoToAttack).enemyPower /= 2;
                sleep(500);
            }
        }
    }

    private static void restPlayer(Player player) {
        if ((player.playerPower - Game.minPlayerPower) == 0) {
            System.out.println("La tua vita è al massimo!");
            return;
        }

        Scanner input = new Scanner(System.in);
        int lifeToRest;

        System.out
                .print(">> Qual è la quantità da recuperare? (1-" + (player.playerPower - Game.minPlayerPower) + ") ");

        // input vita da recuperare per player
        do {
            lifeToRest = input.nextInt();
            // la vita in input è accettata solo se tra 1 e la quantità che rende la potenza
            // pari al minimo possibile (10)
            if (lifeToRest > (player.playerPower - Game.minPlayerPower) || lifeToRest < 1)
                System.out.print("Valore non valido. Riprova: ");
        } while (lifeToRest > (player.playerPower - Game.minPlayerPower) || lifeToRest < 1);

        if (player.isSpecialized)
            System.out.println(ConsoleColors.WHITE_BOLD
                    + "Hai una possibilità su 3 di ricevere un hp extra! Vediamo quanto sarai fortunato..");

        // il player recupera la vita richiesta
        player.restPlayer(lifeToRest);

        System.out.print(lifeToRest + " hp recuperati correttamente!");
    }

    private static void saveGame(FileManager file, Player player, Vector<Enemy> enemies) {
        if (file.saveGame(player, enemies))
            System.out.println("Salvataggio effettuato!");
        else
            System.out.println("Errore nel salvataggio della partita.");
    }
}
