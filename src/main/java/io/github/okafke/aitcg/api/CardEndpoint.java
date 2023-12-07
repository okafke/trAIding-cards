package io.github.okafke.aitcg.api;

import io.github.okafke.aitcg.card.creation.CardService;
import io.github.okafke.aitcg.card.printing.PrintingIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/card")
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class CardEndpoint {
    private final PrintingIdService printingIdService;
    private final CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<CardCreationResponse> createCard(@RequestBody CardCreationRequest request) throws IOException {
        int id = printingIdService.getPrintingId();
        UUID uuid = UUID.randomUUID();
        UUID secondUUID = UUID.randomUUID();
        log.info("Received card creation request " + id + " " + uuid + " " + request + " second card: " + secondUUID);
        cardService.createCard(request, id, uuid, secondUUID);
        return new ResponseEntity<>(new CardCreationResponse(id, uuid, secondUUID), HttpStatus.CREATED);
    }

}
