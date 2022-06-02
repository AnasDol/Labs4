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

    public Ant (int index, String info) {

        this.index = index;

        String[] arr = info.split(" ");

        this.birthX = Double.parseDouble(arr[0]);
        this.birthY = Double.parseDouble(arr[1]);

        this.targetX = Double.parseDouble(arr[2]);
        this.targetY = Double.parseDouble(arr[3]);

        if (arr[4] == "Warrior") this.type = Type.Warrior;
        else if (arr[4] == "Worker") this.type = Type.Worker;

        if (arr[5] == "Red") this.color = Color.Red;
        else if (arr[5] == "Black") this.color = Color.Black;

        this.health = Integer.parseInt(arr[6]);
        this.lifeTime = Integer.parseInt(arr[7]);
        this.birthTime = Integer.parseInt(arr[8]);

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

    }

    public Ant(int index, double x, double y, Type type, Color color, int health,
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

    public String toString() {
        // -> string format
        String str = "";
        str+=Double.toString(birthX);
        str+=" ";
        str+=Double.toString(birthY);
        str+=" ";
        str+=Double.toString(targetX);
        str+=" ";
        str+=Double.toString(targetY);
        str+=" ";
        str+= type.toString();
        str+=" ";
        str+= color.toString();
        str+=" ";
        str+=Integer.toString(health);
        str+=" ";
        str+=Integer.toString(lifeTime);
        str+=" ";
        str+=Integer.toString(birthTime);
        System.out.println(str);
        return str;
    }

}