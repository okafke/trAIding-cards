package io.github.okafke.aitcg.llm;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Prompts {
    /**
     * Ensures that the model we use does not say things like: "Certainly! To do "thing you instructed me to do" (basically just our prompt) ...".
     * This is not perfect: Upon requesting a prompt for Dall-E it replied with "Design a prompt for Dall-E:...".
     */
    public static final String ONLY_OUTPUT = "Only output your answer, do not repeat my prompt, " +
            "do not explain your thought-process and do not begin by saying things like \"Certainly!\" or \"Sure!\". ";

    public static final String RANDOM_AUTHOR = "Create a story in the style of a random author (dont tell me which), about this object in 50 words.";
    public static final String NAME = "Given this prompt, give this object a name in a fantastic setting, in 1-5 words."; // TODO: attributes for names
    public static final String REQUEST_ELEMENT = "Which element do you think would fit the object just described the best: fire, water, earth or air? " +
            "Answer only with the element, do not explain your decision.";

    // TODO: small chance for request to be a poem?

    public static String limitSentences(int n) {
        return "Answer in " + n + " sentences. ";
    }

    public static String list(List<String> words) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (i != 0) {
                if (i == words.size() - 1) {
                    result.append(" and ");
                } else {
                    result.append(", ");
                }
            }

            result.append(word);
        }


        return result.toString();
    }

}
