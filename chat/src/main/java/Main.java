import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter port to to connect to: ");
        String s = sc.nextLine();

        ChatClient client = null;

        if (s.equals("")) {
            System.out.println("start server");
            final ChatServer server = new ChatServer();
            server.start();
            server.blockUntilShutdown();
        } else {
            System.out.println("start client");
            String user = "nik";
            String target = "localhost:50051";

            ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                    .usePlaintext()
                    .build();
            client = new ChatClient(channel);
        }

        System.out.println("11 ~ You entered string "+s);
        while ( !(s = sc.nextLine()).trim().equals("")) {
            System.out.println("You entered string " + s);

            if (client != null) {
                client.sendMessage(s);
            }
        }
    }
}
