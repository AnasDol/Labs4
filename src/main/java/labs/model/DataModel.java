package labs.model;

import labs.model.ants.Ant;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

public interface DataModel {

    Vector<Ant> getAnts();

    HashSet<Integer> getIds();

    TreeMap<Integer, Integer> getMap();

    void setP1(int P1);

    void setP2(int P2);

    void setPeriod(double period);

    void setLifeTimeWarrior(int lifetime);

    void setLifeTimeWorker(int lifetime);

    void generateAnt(int birthtime);

    void clear();

    double getPeriod();

    int getP1();

    int getP2();

    int getLifeTimeWarrior();

    int getLifeTimeWorker();

    Ant getLast();
}
