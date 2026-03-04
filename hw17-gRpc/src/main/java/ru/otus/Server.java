package ru.otus;

import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import ru.otus.numbers.*;

public class Server {

    public static void main(String[] args) throws Exception {
        io.grpc.Server server = ServerBuilder.forPort(50051)
                .addService(new NumbersServiceImpl())
                .build()
                .start();

        System.out.println("Server started on port 50051");

        server.awaitTermination();
    }

    static class NumbersServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {
        @Override
        public void generateNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
            int first = request.getFirstValue();
            int last = request.getLastValue();

            new Thread(() -> {
                        try {
                            for (int i = 1; i <= last - first; i++) {
                                int value = first + i;
                                NumberResponse response = NumberResponse.newBuilder()
                                        .setValue(value)
                                        .build();
                                responseObserver.onNext(response);
                                TimeUnit.SECONDS.sleep(2);
                            }
                            responseObserver.onCompleted();
                        } catch (InterruptedException e) {
                            responseObserver.onError(e);
                        }
                    })
                    .start();
        }
    }
}
