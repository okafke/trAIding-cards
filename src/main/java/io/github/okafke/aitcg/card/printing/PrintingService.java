package io.github.okafke.aitcg.card.printing;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Setter
@Service
public class PrintingService {
    public static final String JPEG_FORMAT = "image/jpeg";

    private final Set<IppPrintJob> printingHistory = new LinkedHashSet<>();
    private final PrintingIdService printingIdService;
    private final List<Printer> printers;

    private @Nullable Printer cardPrinter;

    @Autowired
    public PrintingService(@Value("${printing.printers}") String[] printers,
                           @Value("${printing.card.printer:#{null}}") @Nullable String cardPrinter,
                           PrintingIdService printingIdService) {
        this.printers = Arrays.stream(printers).map(printerIp -> new Printer(URI.create(printerIp))).toList();
        this.cardPrinter = cardPrinter == null ? null : new Printer(URI.create(cardPrinter));
        this.printingIdService = printingIdService;
        log.info("Initialized Printing Service with printers " + this.printers);
    }

    // TODO: solve with blocking thread that we notify about printing job?
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedDelay = 2)
    public void updateQueues() {
        printers.forEach(Printer::update);
    }

    public void printCardJpeg(String info, byte[] jpegBytes) {
        Printer printer = cardPrinter != null ? cardPrinter : getEmptiestPrinter();
        print(printingIdService.getPrintingId(), info, printer, JPEG_FORMAT, jpegBytes);
    }

    public void print(int id, @Nullable URI printerIp, String documentFormat, byte[] bytes) {
        Printer printer = printerIp == null ? getEmptiestPrinter() : getPrinter(printerIp);
        print(id, null, printer, documentFormat, bytes);
    }

    public void print(int id, @Nullable String info, @Nullable Printer printer, String documentFormat, byte[] bytes) {
        if (printer == null) {
            log.info("Failed to print " + id + " printer did not exist!");
            return;
        }

        IppPrintJob printJob = new IppPrintJob(documentFormat, printer, bytes, id, info == null ? "" : info);
        print(printJob, false);
    }

    public void print(IppPrintJob job, boolean addFirst) {
        if (addFirst) {
            job.getPrinter().getJobs().addFirst(job);
        } else {
            job.getPrinter().getJobs().add(job);
        }

        printingHistory.add(job);
    }

    private @Nullable Printer getPrinter(URI printerIp) {
        return printers.stream().filter(p -> printerIp.equals(p.getPrinterIp())).findFirst().orElse(null);
    }

    private @Nullable Printer getEmptiestPrinter() {
        return printers.stream().min(Comparator.comparingInt(p -> p.getJobs().size())).orElse(null);
    }

}
