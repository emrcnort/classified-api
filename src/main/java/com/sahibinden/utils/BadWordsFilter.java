package com.sahibinden.utils;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class BadWordsFilter {
    private Set<String> badWords = new HashSet<>();

    @PostConstruct
    public void init() {
        loadBadWords(Paths.get("Badwords.txt"));
    }

    public void loadBadWords(Path path) {
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(badWords::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean containsBadWords(String text) {
        return badWords.stream().anyMatch(text::contains);
    }
}