import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BinToHexClient extends JFrame implements ActionListener{

    private static InetAddress host;
    private static final int PORT = 1234;
    private static Socket link = null;

    public static void main(String[] args) throws IOException
    {
        String ip = "174.242.38.52";
        byte[] ipAd = {(byte) 174,(byte) 242,38,52};
        try
        {
            host = InetAddress.getLocalHost();
            //host = InetAddress.getByAddress(ipAd);

            System.out.println("Made it here...");
            link = new Socket(host,PORT);
            System.out.println("Made it here...");

        }
        catch(UnknownHostException uhEx)
        {
            System.out.println("Host ID not found!");
            System.exit(1);
        }

        BinToHexClient frame = new BinToHexClient();
        frame.setSize(400,300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getRootPane().setDefaultButton(submit);

        frame.addWindowListener( 
            new WindowAdapter()
            {
                public void windowClosing( 
                    WindowEvent event)
                    {
                        if (link != null)
                        {
                            try 
                            {
                                link.close();
                            }
                            catch (IOException ioEx)
                            {
                                System.out.println("\nUnable to close link\n");
                                System.exit(1);
                            }
                        }
                        System.exit(0);
                    }
            }
        );

    }

    static JButton submit = new JButton("SUBMIT");
    
    static JTextField inputField = new JTextField(20);

    static JTextArea textBox = new JTextArea();

    static JPanel mainPanel = new JPanel();
    static JPanel outputPanel = new JPanel();

    BinToHexClient()
    {
        
        submit.setActionCommand("SUB");
        submit.addActionListener(this);

        mainPanel.add(inputField);
        mainPanel.add(submit);

        outputPanel.add(textBox);

        add(mainPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);


        
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        try
        {

            Scanner input = new Scanner(link.getInputStream());

            PrintWriter output = new PrintWriter(link.getOutputStream(),true);

            String message = "";
            String response;

            boolean legal = false;

            if(e.getActionCommand().equals("SUB"))
            {
                        
                        
                message = inputField.getText();

                for(int i = 0; i < message.length(); i++)
                {
                    if(!(message.charAt(i) == '1' || message.charAt(i) == '0'))
                    {
                        legal = false;
                    }
                    else{
                        legal = true;
                    }
                }

                if(legal == false)
                {
                    textBox.setText(null);
                    textBox.append("Please only use binary!");
                }
                else if(legal == true)
                {
                    output.println(message);
                    response = input.nextLine();
                    textBox.setText(null);
                    textBox.append("SERVER> " + response);
                }
                    
                
            }

            if(inputField.getText() == "CLOSE")
            {
                input.close();
            }

        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        }

        finally
        {
            try
            {
                if(inputField.getText() == "CLOSE")
                {
                    System.out.println( "\n* Closing conneciton... *");

                    link.close();
                }
            }
            catch(IOException ioEx)
            {
                if(inputField.getText() == "CLOSE")
                {
                    System.out.println("Unable to disconnect!");
                
                    System.exit(1);
                }
            }
        }
    }
}
