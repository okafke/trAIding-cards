package io.github.okafke.aitcg.card.printing;

import lombok.RequiredArgsConstructor;

import java.net.URI;

@RequiredArgsConstructor
public class IppPrintJob implements PrintingQueue.PrintJob {
    private final String documentFormat;
    private final byte[] bytes;

    @Override
    public void print(URI printerIp) {

    }

}
