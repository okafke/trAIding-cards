package io.github.okafke.aitcg.api;

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

    @PostMapping("/print")
    public ResponseEntity<Void> createCard(@RequestBody PrintRequest request) {
        UUID uuid = UUID.randomUUID();
        log.info("Received printing request " + uuid + " for printer " + request.printerIp());
        printingService.print(uuid, request.printerIp(), request.documentFormat(), request.file());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
