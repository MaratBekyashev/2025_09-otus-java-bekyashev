package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;
import homework.dao.SingleTest;
import homework.exceptions.RunTestException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TestRunner {

    public static void runTests(Class<?> testClass) {
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();

        List<Method> methods = Arrays.stream(testClass.getDeclaredMethods()).toList();

        for (var method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            }

            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }

            if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }
        var totalTestCounter = 0;
        var passedTestCounter = 0;
        var failedTestCounter = 0;
        log.info("================= START =================");
        for (var testMethod : testMethods) {
            totalTestCounter++;
            SingleTest testInstance = new SingleTest();
            testInstance.setBeforeMethods(beforeMethods);
            testInstance.setAfterMethods(afterMethods);
            testInstance.setRunMethod(testMethod);
            try {
                log.info("************** new test ***********************");
                testInstance.run(testClass);
                passedTestCounter++;
            } catch (RunTestException e) {
                failedTestCounter++;
            }
        }
        log.info("================= FINISH =================");
        log.info("All tests have been completed");
        log.info("Statistics: total:" + totalTestCounter + ", passed: " + passedTestCounter + ", failed: "
                + failedTestCounter);
    }
}
