package ru.calculator;

/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

/* run performance statistics:
-Xms128m -Xmx128m   Before: spend sec:211/213/202 || After: 6/6/7
-Xms256m -Xmx256m   Before: spend sec:199/193/202 || After: 5/5/5
-Xms784m -Xmx784m   Before: spend sec:187/188/188 || After: 5/5/5
-Xms1024m -Xmx1024m Before: spend sec:192/200/204 || After: 5/5/5
-Xms2028m -Xmx2048m Before: spend sec:205/194/199 || After: 5/5/5

Выполненные доработки:
В классе Data :
 - Integer заменен на int
В классе Summator:
 - SecureRandom.nextInt заменен на ThreadLocalRandom.current().nextInt(); - именно это дает основной прирост производительности
 - При очистке массива дополнительно физическая очистка списка выполняется ((ArrayList) listValues).trimToSize();
  (Хотя это замедляет программу)
 - Тип локальных переменных класса изменен с Integer на int
*/

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcDemo {
    private static final Logger log = LoggerFactory.getLogger(CalcDemo.class);

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        log.info("HEAP MAX SIZE IS: " + Math.round(runtime.maxMemory() / 1024 / 1024) + "Mb");
        long counter = 500_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        for (var idx = 0; idx < counter; idx++) {
            var data = new Data(idx);
            summator.calc(data);

            if (idx % 10_000_000 == 0) {
                log.info("{} current idx:{}", LocalDateTime.now(), idx);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        log.info("PrevValue:{}", summator.getPrevValue());
        log.info("PrevPrevValue:{}", summator.getPrevPrevValue());
        log.info("SumLastThreeValues:{}", summator.getSumLastThreeValues());
        log.info("SomeValue:{}", summator.getSomeValue());
        log.info("Sum:{}", summator.getSum());
        log.info("spend msec:{}, sec:{}", delta, (delta / 1000));
    }
}
