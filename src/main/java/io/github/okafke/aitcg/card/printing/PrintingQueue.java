package io.github.okafke.aitcg.card.printing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

// TODO: solve this with a blocking thread that we notify, instead of polling?
@Getter
@RequiredArgsConstructor
public class PrintingQueue {
    private final Queue<PrintJob> jobs = new LinkedList<>();
    private final URI printerIp;
    private long lastJob;

    public void update() {
        if (System.nanoTime() - lastJob < TimeUnit.SECONDS.toNanos(60)) {
            return;
        }

        PrintJob job = jobs.poll();
        if (job != null) {
            try {
                job.print(printerIp);
            } finally {
                lastJob = System.nanoTime();
            }
        }
    }

    @FunctionalInterface
    public interface PrintJob {
        void print(URI printerIp);
    }

}
