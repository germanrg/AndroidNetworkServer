package net.headstudio.java;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * @author germaNRG
 *
 */
public class Server extends JFrame {

     private static final long serialVersionUID = 1L;
     private JTextField inputText;
     private JTextArea screenArea;
     private ServerSocket server;
     private Socket connection;
     private int n = 1;

     // GUI
     public Server() {
         super("Server");
         showMessage("++main");

         Container container = getContentPane();
         inputText = new JTextField();
         inputText.setEditable(false);
         inputText.addActionListener(new ActionListener() {
        	 // Send message to client
        	 public void actionPerformed(ActionEvent evento) {
        		 inputText.setText("message for the android client");
             }
         });

         container.add(inputText, BorderLayout.NORTH);

         // Create screenarea
         screenArea = new JTextArea();
         container.add(new JScrollPane(screenArea), BorderLayout.CENTER);

         setSize(300, 150);
         setVisible(true);
         screenArea.setEditable(false);
     }

     // Configure and execute server
     public void exeServer() {
         try {
             // Step 1: Create ServerSocket.
             server = new ServerSocket(12345, 100);
             showMessage("++newServer");
             while (true) {
            	 try {
                     // Step 2: Wait for conn.
                     showMessage("\nWaiting for connection...\n");
                     connection = server.accept(); 

                     Runnable newClient = new Handler(connection, screenArea);
                     Thread thread = new Thread(newClient);
                     thread.start();
                     showMessage("Connection " + n + " from: "
                                     + connection.getInetAddress().getHostName());
                     // getIOStreams(); // Step 3: Get in&out streams
                     // processConnection(); // Step 4: Process connection
                 }

                 // EOFException: Client finish connection
                 catch (EOFException e) {
                     System.err.println("The connection has finished by the server.");
                 }

                 //finally {
                 //      newClient.closeConnection(); // Step 5: Close connection
                 //      ++n;
                 //}

             }

         } // try

         // I/O Problems
         catch (IOException e) {
             e.printStackTrace();
         }

     }

     private void inputTextEditable(final boolean editable) {
         SwingUtilities.invokeLater(new Runnable() { 
             public void run(){
                 inputText.setEditable(editable);
             }
         });
     }

     private void showMessage(final String msgToShow) {
         SwingUtilities.invokeLater(new Runnable() {
             public void run(){
                 screenArea.append(msgToShow);
                 screenArea.setCaretPosition(screenArea.getText()
                                .length());
             }
         });
     }
     
     public static void main(String args[]) {
         JFrame.setDefaultLookAndFeelDecorated(true);
         Server aplicacion = new Server();
         aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         aplicacion.exeServer();
     }

}