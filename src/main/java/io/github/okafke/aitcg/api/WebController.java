package io.github.okafke.aitcg.api;

import io.github.okafke.aitcg.card.CardTypeAndAttributeService;
import io.github.okafke.aitcg.card.printing.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class WebController {
    private final CardTypeAndAttributeService cardTypeAndAttributeService;
    private final FileService fileService;

    @RequestMapping("/")
    public String home(Model model) {
        var names = fileService.getCardNames();
        Collections.shuffle(names);
        model.addAttribute("images", names.stream().map(name -> name + "-card.png").limit(20).collect(Collectors.toList()));
        return "index";
    }

    @RequestMapping("/create")
    public String create(@RequestParam(defaultValue = "") String type, Model model) {
        if (StringUtils.hasText(type)) {
            model.addAttribute("attributes", cardTypeAndAttributeService.getMaxRandomAttributes(50));
            model.addAttribute("type", type);
            model.addAttribute("title_text", "Customize your " + type + " card with up to 5 attributes.");
        } else {
            model.addAttribute("attributes", cardTypeAndAttributeService.getMaxRandomCreatures(50));
            model.addAttribute("type", null);
            model.addAttribute("title_text", "Choose what your card will be based on.");
        }

        return "create";
    }


}
