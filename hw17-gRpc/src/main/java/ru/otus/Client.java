package ru.otus;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.atomic.AtomicInteger;
import ru.otus.numbers.*;

public class Client {

    private static final AtomicInteger lastServerValue = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        NumbersServiceGrpc.NumbersServiceStub stub = NumbersServiceGrpc.newStub(channel);

        // Стриминг от сервера
        stub.generateNumbers(
                NumberRequest.newBuilder().setFirstValue(0).setLastValue(30).build(), new StreamObserver<>() {
                    @Override
                    public void onNext(NumberResponse numberResponse) {
                        lastServerValue.set(numberResponse.getValue());
                        System.out.println("new value from server: " + numberResponse.getValue());
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Server stream completed");
                    }
                });

        // Цикл клиента
        int currentValue = 0;
        for (int i = 0; i <= 50; i++) {
            int serverValue = lastServerValue.getAndSet(0); // учитываем только последний, один раз
            currentValue = currentValue + serverValue + 1;
            System.out.println("currentValue: " + currentValue);
            Thread.sleep(600);
        }

        channel.shutdown();
    }
}
