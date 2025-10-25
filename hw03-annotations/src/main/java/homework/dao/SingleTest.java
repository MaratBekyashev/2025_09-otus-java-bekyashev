package homework.dao;

import homework.exceptions.RunTestException;
import homework.exceptions.TestConfigException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SingleTest {

    private List<Method> beforeMethods;

    private List<Method> afterMethods;

    private Method runMethod;

    public void run(Class<?> testClazz) {
        try {
            if (runMethod == null) {
                throw new TestConfigException("Nothing to test. Test method is not specified");
            }
            Constructor<?> constructor = testClazz.getDeclaredConstructor();
            var testInstance = constructor.newInstance();

            try {
                for (var beforeMethod : beforeMethods) {
                    beforeMethod.invoke(testInstance);
                }
                runMethod.invoke(testInstance);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            } finally {
                for (var afterMethod : afterMethods) {
                    afterMethod.invoke(testInstance);
                }
            }
        } catch (Exception ex) {
            throw new RunTestException(ex.getMessage());
        }
    }
}
