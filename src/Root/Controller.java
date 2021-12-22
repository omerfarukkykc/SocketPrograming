package Root;
import java.io.*;
import java.util.*;
import java.net.*;


public class Controller 
{
    public Controller(int PORT) 
    {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(PORT);
        } 
        catch (Exception e){}
        
        while (true)
        {
            Socket s = null;
            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                Scanner dis = new Scanner(s.getInputStream());
                PrintWriter dos = new PrintWriter(s.getOutputStream(),true);
                dos.println(Boolean.TRUE);

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                
                try {
                    s.close();
                } catch (IOException ex){}
                    e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread
{
    final Scanner dis;
    final PrintWriter dos;
    final Socket s;

    // Constructor
    public ClientHandler(Socket s, Scanner dis, PrintWriter dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run()
    {
        String received="";
        String toreturn;
        while (true)
        {
            try {
                // receive the answer from client
                do{
                    
                    if(dis.hasNext()){
                        received = dis.nextLine();
                    }
                }
                while (!dis.hasNext());
                
                if(received.equals("exit"))
                {
                        System.out.println("Client " + this.s + " sends exit...");
                        System.out.println("Closing this connection.");
                        this.s.close();
                        System.out.println("Connection closed");
                        break;
                }
                switch (received) {
                    default:
                            dos.println(true);
                            break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // closing resources
        this.dis.close();
        this.dos.close();
    }
}
