import java.util.Scanner;
import java.util.Random;
import java.util.Vector;

public class Game {
    public static final String[] enemyNames = { "Necross", "Malphas", "Lyra", "Xalazar", "Kharaz", "Gorganna",
            "Asterius", "Thanatos", "Moros", "Hydra" };

    public static final int startingPlayerLife = 10;
    public static final int minPlayerPower = 10;
    public static final int maxPlayerPower = 20;
    public static final int maxPlayerValues = 30;
    public static final int maxEnemyNumber = 10;
    public static final int minEnemyNumber = 5;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // variabile che riceve la scelta fatta dall'utente nel menù
        int playerMenuInput;
        FileManager file = new FileManager("Salvataggio");

        // variabili del player e gli enemies
        Player player;
        Vector<Enemy> enemies;

        if (!file.isEmpty()) {
            String userReply;

            System.out.print("Hai delle partite salvate! Desideri ricominciare da una di queste? (Y/N): ");
            do {
                userReply = input.nextLine();
                if (!userReply.equals("Y") && !userReply.equals("y") && !userReply.equals("N")
                        && !userReply.equals("n"))
                    System.out.print(">> Valore non valido. Riprova: ");
            } while (!userReply.equals("Y") && !userReply.equals("y") && !userReply.equals("N")
                    && !userReply.equals("n"));

            if (userReply.equals("Y") || userReply.equals("y")) {
                String fileName = file.selectSaveFile();
                if (fileName.equals("$exit")) {
                    input.close();
                    return;
                }
                Object[] savedData = new Object[2];
                savedData = file.readData(fileName);
                if (savedData == null) {
                    System.out.println("Errore nel caricamento della partita.");
                    input.close();
                    return;
                }
                player = (Player) savedData[0];
                enemies = (Vector<Enemy>) savedData[1];
            } else {
                // inizio gioco
                System.out.print("Premi " + ConsoleColors.WHITE_BOLD + "INVIO " + ConsoleColors.WHITE
                        + "per iniziare a giocare..");
                input.nextLine();

                player = generatePlayer();
                enemies = generateEnemies();
            }
        } else {
            // inizio gioco
            System.out.print("Premi " + ConsoleColors.WHITE_BOLD + "INVIO " + ConsoleColors.WHITE
                    + "per iniziare a giocare..");
            input.nextLine();

            player = generatePlayer();
            enemies = generateEnemies();
        }

        // sviluppo del gioco
        do {
            playerMenuInput = Menu.showMenu();
            player = Menu.selectChoice(playerMenuInput, player, enemies, file);
        } while (playerMenuInput != 0 && player.playerLife > 0 && enemies.size() > 0);

        input.close();

        if (player.playerLife <= 0)
            System.out.println("Purtroppo sei morto. Però, non mollare! Un giorno sarai più forte di loro..");
        if (enemies.size() == 0)
            System.out.println("Hai ucciso tutti i nemici! Sei troppo forte per questo gioco..");
    }

    private static Player generatePlayer() {
        Scanner input = new Scanner(System.in);

        // informazioni temporanee per la creazione del Player
        String playerName;
        int playerPower;
        int playerLife;
        int playerMoney = 5;
        int maxPlayerArtifacts = 2;

        // inizializzazione Player
        Player player;

        System.out.print("Inserisci il nome del Player (5-15 caratteri): ");
        do {
            playerName = input.nextLine();
            if (playerName.length() < 5 || playerName.length() > 15)
                System.out.print("Nome inserito non valido. Riprova: ");
        } while (playerName.length() < 5 || playerName.length() > 15);

        System.out.print(
                "La vita iniziale di '" + playerName + "' è " + ConsoleColors.WHITE_BOLD + startingPlayerLife
                        + ConsoleColors.WHITE + ". Inserisci la sua potenza ("
                        + minPlayerPower + "-" + maxPlayerPower + "): ");
        do {
            playerPower = input.nextInt();
            if (playerPower < minPlayerPower || playerPower > maxPlayerPower)
                System.out.print("Potenza inserita non valido. Riprova: ");
        } while (playerPower < minPlayerPower || playerPower > maxPlayerPower);

        playerLife = 0;
        playerLife += maxPlayerValues - playerPower;
        System.out.println("La vita finale di '" + ConsoleColors.WHITE_BOLD + playerName + ConsoleColors.WHITE + "' è "
                + playerLife + ".");
        System.out.println(
                "Al momento, possiedi " + ConsoleColors.WHITE_BOLD + playerMoney + ConsoleColors.WHITE
                        + " monete. Ottienine di più uccidendo nemici specializzati!");

        // inizializzazione player
        player = new Player(playerPower, playerLife, playerMoney, playerName, maxPlayerArtifacts);

        return player;
    }

    private static Vector<Enemy> generateEnemies() {
        Random rand = new Random();

        // informazioni temporanee per la crezione degli enemies
        int enemyNumber;
        int randEnemyPower;
        int enemyLife;
        int specializedNumber;
        int specialization;

        Vector<Enemy> enemies;

        // inizializzazione enemys
        enemyNumber = rand.nextInt(maxEnemyNumber - minEnemyNumber + 1) + minEnemyNumber; // il numero di enemys è
                                                                                          // casuale, da 5 a 10
        specializedNumber = rand.nextInt((enemyNumber / 2) - 1) + 2; // da 2 a enemyNumber/2 nemici specializzati
        enemies = new Vector<Enemy>(enemyNumber);
        specialization = 0;
        // attribuzione valori agli enemies
        for (int i = 0; i < enemyNumber; i++) {
            // la potenza dell'enemy è casuale, da 10 a 20
            randEnemyPower = rand.nextInt(maxPlayerPower - minPlayerPower + 1) + minPlayerPower;
            enemyLife = (maxPlayerValues - 1) - randEnemyPower; // la vita si calcola togliendo a 29 la potenza ottenuta

            // si istanzia il singolo enemy nell'array con i valori generati (la selezione
            // dei nomi è invece ordinata), gli enemy specializzati vengono istanziati in
            // maniera alternata alla fine del Vector
            if (i > enemyNumber - specializedNumber - 1) {
                if (specialization == 0) {
                    enemies.add(i, new HasArmor(randEnemyPower, enemyLife, enemyNames[i], rand.nextInt(30) + 10));
                    specialization++;
                } else {
                    enemies.add(i, new HasSword(randEnemyPower, enemyLife, enemyNames[i], rand.nextInt(30) + 10));
                    specialization--;
                }
            } else
                enemies.add(i, new Enemy(randEnemyPower, enemyLife, enemyNames[i]));
        }

        return enemies;
    }
}