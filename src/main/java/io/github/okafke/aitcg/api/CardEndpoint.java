package io.github.okafke.aitcg.api;

import io.github.okafke.aitcg.card.creation.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class CardEndpoint {
    private final CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<Void> createCard(@RequestBody CardCreationRequest request) throws IOException {
        // TODO: return an id
        cardService.createCard(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
