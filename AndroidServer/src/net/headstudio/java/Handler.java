package net.headstudio.java;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
 
/**
 * @author germaNRG
 *
 */
public class Handler implements Runnable {
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private JTextArea screenArea;
 
    public Handler(Socket client, JTextArea area) {
        this.client = client;
        this.screenArea = area;
    }
    
    public synchronized void stop(){
        client = null;
        out = null;
        in = null;
        screenArea = null;
    }

    public void run() {
        while (true) {
            try {
            	showMessage("--getStreams");
                getStreams();
                showMessage("++getStreams");
                showMessage("--processConnection");
                processConnection();
                showMessage("++processConnection");
            } catch (IOException e) {
                e.printStackTrace();
            	try {
                    showMessage("\n"
                                + (String) client.getInetAddress().getHostName()
                                + " turn off.");
                    out.close();
                    in.close();
                    client.close();
                    stop();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        }
    }
 

    private void getStreams() throws IOException, ClassNotFoundException {
        out = new ObjectOutputStream(client.getOutputStream());
        out.flush();  
        out.writeObject("+Server: Msg Succesfully send");
        in = new ObjectInputStream(client.getInputStream());
        String o = (String)in.readObject();
        showMessage("\nI/O streams were been received\n" + o + "\n");
    }
 
    private void processConnection() throws IOException {
        String message = "";
 
        // Enable input text
        // sendMessage()

        do {
            try {
                message = (String) client.getInetAddress().getHostName() + " dice: "
                              + in.readObject();
                showMessage("\n" + message);
            } catch (ClassNotFoundException e) {}
        } while (!message.equals("cend"));
 
        } 
    
    private void closeConnection() {
    	
        // showMessage("\nClosing connection\n");
        // disable input text
    	
        try {
            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}