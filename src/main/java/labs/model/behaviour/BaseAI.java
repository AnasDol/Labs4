package labs.model.behaviour;

import labs.model.DataModelManager;
import labs.model.ants.Ant;

public abstract class BaseAI extends Thread {

    protected Ant ant;
    protected double step = 3;

    public BaseAI() {
        ant = DataModelManager.getInstance().getLast();
    }

    @Override
    public void run() {

        do
        {
            if(!Thread.interrupted())	//Проверка прерывания
            {
                if (getRunning()) {

                    if (ant == null) interrupt();

                    synchronized (DataModelManager.getInstance().getAnts()) {
                        calculate(ant);
                    }

                }
            }
            else
                return;		//Завершение потока

            try{
                Thread.sleep(40);		//Приостановка потока
            }catch(InterruptedException e){
                return;	//Завершение потока после прерывания
            }

        }
        while(true);
    }

   /* @Override
    public void run() {
        do
        {
            if(!Thread.interrupted())	//Проверка прерывания
            {

                for (int j = 0;j<dataModel.getAnts().size();j++) {
                    calculate(dataModel.getAnts().get(j));
                }

                i++;
            }
            else
                return;		//Завершение потока

            try{
                Thread.sleep(40);		//Приостановка потока
            }catch(InterruptedException e){
                return;	//Завершение потока после прерывания
            }
        }
        while(true);
    }*/

    public abstract void calculate(Ant ant);

    public abstract boolean getRunning();

}

