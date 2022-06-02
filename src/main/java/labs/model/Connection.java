package labs.model;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import labs.viewmodel.HabitatViewModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Connection {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 3443;

    private Socket clientSocket;
    private Scanner inMessage;
    private PrintWriter outMessage;

    private DataModel dataModel;
    private HabitatViewModel viewModel;

    private Thread thread;

    private String username;

    private ArrayList<String> userList = new ArrayList<>();

    public Connection(String username, DataModel dataModel, HabitatViewModel viewModel) {

        this.username = username;
        this.dataModel = dataModel;
        this.viewModel = viewModel;

        try {
            // подключаемся к серверу
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            alarm(1, "Не удалось подсоединиться к серверу");
        }

        // сообщаем, как нас зовут
        sendMessage("##username##");
        sendMessage(username);


        thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        // если есть входящее сообщение
                        if (inMessage.hasNext()) {
                            // считываем его
                            String inMes = inMessage.nextLine();
                            System.out.println(inMes);

                            if (inMes.equalsIgnoreCase("##request##")) {
                                String receiver = waitForMessage(); // кому
                                int N = Integer.parseInt(waitForMessage()); // сколько


                                ArrayList<String> ants_info = new ArrayList<>(); // что
                                for (int i=0;i<N;i++) {
                                    ants_info.add(dataModel.getAnts().get(new Random().nextInt(dataModel.getAnts().size())).toString());
                                }

                                sendMessage("##response##");
                                sendMessage(receiver); // кому
                                for(String info:ants_info) {
                                    sendMessage(info);
                                }
                                sendMessage("##response##end##");

                            }

                            else if (inMes.equalsIgnoreCase("##response##")) {

                                String receiver = waitForMessage(); // от кого

                                ArrayList<String> ants_info = new ArrayList<>(); // что
                                String mes = waitForMessage();
                                while (!Objects.equals(mes, "##response##end##")) {
                                    ants_info.add(mes); // добавление сформированной клиентом строчки с информацией
                                    mes = waitForMessage();
                                }

                                dataModel.addAnts(ants_info);

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewModel.updateImages();
                                    }
                                });

                            }

                            else if (inMes.equalsIgnoreCase("##userlist##")) {

                                userList.clear(); // что
                                String mes = waitForMessage();
                                while (!Objects.equals(mes, "##userlist##end##")) {
                                    if (!Objects.equals(mes, username)) userList.add(mes); // добавление сформированной клиентом строчки с информацией
                                    mes = waitForMessage();
                                }
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewModel.updateUserList(userList);
                                    }
                                });


                            }

                            else if (inMes.equalsIgnoreCase("##username##error##")) {
                                //username = Integer.toString(new Random().nextInt());
                                alarm(1, "name already in use");
                            }

                            else if (inMes.equalsIgnoreCase("##error##")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        alarm(1, "Пользователь с указанным именем не найден");
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {e.printStackTrace();}
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public void close() {

        // отправляем служебное сообщение, которое является признаком того, что клиент вышел из чата
        sendMessage("##session##end##");
        thread.interrupt();
        outMessage.close();
        inMessage.close();
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String msg) {
        outMessage.println(msg);
        outMessage.flush();
    }


    public void alarm(int type, String msg) {

        // жалуется и ворчит

        Alert alert;

        if (type == 0) {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Pause");
            alert.setHeaderText("Statistics: ");
        }
        else alert = new Alert(Alert.AlertType.ERROR);


        alert.setContentText(msg);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.CANCEL) {
            System.out.println("cansel pressed");
        }


    }

    public String waitForMessage() {
        String clientMsg = "";
        while(true){
            if (inMessage.hasNext()) {
                clientMsg = inMessage.nextLine();
                break;
            }
        }
        System.out.println("receiving: " + clientMsg);
        return clientMsg;
    }

    public ArrayList<String> getUserList() {

        return userList;

    }


    public String getUsername() {
        return username;
    }
}
