package io.github.okafke.aitcg.cli;

import org.springframework.shell.command.CommandExceptionResolver;
import org.springframework.shell.command.CommandHandlingResult;

public class CommandExceptionResolverImpl implements CommandExceptionResolver {
    @Override
    public CommandHandlingResult resolve(Exception ex) {
        if (ex instanceof CommandException) {
            return CommandHandlingResult.of(ex.getMessage() + '\n');
        }

        return null;
    }

}
