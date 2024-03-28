package Socket.BAI_2;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;

public class ClientTime extends JFrame {
    private JTextArea clientTextArea;
    private JTextField messageField;
    private JButton sendButton;

    private Socket socket;
    private BufferedReader serverInPut;
    private PrintWriter serverOutPut;

    public ClientTime() {
        setTitle("Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        clientTextArea = new JTextArea();
        clientTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(clientTextArea);
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
            socket = new Socket("localhost", 12345);
            clientTextArea.append("Connected to server.\n");

            serverInPut = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverOutPut = new PrintWriter(socket.getOutputStream(), true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String inputLine;
                    try {
                        while ((inputLine = serverInPut.readLine()) != null) {
                            clientTextArea.append("Server: " + inputLine + "\n");
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
        clientTextArea.append("Client: " + message + "\n");
        serverOutPut.println(message);
        messageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ClientTime();
            }

        });
    }
}
