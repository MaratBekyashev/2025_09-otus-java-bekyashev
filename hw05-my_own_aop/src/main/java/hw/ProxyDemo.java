package hw;

import hw.logging.LoggingProxy;
import hw.logging.TestLoggingInterface;
import hw.logging.TestLoggingInterfaceImpl;

public class ProxyDemo {

    public static void main(String[] args) {
        TestLoggingInterface test = (TestLoggingInterface) LoggingProxy.createProxy(new TestLoggingInterfaceImpl());

        for (var i = 0; i < 5; i++) {
            test.calculation(6);

            test.calculation(45, 5);

            test.calculation(1, 3, "qwerty");
        }
    }
}
