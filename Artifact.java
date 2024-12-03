import java.io.Serializable;

public abstract class Artifact implements Serializable {
    public abstract int getPrice();

    public abstract void activate(Player player);
}
