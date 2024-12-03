public class Totem extends Artifact{
    protected static int price = 3;
    
    public Totem(){}

    public int getPrice(){
        return price;
    }

    public void activate(Player player){
        player.playerLife = 1;
    }
}
