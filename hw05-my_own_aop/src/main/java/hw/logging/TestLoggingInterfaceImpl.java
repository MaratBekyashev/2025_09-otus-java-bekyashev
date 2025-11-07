package hw.logging;

import hw.annotations.Log;

public class TestLoggingInterfaceImpl implements TestLoggingInterface {

    @Log
    @Override
    public void calculation(int param) {
        System.out.println("Original method invocated. calculation(int param)");
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        System.out.println("Original method invocated. calculation(int param1, int param2)");
    }

    @Override
    public void methodWithoutLogging(int param) {
        System.out.println("Original method invocated. methodWithoutLogging(int param)");
    }
}
