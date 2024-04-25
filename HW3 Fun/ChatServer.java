import java.io.*;
import java.net.*;

public class ChatServer
{
    private static ServerSocket serverSocket;
    private static final int PORT = 1234;

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
            BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));

            String message = "";
            String response = "";

            System.out.println("Press enter to send messages type ***CLOSE*** to end: ");

            while (link.isClosed() == false)
            {

                while(userEntry.ready())
                {
                    response = userEntry.readLine();
                    
                    output.println(response);
                    
                }
                
                if(input.ready() == true)
                {

                    message = input.readLine();

                    if(!message.equals("***CLOSE***") || !link.isClosed())
                    {
                        System.out.println("User 1> " + message);
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
    }
}