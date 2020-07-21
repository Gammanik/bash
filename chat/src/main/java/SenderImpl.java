import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderImpl extends SenderGrpc.SenderImplBase {
    Logger logger;

    public SenderImpl(Logger lg) {
        logger = lg;
    }

    public static StreamObserver<Message> getObserver(Logger logger) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Message value) {
                logger.info(value.getUser() + ": " + value.getText()); // todo: show time & color change
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.SEVERE, t.toString());
            }

            @Override
            public void onCompleted() {
                logger.info("Chat finished");
            }
        };
    }

    @Override
    public StreamObserver<Message> send(StreamObserver<Message> responseObserver) {
        logger.info("Start chat session");

        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            String s;
            while (!(s = sc.nextLine()).trim().equals("q")) {
                Message msg = Message.newBuilder()
                        .setDatetime((int) Instant.now().getEpochSecond())
                        .setUser("client") // todo: fix
                        .setText(s)
                        .build();
                responseObserver.onNext(msg);
            }
        }).start();

        return getObserver(logger);
    }



}
