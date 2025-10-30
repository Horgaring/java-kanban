import manager.Managers;
import server.HttpTaskServer;

public class Main {
    public static void main(String[] args) throws Exception {
       var a =  new HttpTaskServer(Managers.getDefault());

        a.start();

        Thread.sleep(5000);

        a.stop();
    }
}
