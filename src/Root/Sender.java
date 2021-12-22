package Root;
import java.io.*;
import java.net.*;
import java.util.*;

public class Sender extends Thread {
    private InetAddress host;
    private final int PORT;
    private Socket link = null;
    Scanner input;
    PrintWriter output;
    public Sender(int PORT) {
        this.PORT = PORT;
        this.serviceStart(PORT);
    }

    @Override
    public void run() {
        try {
            while (true) {                
                this.accessServer();
            }
            
        } 
        catch (IOException ioEx) 
        {
            ioEx.printStackTrace();
        } 
        finally 
        {
            try 
            {
                System.out.println("\n* Closing connections (Sender side)*");
                output.println("***CLOSE***");
                link.close();
            } 
            catch (IOException ioEx) 
            {
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
        }
    }
    
    private void accessServer() throws IOException {
            
            
        System.out.println("How many packets? ");
        Scanner userEntry = new Scanner(System.in);
        String message, str2, response;
        int number;

        response = userEntry.nextLine();
        number = Integer.parseInt(response);
        int counter = 0, attempt = 0;
        long startTime = System.nanoTime();
        do {

            message = "PCK";
            this.sendMessage(message + counter);
            attempt++;
            
            link.setSoTimeout(4000); // Setting timer for sender

            String request = this.getRequest();

            str2 = request.substring(0, 3);

            while (!str2.equals("ACK")) {
                System.out.println(message + counter + " Resending...");
                output.println(message + counter);
                attempt++;
                request = input.nextLine();
                str2 = request.substring(0, 3);
            }

            System.out.println(request + " received from receiver successfully");
            counter++;
        } while (counter < number);
        long endTime = System.nanoTime();
        System.out.println("Total number of try: " + attempt);
        System.out.println("System afficiency: " + (double)number/attempt);
        System.out.println("Time taken to send all packets: " +(endTime - startTime) + " nano seconds.");
        
    }
    public void sendMessage(String message){
        output.println(message);
    }
    public String getRequest(){
        while (true) {            
            if (input.hasNext()) {
            return input.nextLine();
            }
        }
    }
    public void serviceStart(int PORT) {
        try {
            
            host = InetAddress.getLocalHost();
            link = new Socket(host, PORT);
            input = new Scanner(link.getInputStream());
            output = new PrintWriter(link.getOutputStream(), true);
        } catch (Exception uhEx) {
            System.exit(1);
        }
    }
}