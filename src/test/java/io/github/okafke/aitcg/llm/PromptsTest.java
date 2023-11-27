package io.github.okafke.aitcg.llm;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PromptsTest {
    @Test
    public void testList() {
        assertEquals("", Prompts.list(List.of()));
        assertEquals("test", Prompts.list(List.of("test")));
        assertEquals("test and test1", Prompts.list(List.of("test", "test1")));
        assertEquals("test, test1 and test2", Prompts.list(List.of("test", "test1", "test2")));
    }

}
