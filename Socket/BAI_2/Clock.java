package Socket.BAI_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class Clock extends JFrame {
    private JLabel timeLabel;

    private Socket socket;
    private BufferedReader serverInPut;
    private PrintWriter serverOutPut;

    public Clock() {
        setTitle("Đồng hồ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new BorderLayout());

        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        add(timeLabel, BorderLayout.CENTER);

        setVisible(true);

        connectServer();

    }

        private void connectServer(){
            try {
                socket = new Socket("localhost", 12345);

             serverInPut = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverOutPut = new PrintWriter(socket.getOutputStream(), true);

             new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (true) {
                            serverOutPut.println("time");
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
    
            }).start();
    
            new Thread(new Runnable() {
    
                @Override
                public void run() {
                    while(true){
                        String time = "" ;
                    try {
                        time = serverInPut.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    String final_time = time;

                    SwingUtilities.invokeLater(() -> { // Update_UI ?
                        timeLabel.setText(final_time);
                    });
                    }
                }
    
            }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
           
        }
        

    

    // private void sendTimeRequestToServer() {
    // try (Socket socket = new Socket("localhost", 12345)) {
    // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    // out.println("time");
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    // private void updateClock() {
    // new Thread(() -> {
    // try (Socket socket = new Socket("localhost", 12345);
    // BufferedReader in = new BufferedReader(new
    // InputStreamReader(socket.getInputStream()))) {

    // while (true) {

    // String time = in.readLine();

    // SwingUtilities.invokeLater(() -> timeLabel.setText(time));
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }).start();
    // }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                new Clock();
            }
        });

    }
}
