package labs.model;

import labs.model.ants.Ant;
import labs.model.ants.Warrior;
import labs.model.ants.Worker;
import labs.model.behaviour.WarriorBaseAI;
import labs.model.behaviour.WorkerBaseAI;

import java.util.*;

public final class DataModelManager implements DataModel {

    private static DataModelManager instance;

    public static synchronized DataModelManager getInstance() {
        if (instance == null) {
            instance = new DataModelManager();
        }
        return instance;
    }

    private int Width = 1200, Height = 600;
    private int P1 = 50, P2 = 50;
    private double period = 1.5;
    private int lifeTimeWarrior = 10, lifeTimeWorker = 10;
    private int index=0;

    private Vector<Ant> ants;
    private HashSet<Integer> ids;
    private TreeMap<Integer, Integer> map;

    private ThreadGroup warriorThreadGroup, workerThreadGroup;

    private DataModelManager() {

        ants = new Vector<>();
        ids = new HashSet<>();
        map = new TreeMap<>();

        warriorThreadGroup = new ThreadGroup("warriorThreadGroup");
        workerThreadGroup = new ThreadGroup("workerThreadGroup");

    }

    public void clear(){
        ants.clear();
        ids.clear();
        map.clear();
        index = 0;
    }

    public int getP1() {
        return P1;
    }
    public int getP2() {
        return P2;
    }
    public double getPeriod() {
        return period;
    }
    public int getLifeTimeWarrior(){return lifeTimeWarrior;}
    public int getLifeTimeWorker(){return lifeTimeWorker;}

    public void setP1(int P1) {
        this.P1=P1;
    }
    public void setP2(int P2) {
        this.P2=P2;
    }
    public void setPeriod(double period) {
        this.period = period;
    }
    public void setLifeTimeWarrior(int lifetime){this.lifeTimeWarrior = lifetime;}
    public void setLifeTimeWorker(int lifetime){this.lifeTimeWorker = lifetime;}

    public Vector<Ant> getAnts() {
        return ants;
    }
    public HashSet<Integer> getIds() {return ids;}
    public TreeMap<Integer, Integer> getMap() {return map;}
    public Ant getLast() {
        if (ants.size()>0) return ants.get(ants.size()-1);
        else return null;
    }

    public synchronized ThreadGroup getWarriorThreadGroup(){
        return warriorThreadGroup;
    }

    public synchronized ThreadGroup getWorkerThreadGroup(){
        return workerThreadGroup;
    }

    public void generateAnt(int birthtime) {

        Ant ant;

        int rand1 = (int) (Math.random() * 100);
        int rand2 = (int) (Math.random() * 100);

        // строители генерятся везде
        // воины генерятся на расстоянии до 200 (по осям) от своего муравейника

        double rad = 200;
        double x, y;

        Thread thread;

        if (rand1 > P2 && rand2 > P1) // черный воин
        {
            x = Ant.BlackAnthillX - rad + Math.random()*rad*2;
            y = Ant.BlackAnthillY - rad + Math.random()*rad*2;
            ant = new Warrior(index, x, y, Ant.Color.Black, 100, lifeTimeWorker, birthtime, 20);
            ants.add(ant);
            thread = new Thread(warriorThreadGroup, new WarriorBaseAI(), Integer.toString(ant.index));
        }
        else if (rand1 > P2 && rand2 <= P1) // черный строитель
        {
            x = Math.random() * Width * 0.75;
            y = Math.random() * Height * 0.9 + 15;
            ant = new Worker(index, x, y, Ant.Color.Black, 100, lifeTimeWorker, birthtime);
            ants.add(ant);
            thread = new Thread(workerThreadGroup, new WorkerBaseAI(), Integer.toString(ant.index));
        }
        else if (rand1 < P2 && rand2 > P1) // красный воин
        {
            x = Ant.RedAnthillX - rad + Math.random()*rad*2;
            y = Ant.RedAnthillY - rad + Math.random()*rad*2;
            ant = new Warrior(index, x, y, Ant.Color.Red, 100, lifeTimeWarrior, birthtime, 20);
            ants.add(ant);
            thread = new Thread(warriorThreadGroup, new WarriorBaseAI(), Integer.toString(ant.index));
        }
        else // красный строитель
        {
            x = Math.random() * Width * 0.75;
            y = Math.random() * Height * 0.9 + 15;
            ant = new Worker(index, x, y, Ant.Color.Red, 100, lifeTimeWorker, birthtime);
            ants.add(ant);
            thread = new Thread(workerThreadGroup, new WorkerBaseAI(), Integer.toString(ant.index));
        }

        thread.setDaemon(true);
        thread.start();

        ids.add(index);
        System.out.println(ids.toString());
        map.put(index,birthtime);
        System.out.println(map.toString());
        index++;

    }

    public void addAnts(ArrayList<String> ants_info) {

        for (String info : ants_info) {

            String[] arr = info.split(" ");

            double birthX = Double.parseDouble(arr[0]);
            double birthY = Double.parseDouble(arr[1]);

            double targetX = Double.parseDouble(arr[2]);
            double targetY = Double.parseDouble(arr[3]);

            Ant.Type type;
            if (arr[4].equalsIgnoreCase("Warrior")) type = Ant.Type.Warrior;
            else type = Ant.Type.Worker;

            Ant.Color color;
            if (arr[5].equalsIgnoreCase("Red")) color = Ant.Color.Red;
            else color = Ant.Color.Black;

            int health = Integer.parseInt(arr[6]);
            int lifeTime = Integer.parseInt(arr[7]);
            int birthTime = Integer.parseInt(arr[8]);



            Ant ant;

            int rand1 = (int) (Math.random() * 100);
            int rand2 = (int) (Math.random() * 100);

            // строители генерятся везде
            // воины генерятся на расстоянии до 200 (по осям) от своего муравейника

            double rad = 200;
            double x, y;

            Thread thread;

            if (type == Ant.Type.Warrior && color == Ant.Color.Black) // воин
            {
                ant = new Warrior(index, birthX, birthY, color, health, lifeTime, birthTime, 20);
                ants.add(ant);
                thread = new Thread(warriorThreadGroup, new WarriorBaseAI(), Integer.toString(ant.index));
            }
            else  // строитель
            {
                ant = new Worker(index, birthX, birthY, color, health, lifeTime, birthTime);
                ants.add(ant);
                thread = new Thread(warriorThreadGroup, new WarriorBaseAI(), Integer.toString(ant.index));
            }

            thread.setDaemon(true);
            thread.start();

            ids.add(index);
            System.out.println(ids.toString());
            map.put(index,birthTime);
            System.out.println(map.toString());
            index++;


        }

    }



}
