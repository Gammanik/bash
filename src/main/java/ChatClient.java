import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import java.util.logging.Logger;

public class ChatClient {
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());
    int port;

    public ChatClient(int p) {
        port = p;
    }

    public void start() {
        Channel channel = ManagedChannelBuilder
                .forAddress("localhost", port)
                .usePlaintext()
                .build();

        SenderImpl sender = new SenderImpl(logger);
        sender.send(SenderGrpc.newStub(channel).send(SenderImpl.getObserver(logger)));
    }
}
