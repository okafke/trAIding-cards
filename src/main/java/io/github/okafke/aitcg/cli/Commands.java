package io.github.okafke.aitcg.cli;

import io.github.okafke.aitcg.card.printing.IppPrintJob;
import io.github.okafke.aitcg.card.printing.PrintingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.ExitRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Quit;
import org.springframework.shell.table.*;

@ShellComponent
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class Commands implements Quit.Command {
    private final ApplicationContext applicationContext;
    private final PrintingService printingService;

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

    @ShellMethod(value = "Exits the application.", key = {"quit", "exit"})
    public void exit() {
        SpringApplication.exit(applicationContext, () -> 0);
        throw new ExitRequest();
    }

}
