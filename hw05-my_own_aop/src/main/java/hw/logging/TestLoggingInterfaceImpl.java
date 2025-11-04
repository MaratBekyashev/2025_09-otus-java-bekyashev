package hw.logging;

import hw.annotations.Log;

public class TestLoggingInterfaceImpl implements TestLoggingInterface {

    @Log
    @Override
    public void calculation(int param) {
        System.out.println("Regular method invocated. calculation(int param)");
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        System.out.println("Regular method invocated. calculation(int param1, int param2)");
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println("Regular method invocated. calculation(int param1, int param2, String param3)");
    }
}
