package io.github.okafke.aitcg.llm;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Prompts {
    /**
     * Ensures that the model we use does not say things like: "Certainly! To do "thing you instructed me to do" (basically just our prompt) ...".
     */
    public static final String ONLY_OUTPUT = "Only output your answer, do not repeat my prompt, " +
            "do not explain your thought-process and do not begin by saying things like \"Certainly!\" or \"Sure!\". ";

    public static final String RANDOM_AUTHOR = "Create a story in the style of a random author (dont tell me which), about this creature. ";
    public static final String NAME = "Given this prompt, give this creature a cute name in 1-5 words."; // TODO: attributes for names

    public static String limitSentences(int n) {
        return "Keep your answer in " + n + " sentences. ";
    }

}
