public class HasArmor extends Enemy {
    protected int percentageProtection;

    public HasArmor(int enemyPower, int enemyLife, String enemyName, int percentageProtection) {
        super(enemyPower, enemyLife, enemyName);
        isSpecialized = true;
        this.percentageProtection = percentageProtection;
    }

    @Override
    public void takeDamage(int playerPower) {
        enemyLife -= playerPower - ((playerPower * percentageProtection) / 100);
    }

    @Override
    public String toString() {
        String retValue = super.toString();

        retValue += "\nArmatura SPECIALIZZATA, danno ricevuto ridotto del " + ConsoleColors.WHITE_BOLD
                + percentageProtection + ConsoleColors.WHITE + "%";

        return retValue;
    }
}
