package com.baqterya.muzukanji.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KanjiDto {
    private Integer id;
    private String kanji;
    private List<String> meanings;
    private List<String> kunyomi;
    private List<String> kunyomi_romaji;
    private List<String> onyomi;
    private List<String> onyomi_romaji;
    private Integer strokes;
    private String jlptLevel;
    private Integer jyoyoGradeTaught;
    private Integer mostUsedInNewspapers;
}
