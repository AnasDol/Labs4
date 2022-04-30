package labs.model.ants;

public class Warrior extends Ant {

    public int damage;

    public int MAX_DAMAGE = 10;
    public int MIN_DAMAGE = 5;

    public Warrior(int index, double x, double y, Color color, int health, int lifeTime, int birthtime, int damage) {
        super(index, x, y, Type.Warrior, color, health, lifeTime, birthtime);
        this.damage = damage;
        n++;
        if (color == Color.Red) n_red_warrior++;
        else n_black_warrior++;
    }

}
