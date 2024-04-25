import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatServer extends JFrame
{
    private static ServerSocket serverSocket;
    private static final int PORT = 1234;
    static Socket link = null;

    static String response = "";

    static JButton submit = new JButton("SUBMIT");
    
    static JTextField inputField = new JTextField(20);

    static JTextArea textBox;

    static JScrollPane scrollPane;

    static JPanel mainPanel = new JPanel();

    ChatServer()
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

    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("Opening port...\n");

        try
        {
            serverSocket = new ServerSocket(PORT); 
        }
        catch(IOException ioEx)
        {
            System.out.println( "Unable to attach to port!");

            System.exit(1);
        }

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        ChatServer frame = new ChatServer();
        frame.setTitle("ChatServer");
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
        
        handleChat();
        
    }

    private static void handleChat() 
    {
        Socket link = null;

        try
        {
            link = serverSocket.accept();

            BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);

            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e)
                {
                    if(e.getActionCommand().equals("SUB"))
                    {
                        response = inputField.getText();
                        inputField.setText("");
    
                        if(!response.isBlank())
                        {
                            textBox.append("You>" + response + "\n");
                            output.println(response);
                            response = "";
                        }
    
                    }
                }
            });

            String message = "";
            
            while (link.isClosed() == false)
            {

                if(input.ready())
                {

                    message = input.readLine();

                    if(!message.equals("***CLOSE***") || !link.isClosed())
                    {
                        textBox.append("ChatBud> " + message + "\n");
                    }
                }

                if (message.equals("***CLOSE***") || link.isClosed())
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
            output.println("SERVER CLOSED");
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