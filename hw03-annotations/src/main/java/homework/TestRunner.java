package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;
import homework.dao.SingleTest;
import homework.exceptions.TestConfigException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TestRunner {

    public static void executeTests(Class<?> testClass) {
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
        List<SingleTest> testsList = new ArrayList<>();
        for (var testMethod : testMethods) {
            testsList.add(new SingleTest(beforeMethods, afterMethods, testMethod));
        }
        run(testClass, testsList);
    }

    @SneakyThrows
    private static void run(Class<?> testClazz, List<SingleTest> testsList) {
        var totalTestCounter = 0;
        var passedTestCounter = 0;
        var failedTestCounter = 0;
        log.info("================= START ALL TEST =================");
        Constructor<?> constructor = testClazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        var testInstance = constructor.newInstance();
        for (SingleTest test : testsList) {
            log.info("****** new test *********");
            totalTestCounter++;
            try {
                if (test.getRunMethod() == null) {
                    throw new TestConfigException("Nothing to test. Test method is not specified");
                }
                try {
                    for (var beforeMethod : test.getBeforeMethods()) {
                        beforeMethod.invoke(testInstance);
                    }
                    test.getRunMethod().invoke(testInstance);
                    passedTestCounter++;
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    if (ex instanceof InvocationTargetException) {
                        log.info(((InvocationTargetException) ex)
                                .getTargetException()
                                .getMessage());
                    }
                    throw new RuntimeException(ex);
                } finally {
                    for (var afterMethod : test.getAfterMethods()) {
                        afterMethod.invoke(testInstance);
                    }
                }
            } catch (Exception ex) {
                failedTestCounter++;
            }
        }
        log.info("================= FINISH ALL TESTS =================");
        log.info("All tests have been completed");
        log.info("Statistics: total:" + totalTestCounter + ", passed: " + passedTestCounter + ", failed: "
                + failedTestCounter);
    }
}
