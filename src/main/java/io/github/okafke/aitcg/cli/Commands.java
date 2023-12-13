package io.github.okafke.aitcg.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.okafke.aitcg.card.AiTCGCard;
import io.github.okafke.aitcg.card.creation.CardAlternationService;
import io.github.okafke.aitcg.card.printing.FileService;
import io.github.okafke.aitcg.card.printing.IppPrintJob;
import io.github.okafke.aitcg.card.printing.PrintingService;
import io.github.okafke.aitcg.card.render.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.ExitRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Quit;
import org.springframework.shell.table.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@ShellComponent
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class Commands implements Quit.Command {
    private final CardAlternationService alternationService;
    private final ApplicationContext applicationContext;
    private final PrintingService printingService;
    private final ImageService imageService;
    private final FileService fileService;

    @SneakyThrows
    @ShellMethod(value = "Evolves the given card", key = "evolve")
    public void evolve(File file) {
        AiTCGCard card = new ObjectMapper().readValue(file, AiTCGCard.class);
        log.info("Creating evolution for " + card.name());
        UUID uuid = UUID.randomUUID();
        alternationService.createEvolution(card.uuid(), uuid, card.conversation()).thenAccept(alternation -> {
            try {
                AiTCGCard evolutionCard = alternation.awaitAiTCGCard(card.stats().increase(25), card.element());
                log.info("Created evolution '" + evolutionCard.name() + "' for " + card.name());
                byte[] png = imageService.createPNG(evolutionCard);
                fileService.save(evolutionCard, png);
            } catch (IOException | ExecutionException | InterruptedException e) {
                log.error("Failed to produce evolution for " + card.name());
            }
        });
    }

    @ShellMethod(value = "Lists print jobs", key = "jobs")
    public Table jobs() {
        TableModel model = new BeanListTableModel<>(printingService.getPrintingHistory(), "id", "failed", "info", "printer", "ran");
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addFullBorder(BorderStyle.air);
        tableBuilder.addHeaderBorder(BorderStyle.air);
        tableBuilder.addInnerBorder(BorderStyle.air);
        return tableBuilder.build();
    }

    @ShellMethod(value = "Prints the job with the given id", key = "print")
    public String print(int id) {
        IppPrintJob job = printingService.getPrintingHistory().stream().filter(j -> j.getId() == id).findFirst().orElseThrow(() -> new CommandException("Failed to find PrintJob with id " + id));
        printingService.print(job, true);
        return "Reprinting job " + job;
    }

    @SneakyThrows
    @ShellMethod(value = "Prints a file", key = "printfile")
    public String printFile(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] bytes = inputStream.readAllBytes();
            printingService.printCardJpeg("Command " + file, printingService.getPrintingIdService().getPrintingId(), bytes);
        }
        return "Printing file " + file;
    }

    @ShellMethod(value = "Exits the application.", key = {"quit", "exit"})
    public void exit() {
        SpringApplication.exit(applicationContext, () -> 0);
        throw new ExitRequest();
    }

}
