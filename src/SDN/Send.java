package SDN;

import Root.Sender;


public class Send {
    
    public static void main(String[] args) {
        Sender sn = new Sender(1000);
        sn.start();
    }
}
