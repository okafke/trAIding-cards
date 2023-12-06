package io.github.okafke.aitcg.api;

import io.github.okafke.aitcg.card.printing.PrintingIdService;
import io.github.okafke.aitcg.card.printing.PrintingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/printing")
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class PrintingEndpoint {
    private final PrintingService printingService;
    private final PrintingIdService printingIdService;

    @PostMapping("/print")
    public ResponseEntity<Void> createCard(@RequestBody PrintRequest request) {
        int id = printingIdService.getPrintingId();
        log.info("Received printing request " + id + " for printer " + request.printerIp());
        printingService.print(id, request.printerIp(), request.documentFormat(), request.file());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
