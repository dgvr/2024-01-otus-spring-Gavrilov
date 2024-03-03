package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Application Management Commands")
@RequiredArgsConstructor
public class ApplicationManagementCommands {

    private final TestRunnerService testRunnerService;

    private final LocalizedIOService ioService;

    private boolean isReadyToStart = false;

    @ShellMethod(value = "Start test app", key = {"start"})
    @ShellMethodAvailability(value = "isPublishEventCommandAvailable")
    public void start() {
        testRunnerService.run();
    }


    @ShellMethod(value = "Prepare for the test", key = {"prepare", "p"})
    public String prepareForTheTest() {
        isReadyToStart = true;
        return ioService.getMessage("Shell.ready.for.test");
    }


    private Availability isPublishEventCommandAvailable() {
        return isReadyToStart
                ? Availability.available()
                : Availability.unavailable(ioService.getMessage("Shell.not.ready.for.test"));
    }
}
