import java.io.*;
import java.net.*;
import java.util.*;

public class TCPEchoServer
{
    private static ServerSocket serverSocket;
    private static final int PORT = 1234;

    public static void main(String[] args)
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

        do{
            handleClient();
        }while(true);
    }

    private static void handleClient()
    {
        Socket link = null;

        try
        {
            link = serverSocket.accept();

            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);

            String message = input.nextLine();

            int bin;
            String hex;

            while (!message.equals("CLOSE"))
            {
                System.out.println("Message received.");

                bin = Integer.parseInt(message, 2);
                hex = Integer.toHexString(bin);

                output.println("Hex: " + hex);

                message = input.nextLine();
            }
            
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
                System.out.println("\n* Closing connection... *");

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