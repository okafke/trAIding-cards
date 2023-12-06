package io.github.okafke.aitcg.api;

import io.github.okafke.aitcg.card.printing.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class WebController {
    private final FileService fileService;

    @RequestMapping("/")
    public String home(Model model) {
        var names = fileService.getCardNames();
        Collections.shuffle(names);
        model.addAttribute("images", names.stream().map(name -> name + "-card.png").limit(20).collect(Collectors.toList()));
        return "index";
    }

}
