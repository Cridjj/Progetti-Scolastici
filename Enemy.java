import java.io.Serializable;

public class Enemy implements Serializable {
    protected int enemyPower;
    protected int enemyLife;
    protected String enemyName;
    protected boolean isSpecialized;

    public Enemy(int enemyPower, int enemyLife, String enemyName) {
        this.enemyPower = enemyPower;
        this.enemyLife = enemyLife;
        this.enemyName = enemyName;
        this.isSpecialized = false;
    }

    public int getPower() {
        return enemyPower;
    }

    public int getLife() {
        return enemyLife;
    }

    public String getName() {
        return enemyName;
    }

    public void setPower(int enemyPower) {
        this.enemyPower = enemyPower;
    }

    public void setLife(int enemyLife) {
        this.enemyLife = enemyLife;
    }

    public void setName(String enemyName) {
        this.enemyName = enemyName;
    }

    public void attackPlayer(Player player) {
        player.playerLife -= enemyPower;
    }

    public void takeDamage(int playerPower) {
        enemyLife -= playerPower;
    }

    @Override
    public String toString() {
        String retValue = "";

        retValue += "- Nome: " + ConsoleColors.WHITE_BOLD + enemyName + ConsoleColors.WHITE + "\n";
        retValue += "- Vita: " + ConsoleColors.WHITE_BOLD + enemyLife + ConsoleColors.WHITE + "\n";
        retValue += "- Potenza: " + ConsoleColors.WHITE_BOLD + enemyPower + ConsoleColors.WHITE + "\n";

        return retValue;
    }
}
