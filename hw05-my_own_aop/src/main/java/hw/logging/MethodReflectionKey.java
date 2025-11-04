package hw.logging;

import java.util.Arrays;
import java.util.List;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class MethodReflectionKey {

    private String methodName;

    private List<String> methodArgsTypes;

    public MethodReflectionKey(String methodName, Object[] methodsArgs) {
        this.methodName = methodName;
        this.methodArgsTypes = Arrays.asList(methodsArgs).stream()
                .map(e -> e.getClass().getName())
                .toList();
    }
}
