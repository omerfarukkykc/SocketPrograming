package SDN;
import Root.*;

public class MRouter {
    public static final int A = 1010;
    public static final int B = 1333;
    public static final int C = 1002;
    public static final int D = 1003;
    public static final int E = 1004;
    public static final int F = 1005;
    public static final int G = 1006;
    public static final int H = 1007;
    public static final int X = 1008;
    public static final int Y = 1009;
    public static final int Z = 1000;

    public static void main(String[] args) {
        
        Router r0 = new Router(X,Y);
        Router r1 = new Router(H,Y);
        Router r2 = new Router(G,new int[]{H,X});
        Router r3 = new Router(F,new int[]{G,X});
        Router r4 = new Router(E,new int[]{G,H});
        Router r5 = new Router(D,new int[]{E,F});
        Router r6 = new Router(C,new int[]{D,F});
        Router r7 = new Router(B,new int[]{D,E});
        Router r8 = new Router(A,new int[]{B,C});
        Router r9 = new Router(Z,new int[]{A,B});

        r9.start();
        r8.start();
        r7.start();
        r6.start();
        r5.start();
        r4.start();
        r3.start();
        r2.start();
        r1.start();
        r0.start();


//        Router r5 = new Router(D,new int[]{Y});
//        Router r6 = new Router(C,new int[]{Y});
//        Router r7 = new Router(B,new int[]{Y});
//        Router r8 = new Router(A,new int[]{B,C,D});
//      
//        r8.start();
//        r7.start();
//        r6.start();
//        r5.start();



//        Router r5 = new Router(D,new int[]{Y});
//        Router r6 = new Router(C,new int[]{D});
//        Router r7 = new Router(B,new int[]{C,D});
//        Router r8 = new Router(A,new int[]{B,C,D});
//      
//        r8.start();
//        r7.start();
//        r6.start();
//        r5.start();

    }
}
