package io.github.okafke.aitcg.card.printing;

import jakarta.annotation.Nullable;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.nio.file.Paths;

public class PrintingTest {
    @SneakyThrows
    public static void main(String[] args) {
        final PrintService printService = getPrintService();
        final BufferedImage image = ImageIO.read(Paths.get("ignored_images/fiery_fridge_two_cards_rotated.png").toFile());
        final PrinterJob printJob = PrinterJob.getPrinterJob();

        PageFormat pageFormat = printJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        //pageFormat.setOrientation(PageFormat.LANDSCAPE);

        //@Cleanup
        //PDDocument document = Loader.loadPDF(Paths.get("ignored_images/testpdf.pdf").toFile());

        printJob.setJobName("MyApp:");
        printJob.setPrintService(printService);
        printJob.setPrintable(new ImagePrintable(pageFormat, image));
        printJob.print();
    }

    private static @Nullable PrintService getPrintService() {
        for (PrintService service : PrintServiceLookup.lookupPrintServices(null, null)) {
            //if (service.getName().trim().equalsIgnoreCase("Microsoft Print to PDF")) {
            if (service.getName().trim().equalsIgnoreCase("Canon SELPHY CP1500")) {
                return service;
            }
        }

        return null;
    }

    public static class ImagePrintable implements Printable {

        private double          x, y, width;

        private int             orientation;

        private BufferedImage   image;

        public ImagePrintable(PageFormat pageFormat, BufferedImage image) {
            this.x = pageFormat.getImageableX();
            this.y = pageFormat.getImageableY();
            this.width = pageFormat.getImageableWidth();
            this.orientation = pageFormat.getOrientation();
            this.image = image;
        }

        @Override
        public int print(Graphics g, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
            System.out.println(pageFormat.getOrientation());
            if (pageIndex == 0) {
                int pWidth = 0;
                int pHeight = 0;
                if (orientation == PageFormat.PORTRAIT) {
                    pWidth = (int) Math.min(width, (double) image.getWidth());
                    pHeight = pWidth * image.getHeight() / image.getWidth();
                } else {
                    pHeight = (int) Math.min(width, (double) image.getHeight());
                    pWidth = pHeight * image.getWidth() / image.getHeight();
                }
                g.drawImage((Image) image, (int) 0, (int) 0, (int) pageFormat.getPaper().getWidth(), (int) pageFormat.getPaper().getHeight(), null);
                return PAGE_EXISTS;
            } else {
                return NO_SUCH_PAGE;
            }
        }

    }

}
