package io.github.okafke.aitcg.card.printing;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PrintingIdService {
    private final AtomicInteger id = new AtomicInteger();

    public synchronized int getPrintingId() {
        return id.getAndIncrement();
    }

}
