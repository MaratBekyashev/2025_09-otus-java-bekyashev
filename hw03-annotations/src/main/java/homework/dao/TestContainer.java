package homework.dao;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class TestContainer {

    @Before
    public void setup1() {
        log.info(">> Before each test: SETUP_1");
    }

    @Before
    public void setup2() {
        log.info(">> Before each test: SETUP_2");
    }

    @Test
    public void test1() {
        log.info("успешный запуск test1");
    }

    @Test
    public void test2() {
        log.info("успешный запуск test2");
    }

    @Test
    public void test3() {
        log.info("Запуск с ошибкой: test3");
        throw new RuntimeException("Some error inside the test3");
    }

    @Test
    public void test4() {
        log.info("успешный запуск test4");
    }

    @After
    public void tearDown() {
        log.info(">> After each test tearDown");
    }
}
