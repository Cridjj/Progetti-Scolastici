import java.util.Random;
import java.util.Vector;

public class Cartomante extends Player {
    public Cartomante(int playerPower, int playerLife, float playerMoney, String playerName, int maxPlayerArtifacts) {
        super(playerPower, playerLife, playerMoney, playerName, maxPlayerArtifacts);
        isSpecialized = true;
    }

    @Override
    public void restPlayer(int lifeToRest) {
        Random rand = new Random();

        // la rest rimuove a power ciò che deve aggiungere a life
        playerLife += lifeToRest;

        if (rand.nextInt(3) == 1)
            playerLife += 1;

        playerPower -= lifeToRest;
    }

    @Override
    public String toString(Vector<Artifact> artifacts) {
        String retValue = super.toString(artifacts);

        retValue += "\n\nSPECIALIZZATO " + ConsoleColors.WHITE_BOLD + "Cartomante!" + ConsoleColors.WHITE + "\n";
        retValue += "- Al costo di " + ConsoleColors.WHITE_BOLD + "2 monete" + ConsoleColors.WHITE
                + ", potrai invertire la sorte sfavorevole del dado.\n";
        retValue += "- Hai una possibilità su 3 di ricevere un HP extra quando recuperi vita.";

        return retValue;
    }
}
