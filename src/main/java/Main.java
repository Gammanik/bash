import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter port to to connect to: ");
        String s = sc.nextLine();

        if (s.equals("")) {
            System.out.println("starting server");
            ChatServer server = new ChatServer();
            server.start();
            server.blockUntilShutdown();
        } else {
            System.out.println("starting client");
            ChatClient client = new ChatClient(Integer.parseInt(s));
            client.start();
        }
    }
}
