package io.github.okafke.aitcg.card;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CardTypeAndAttributeService {
    private final List<String> attributes;
    private final List<String> animals;
    private final List<String> objects;

    public CardTypeAndAttributeService(@Value("classpath:text/animals.txt") Resource animals,
                                       @Value("classpath:text/objects.txt") Resource objects,
                                       @Value("classpath:text/attributes.txt") Resource attributes,
                                       @Value("classpath:text/animals_de.txt") Resource animalsDe,
                                       @Value("classpath:text/objects_de.txt") Resource objectsDe,
                                       @Value("classpath:text/attributes_de.txt") Resource attributesDe) {
        this.attributes = parseTxt(attributes, attributesDe);
        this.animals = parseTxt(animals, animalsDe);
        this.objects = parseTxt(objects, objectsDe);
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
    private List<String> parseTxt(Resource resource, Resource translationResource) {
        String[] strings = resource.getContentAsString(StandardCharsets.UTF_8).split("\\R");
        String[] stringsTranslated = translationResource.getContentAsString(StandardCharsets.UTF_8).split("\\R");
        List<String> result = new ArrayList<>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (!StringUtils.hasText(string)) {
                continue;
            }

            String translation = i < stringsTranslated.length ? stringsTranslated[i] : "";
            if (StringUtils.hasText(translation)) {
                string += "\n(" + translation + ")";
            }

            result.add(string);
        }

        return result;
    }

}
