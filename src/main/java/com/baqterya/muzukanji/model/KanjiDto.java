package com.baqterya.muzukanji.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KanjiDto {
    private Integer id;
    private String kanji;
    private String meanings;
    private String kunyomi;
    private String kunyomi_romaji;
    private String onyomi;
    private String onyomi_romaji;
    private Integer strokes;
    private String jlptLevel;
    private Integer jyoyoGradeTaught;
    private Integer mostUsedInNewspapers;
}
