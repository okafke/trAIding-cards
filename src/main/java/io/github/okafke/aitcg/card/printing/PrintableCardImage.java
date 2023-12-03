package io.github.okafke.aitcg.card.printing;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;

@RequiredArgsConstructor
public class PrintableCardImage implements Printable {
    private final BufferedImage image;

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Paper paper = pageFormat.getPaper();
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        pageFormat.setPaper(paper);

        graphics.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
        double pageWidth = pageFormat.getImageableWidth();
        double pageHeight = pageFormat.getImageableHeight();

        System.out.println(pageFormat.getImageableHeight() + " vs " + image.getHeight());

        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        double scaleY = pageHeight / imageHeight;
        double newWidth = imageWidth * scaleY;
        double x = (pageWidth - newWidth) / 2;
        graphics.drawImage(image, (int) x, 0, (int) newWidth, (int) pageHeight, null);
        return PAGE_EXISTS;
    }

}
