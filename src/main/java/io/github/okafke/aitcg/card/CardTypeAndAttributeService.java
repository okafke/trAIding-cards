package io.github.okafke.aitcg.card;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardTypeAndAttributeService {
    private final List<String> attributes;
    private final List<String> animals;
    private final List<String> objects;

    public CardTypeAndAttributeService(@Value("classpath:text/animals.txt") Resource animals,
                                       @Value("classpath:text/objects.txt") Resource objects,
                                       @Value("classpath:text/attributes.txt") Resource attributes) {
        this.attributes = parseTxt(attributes);
        this.animals = parseTxt(animals);
        this.objects = parseTxt(objects);
    }

    public List<String> getMaxRandomAttributes(int maxAmount) {
        List<String> copy = new ArrayList<>(attributes);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(maxAmount, copy.size()));
    }

    public List<String> getMaxRandomCreatures(int maxAmount) {
        List<String> copy = new ArrayList<>(animals);
        copy.addAll(objects);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(maxAmount, copy.size()));
    }

    @SneakyThrows
    private List<String> parseTxt(Resource resource) {
        String[] strings = resource.getContentAsString(StandardCharsets.UTF_8).split("\\R");
        return Arrays.stream(strings).filter(StringUtils::hasText).map(String::trim).collect(Collectors.toList());
    }

}
