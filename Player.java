import java.io.Serializable;
import java.util.Vector;

public class Player implements Serializable {
    protected int playerPower;
    protected int playerLife;
    protected float playerMoney;
    protected String playerName;
    protected boolean isSpecialized;
    protected Vector<Artifact> artifacts;
    protected int maxPlayerArtifacts;
    protected int playerPoints;

    public Player(int playerPower, int playerLife, float playerMoney, String playerName, int maxPlayerArtifacts) {
        this.playerPower = playerPower;
        this.playerLife = playerLife;
        this.playerMoney = playerMoney;
        this.playerName = playerName;
        this.maxPlayerArtifacts = maxPlayerArtifacts;
        playerPoints = 0;
        isSpecialized = false;
        artifacts = new Vector<Artifact>(maxPlayerArtifacts);
    }

    public int getPower() {
        return playerPower;
    }

    public int getLife() {
        return playerLife;
    }

    public float getMoney() {
        return playerMoney;
    }

    public String getName() {
        return playerName;
    }

    public int getPoints() {
        return playerPoints;
    }

    public void setPower(int playerLife) {
        this.playerLife = playerLife;
    }

    public void setLife(int playerPower) {
        this.playerPower = playerPower;
    }

    public void setMoney(float playerMoney) {
        this.playerMoney = playerMoney;
    }

    public void setName(String playerName) {
        this.playerName = playerName;
    }

    public void addPoints(int points) {
        playerPoints += points;
    }

    public void takePoints(int points) {
        playerPoints -= points;
    }

    public String toString(Vector<Artifact> artifacts) {
        String retValue = "";

        retValue += "Informazioni Personali:\n";
        retValue += "- Nome: " + ConsoleColors.WHITE_BOLD + playerName + ConsoleColors.WHITE + "\n";
        retValue += "- Vita: " + ConsoleColors.WHITE_BOLD + playerLife + ConsoleColors.WHITE + "\n";
        retValue += "- Potenza: " + ConsoleColors.WHITE_BOLD + playerPower + ConsoleColors.WHITE + "\n";
        retValue += "- Monete: " + ConsoleColors.WHITE_BOLD + playerMoney + ConsoleColors.WHITE + "\n";
        retValue += "- Artefatti: " + ConsoleColors.WHITE_BOLD + artifacts.size() + "/" + artifacts.capacity()
                + ConsoleColors.WHITE + "\n";
        retValue += "- Punteggio: " + ConsoleColors.WHITE_BOLD + playerPoints + ConsoleColors.WHITE;

        return retValue;
    }

    public void restPlayer(int lifeToRest) {
        // la rest rimuove a power ciò che deve aggiungere a life
        playerLife += lifeToRest;
        playerPower -= lifeToRest;
    }
}