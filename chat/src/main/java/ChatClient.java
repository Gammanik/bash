import io.grpc.Channel;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloRequest;

import java.time.Instant;
import java.util.logging.Logger;

public class ChatClient implements ChatInterface {
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());
    private final SenderGrpc.SenderBlockingStub blockingStub;

    public ChatClient(Channel channel) {
        blockingStub = SenderGrpc.newBlockingStub(channel);
    }

    @Override
    public void sendMessage(String text) {
        logger.info("Sending " + text + " ...");

        int unixTimestamp = (int) Instant.now().getEpochSecond();
        MessageRequest req = MessageRequest.newBuilder()
                .setUser("usr")
                .setDatetime(unixTimestamp)
                .setText(text)
                .build();

        MessageReply res = blockingStub.sendMessage(req);
        logger.info("Got result: " + res.getMessage());
    }
}
