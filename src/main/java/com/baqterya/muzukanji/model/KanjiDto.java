package com.baqterya.muzukanji.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class KanjiDto {
    private String kanji;
    private String meanings;
    private String kunyomi;
    private String kunyomiRomaji;
    private String onyomi;
    private String onyomiRomaji;
    private Integer strokes;
    private String jlptLevel;
    private Integer jyoyoGradeTaught;
    private Integer mostUsedInNewspapers;
}
