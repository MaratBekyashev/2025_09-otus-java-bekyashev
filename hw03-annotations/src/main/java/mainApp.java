import homework.TestRunner;
import homework.dao.TestContainer;

public class mainApp {

    public static void main(String[] args) {

        TestRunner.runTests(TestContainer.class);
    }
}
