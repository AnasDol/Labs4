package labs.model.ants;

public class Worker extends Ant {

    public Worker(int index, double x, double y, Color color, int health, int lifeTime, int birthtime) {
        super(index, x, y, Type.Worker, color, health, lifeTime, birthtime);
        n++;
        if (color==Color.Red) n_red_worker++;
        else n_black_worker++;
    }

}