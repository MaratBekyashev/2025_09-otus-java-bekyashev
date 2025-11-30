package hw;

import hw.model.LoggingProxy;
import hw.model.TestLoggingInterface;
import hw.model.TestLoggingInterfaceImpl;

public class ProxyDemo {

    public static void main(String[] args) {
        TestLoggingInterface test = (TestLoggingInterface) LoggingProxy.createProxy(new TestLoggingInterfaceImpl());

        for (var i = 0; i < 3; i++) {
            System.out.println("******");
            test.calculation(6 * i);
            test.calculation(45, 5 * i);
            test.methodWithoutLogging(55);
        }
    }
}
