package labs.model.behaviour;

import labs.model.ants.Ant;

public class WarriorBaseAI extends BaseAI {

    private static boolean warriorRunning;
    public static void setRunning(boolean warriorRunning) {
        WarriorBaseAI.warriorRunning = warriorRunning;
    }
    public boolean getRunning() {
        return warriorRunning;
    }

    private static int priority;
    public static void setUserPriority(int priority) {
        WarriorBaseAI.priority = priority;
    }

    private double hypotenuse, alpha, a0;
    private long i = 0;
    private boolean f = true;

    public WarriorBaseAI() {}

    @Override
    public void calculate(Ant ant) {

        if (ant.type != Ant.Type.Warrior) return;

        /*if (!warriorRunning) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notify();*/

        if (priority!=0) {
            this.setPriority(priority);
            priority = 0;
        }

        if (f) {
            init(ant);
            f = !f;
        }

        double curX = ant.getImage().getX() + ant.getImage().getFitWidth()/2;
        double curY = ant.getImage().getY() + ant.getImage().getFitWidth()/2;

        double deltaX = (ant.targetX-curX);
        double deltaY = (ant.targetY-curY);

        if (hypotenuse < step/2)  return;

        ant.getImage().setX(ant.targetX + hypotenuse * Math.cos(alpha * i + a0) - ant.getImage().getFitWidth()/2);
        ant.getImage().setY(ant.targetY + hypotenuse * Math.sin(alpha * i + a0) - ant.getImage().getFitHeight()/2);

        i++;

    }

    public void init(Ant ant) {

        double curX = ant.getImage().getX() + ant.getImage().getFitWidth()/2;
        double curY = ant.getImage().getY() + ant.getImage().getFitWidth()/2;

        double deltaX = (ant.targetX-curX);
        double deltaY = (ant.targetY-curY);

        hypotenuse = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
        alpha = 2 * Math.asin(step / (2 * hypotenuse));
        a0 = Math.atan(((ant.birthY - ant.targetY) / (ant.birthX - ant.targetX)));

        if (deltaX > 0) a0+=3.14;

        //System.out.println("hypotenuse: "+hypotenuse);
        //System.out.println("alpha: "+alpha);
        //System.out.println("a0: "+a0);

    }







}

