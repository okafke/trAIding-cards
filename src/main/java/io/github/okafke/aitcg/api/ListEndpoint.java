package io.github.okafke.aitcg.api;

import io.github.okafke.aitcg.card.printing.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class ListEndpoint {
    private final FileService fileService;

    @GetMapping("/list")
    public ResponseEntity<List<String>> createCard() {
        return new ResponseEntity<>(fileService.getCardNames(), HttpStatus.OK);
    }

}
