public class HasSword extends Enemy {
    protected int percentageDamage;

    public HasSword(int enemyPower, int enemyLife, String enemyName, int percentageDamage) {
        super(enemyPower, enemyLife, enemyName);
        isSpecialized = true;
        this.percentageDamage = percentageDamage;
    }

    @Override
    public void attackPlayer(Player player) {
        player.playerLife -= enemyPower + ((enemyPower * percentageDamage) / 100);
    }

    @Override
    public String toString() {
        String retValue = super.toString();

        retValue += "\nSpada SPECIALIZZATA, danno inflitto aumentato del " + ConsoleColors.WHITE_BOLD + percentageDamage
                + ConsoleColors.WHITE + "%";

        return retValue;
    }
}
