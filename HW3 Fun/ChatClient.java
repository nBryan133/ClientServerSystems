import java.io.*;
import java.net.*;

public class ChatClient 
{
    private static InetAddress host;
    private static final int PORT = 1234;

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

            BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));

            String message = ""; 
            String response = "";

            System.out.println("Enter messages to speak type ***CLOSE*** to end: ");

            while (link.isClosed() == false)
            {

                while(userEntry.ready())
                {

                    message = userEntry.readLine();

                    output.println(message);
                     
                }

                if(input.ready() == true)
                {

                    response = input.readLine();

                    if(!response.equals("***CLOSE***") || link.isClosed())
                    {
                        System.out.println("User 2> " + response);
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

            System.out.println("User 1> " + message);

            userEntry.close();
            input.close();
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        } 
    }
}
