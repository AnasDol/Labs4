package labs.model.ants;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class Ant {

    public enum Color {Red, Black};
    public enum Type {Worker, Warrior};

    public int index;
    public double birthX, birthY;
    public double targetX, targetY;
    public Type type;
    public Color color;
    public int health;
    public int lifeTime;
    public int birthTime;
    public ImageView image;

    public static int n = 0, n_red_warrior = 0, n_red_worker = 0, n_black_warrior = 0, n_black_worker = 0;

    public static double RedAnthillX = 150;
    public static double RedAnthillY = 150;
    public static double BlackAnthillX = 650;
    public static double BlackAnthillY = 450;

    Ant(int index, double x, double y, Type type, Color color, int health,
        int lifeTime, int birthTime) {

        this.index = index;
        this.birthX = x;
        this.birthY = y;
        this.type = type;
        this.color = color;
        this.health = health;
        this.lifeTime = lifeTime;
        this.birthTime = birthTime;

        if (color == Color.Red) {
            targetX = RedAnthillX;
            targetY = RedAnthillY;
        } else {
            targetX = BlackAnthillX;
            targetY = BlackAnthillY;
        }

        image = new ImageView();
        try {
            setImage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image.setFitWidth(64);
        image.setFitHeight(64);
        image.setX(birthX-image.getFitWidth()/2);
        image.setY(birthY-image.getFitHeight()/2);
        //image.toFront();

    }

    public void setImage() throws FileNotFoundException {
        if (color == Color.Red && type == Type.Warrior)
            image.setImage(new Image(new FileInputStream("src/main/resources/labs/red warrior2.png")));
        else if (color == Color.Black && type == Type.Warrior)
            image.setImage(new Image(new FileInputStream("src/main/resources/labs/black warrior2.png")));
        else if (color == Color.Red && type == Type.Worker)
            image.setImage(new Image(new FileInputStream("src/main/resources/labs/red worker2.png")));
        else
            image.setImage(new Image(new FileInputStream("src/main/resources/labs/black worker2.png")));
    }

    public ImageView getImage() {
        return image;
    }

    public static void clearStatics() {
        n = 0;
        n_red_warrior = 0;
        n_red_worker = 0;
        n_black_warrior = 0;
        n_black_worker = 0;
    }

}