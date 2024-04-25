import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient extends JFrame
{
    private static InetAddress host;
    private static final int PORT = 1234;
    static Socket link = null;

    static String message = ""; 

    static JButton submit = new JButton("SUBMIT");
    
    static JTextField inputField = new JTextField(20);

    static JTextArea textBox;

    static JScrollPane scrollPane;

    static JPanel mainPanel = new JPanel();

    ChatClient()
    {
        
        submit.setActionCommand("SUB");

        textBox = new JTextArea(23, 25);
        textBox.setEditable(false);

        scrollPane = new JScrollPane(textBox);

        mainPanel.add(inputField);
        mainPanel.add(submit);

        add(mainPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);  
        
    }


    public static void main(String[] args) throws IOException
    {

        try
        {
            host = InetAddress.getLocalHost();

        }
        catch(UnknownHostException uhEx)
        {
            System.out.println("Host ID not found!");
            System.exit(1);
        }

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        ChatClient frame = new ChatClient();
        frame.setTitle("ChatClient");
        frame.setSize(d.width/2 ,d.height/2);
        frame.setVisible(true);
        frame.setResizable(false);
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
        
        chatFunction();

    }

    private static void chatFunction()
    {
        Socket link = null;

        try 
        {
            link = new Socket(host,PORT);
            
            BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));

            PrintWriter output = new PrintWriter(link.getOutputStream(),true); 

            String response = "";
            
            System.out.println("Enter messages to speak type ***CLOSE*** to end: ");

            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e)
                {
                    if(e.getActionCommand().equals("SUB"))
                    {
                        message = inputField.getText();
                        inputField.setText("");
    
                        if(!message.isBlank())
                        {
                            textBox.append("You>" + message + "\n");
                            output.println(message);
                            message = "";
                        }
    
                    }
                }
            });

            while (link.isClosed() == false)
            {

                if(input.ready())
                {

                    response = input.readLine();

                    if(!response.equals("***CLOSE***") || link.isClosed())
                    {
                        textBox.append("Chatbud>" + response + "\n");
                    }
                }
                
                if(response.equals("***CLOSE***") || link.isClosed())
                {
                    try 
                    {
                        output.println("***CLOSE***");
                        link.close(); 
                    }
                    catch(IOException ioEx)
                    {
                        System.out.println("Unable to disconnect!");
                        System.exit(1);
                    }
                }
                
            }

            input.close();
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        } 
        finally
        {
            try 
            {
                link.close();
            }
            catch(IOException ioEx)
            {
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
        }
    }
}
