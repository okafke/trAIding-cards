package io.github.okafke.aitcg.card.printing;

import io.github.okafke.aitcg.card.ImageService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class PrintingService {
    public static final MediaSize MEDIA_SIZE = MediaSize.ISO.A6;
    public static final MediaSizeName MEDIA_SIZE_NAME = MEDIA_SIZE.getMediaSizeName();

    private final ImageService imageService;

    @Value("${printer.name:#{null}}")
    private String printer;

    public void print(UUID uuid, BufferedImage image) throws PrintException, IOException, PrinterException {
        PrintService service = getPrintService();
        if (service == null) {
            log.warn("Failed to find printing service " + printer);
            return;
        }

        log.info("Printing card " + uuid);
        DocPrintJob job = service.createPrintJob();
        PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
        printAttributes.add(MEDIA_SIZE_NAME);
        //Doc doc = new SimpleDoc(new ByteArrayInputStream(imageService.toBytes(image)), DocFlavor.INPUT_STREAM.PNG, null);
        Doc doc = new SimpleDoc((Printable) (graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Paper paper = pageFormat.getPaper();
            paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
            pageFormat.setPaper(paper);
            graphics.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            return Printable.PAGE_EXISTS;
        }, DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);
        job.print(doc, printAttributes);

        /*PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        System.out.println(paper.getWidth() + " " + paper.getHeight());
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        pageFormat.setPaper(paper);

        printerJob.setPrintable(new PrintableCardImage(image), pageFormat);
        printerJob.setJobName("Card-" + uuid + ".png");
        printerJob.setPrintService(service);
        //printerJob.print();*/

        /*
        PrinterJob job = PrinterJob.getPrinterJob();
        double dpi = 300.0;
        // Create the paper size of our preference
        double cmPx300 = dpi / 2.54;
        Paper paper = new Paper();
        paper.setSize(21.3 * cmPx300, 29.7 * cmPx300);
        paper.setImageableArea(0, 0, 21.3 * cmPx300, 29.7 * cmPx300);
        PageFormat format = new PageFormat();
        format.setPaper(paper);

        job.pageDialog(format);
        // Assign a new print renderer and the paper size of our choice !
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            double cmPx = dpi / 2.54;
            graphics.drawImage(image.getScaledInstance((int) (4.8 * cmPx), -1, BufferedImage.SCALE_SMOOTH), (int) (cmPx), (int) (cmPx), null);
            return Printable.PAGE_EXISTS;
        }, format);

        /*if (job.printDialog()) {
            try {
                HashPrintRequestAttributeSet set = new HashPrintRequestAttributeSet();
                PrinterResolution pr = new PrinterResolution((int) (dpi), (int) (dpi), ResolutionSyntax.DPI);
                set.add(pr);
                job.setJobName("Jobname");
                job.print(set);
            } catch (PrinterException e) {
            }
        }

        System.out.println(image.getScaledInstance((int) (4.8 * dpi / 2.54), -1, BufferedImage.SCALE_SMOOTH).getClass());
        try (FileOutputStream fos = new FileOutputStream("ignored_images/tzesztjsltkej.png")) {
            //fos.write(imageService.toBytes(image.getScaledInstance((int) (4.8 * dpi / 2.54), -1, BufferedImage.SCALE_SMOOTH)));
        }*/
    }

    private @Nullable PrintService getPrintService() {
        if (printer == null) {
            return null;
        }

        for (PrintService service : PrintServiceLookup.lookupPrintServices(null, null)) {
            if (service.getName().trim().equalsIgnoreCase(printer)) {
                return service;
            }
        }

        return null;
    }

}
