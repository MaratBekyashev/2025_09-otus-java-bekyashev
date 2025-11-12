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
-Xms8m -Xmx8m       Before:  231  || After: 9
-Xms16m -Xmx16m     Before:  192  || After: 6
-Xms32m -Xmx32m     Before:  181  || After: 5
-Xms64m -Xmx64m     Before:  174  || After: 5
-Xms128m -Xmx128m   Before:  174  || After: 5
-Xms256m -Xmx256m   Before:  182  || After: 5
-Xms512m -Xmx512m   Before:  173  || After: 5
-Xms784m -Xmx784m   Before:  172  || After: 5
-Xms1024m -Xmx1024m Before:  172  || After: 5
-Xms2028m -Xmx2048m Before:  173  || After: 5

Выполненные доработки:
В классе Data :
 - Integer заменен на int
В классе Summator:
 - SecureRandom.nextInt заменен на ThreadLocalRandom.current().nextInt(); - именно это дает основной прирост производительности
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
