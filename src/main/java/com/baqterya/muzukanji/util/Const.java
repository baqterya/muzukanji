package com.baqterya.muzukanji.util;

import com.baqterya.muzukanji.model.Kanji;

public class Const {
    public static final String KANJI_NOT_FOUND_BY_ID_MESSAGE = "Kanji with id %d not found in the database";
    public static final String KANJI_ALREADY_EXISTS_MESSAGE = "Kanji '%s' already exists";

    public static final String JLPT_REGEX = "^(N5|N4|N3|N2|N1|n5|n4|n3|n2|n1)$";
    public static final String JLPT_ERROR_MESSAGE
            = "Invalid JLPT level entered. JLPT level must match (N5|N4|N3|N2|N1|n5|n4|n3|n2|n1)" ;

    public static final Kanji TEST_KANJI =  Kanji.builder()
            .kanji("一")
            .meanings("One, One Radical (no.1)")
            .kunyomi("ひと-, ひと.つ")
            .kunyomiRomaji("hito-, hito.tsu")
            .onyomi("イチ, イツ")
            .onyomiRomaji("ichi, itsu")
            .strokes(1)
            .jlptLevel("N5")
            .jyoyoGradeTaught(1)
            .mostUsedInNewspapers(2)
            .build();

    public static final Kanji TEST_KANJI_2 =  Kanji.builder()
            .kanji("力")
            .meanings("Power, Strength, Strong, Strain, Bear Up, Exert")
            .kunyomi("ちから")
            .kunyomiRomaji("chikara")
            .onyomi("リョク, リキ, リイ")
            .onyomiRomaji("riku, riki, rii")
            .strokes(2)
            .jlptLevel("N4")
            .jyoyoGradeTaught(1)
            .mostUsedInNewspapers(62)
            .build();

}
