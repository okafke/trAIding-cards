package io.github.okafke.aitcg.card.printing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

// TODO: solve this with a blocking thread that we notify, instead of polling?
@Slf4j
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class Printer {
    private final Deque<IppPrintJob> jobs = new LinkedList<>();
    private final URI printerIp;
    private long lastJob;

    public synchronized void update() {
        if (System.nanoTime() - lastJob < TimeUnit.SECONDS.toNanos(70)) {
            return;
        }

        IppPrintJob job = jobs.poll();
        if (job != null) {
            log.info("Found non-null print job");
            try {
                job.print();
                if (job.isFailed() && job.getRan() < 3) {
                    log.info("Readding job " + job.getId() + " a " + (job.getRan() + 1) + " time.");
                    jobs.addFirst(job);
                }
            } finally {
                lastJob = System.nanoTime();
            }
        }
    }

}
