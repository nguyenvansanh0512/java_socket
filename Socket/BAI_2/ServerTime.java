package Socket.BAI_2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class ServerTime extends JFrame {
    private JTextArea serverTextArea;
    private JTextField messageField;
    private JButton sendButton;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader clientInPut;
    private PrintWriter clientOutPut;

    public ServerTime() {
        setTitle("Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        serverTextArea = new JTextArea();
        serverTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(serverTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        messageField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }

        });

        setVisible(true);

        try {
            serverSocket = new ServerSocket(12345);
            clientSocket = serverSocket.accept();
            serverTextArea.append("Client connected.\n");

            clientInPut = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOutPut = new PrintWriter(clientSocket.getOutputStream(), true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String inputLine;
                    try {
                        while ((inputLine = clientInPut.readLine()) != null) {
                            serverTextArea.append("Client: " + inputLine + "\n");
                            if (inputLine.equals("time")) {
                                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                                Date currentTime = new Date();
                                String time = formatter.format(currentTime);
                                clientOutPut.println(time);
                                
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        serverTextArea.append("Server: " + message + "\n");
        clientOutPut.println(message);
        messageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ServerTime();
            }

        });
    }
}
