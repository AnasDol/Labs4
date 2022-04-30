package labs.model.behaviour;

import labs.model.ants.Ant;

public class WorkerBaseAI extends BaseAI {

    private static boolean workerRunning;
    public static void setRunning(boolean workerRunning) {
        WorkerBaseAI.workerRunning = workerRunning;
    }
    public boolean getRunning() {
        return workerRunning;
    }

    private static int priority;
    public static void setUserPriority(int priority) {
        WorkerBaseAI.priority = priority;
    }

    public WorkerBaseAI() {}

    @Override
    public void calculate(Ant ant) {

        if (ant.type != Ant.Type.Worker) return;

        if (priority!=0) {
            this.setPriority(priority);
            priority = 0;
        }

        double curX = ant.getImage().getX() + ant.getImage().getFitWidth()/2;
        double curY = ant.getImage().getY() + ant.getImage().getFitWidth()/2;

        double deltaX = (ant.targetX-curX);
        double deltaY = (ant.targetY-curY);

        double hypotenuse = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));

        double dX, dY;
        if (hypotenuse >= step) {
            dY = deltaY * step / hypotenuse;
            dX = deltaX * step / hypotenuse;
        } else { // дошли до цели и развернулись
            dY = ant.targetY - curY;
            dX = ant.targetX - curX;
            double temp = ant.targetX;
            ant.targetX = ant.birthX;
            ant.birthX = temp;
            temp = ant.targetY;
            ant.targetY = ant.birthY;
            ant.birthY = temp;

        }


        ant.getImage().setX(curX + dX - ant.getImage().getFitWidth() / 2);
        ant.getImage().setY(curY + dY - ant.getImage().getFitHeight() / 2);

    }


}
