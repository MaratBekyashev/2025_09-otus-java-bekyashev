package homework.dao;

import java.lang.reflect.Method;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleTest {

    private List<Method> beforeMethods;

    private List<Method> afterMethods;

    private Method runMethod;
}
