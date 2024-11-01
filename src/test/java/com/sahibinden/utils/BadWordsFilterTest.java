package com.sahibinden.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BadWordsFilterTest {

    private BadWordsFilter badWordsFilter;

    @BeforeEach
    void setUp() {
        badWordsFilter = new BadWordsFilter();
    }

    @Test
    void whenBadWordIsGiven_shouldDetectTextContainingBadWord() throws IOException {
        badWordsFilter.loadBadWords(Paths.get("Badwords.txt"));

        assertTrue(badWordsFilter.containsBadWords("kqr"));
    }

    @Test
    void whenNoBadWordIsGiven_thereShouldBeNoBadWordsToDetect() throws IOException {
        badWordsFilter.loadBadWords(Paths.get("Badwords.txt"));

        assertFalse(badWordsFilter.containsBadWords("badword"));
    }

}
