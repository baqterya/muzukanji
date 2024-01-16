package com.baqterya.muzukanji.util;

import com.baqterya.muzukanji.model.Kanji;

public class Const {
    public static final String KANJI_NOT_FOUND_BY_ID_MESSAGE = "Kanji with id %d not found in the database";
    public static final String KANJI_ALREADY_EXISTS_MESSAGE = "Kanji '%s' already exists";

    public static final String JLPT_REGEX = "^(N5|N4|N3|N2|N1|n5|n4|n3|n2|n1)$";
    public static final String JLPT_ERROR_MESSAGE
            = "Invalid JLPT level entered. JLPT level must match (N5|N4|N3|N2|N1|n5|n4|n3|n2|n1)" ;

    public static final Kanji TEST_KANJI = new Kanji(
            "一", "One, One Radical (no.1)",
            "ひと-, ひと.つ", "hito-, hito.tsu",
            "イチ, イツ", "ichi, itsu",
            1, "N5", 1, 2
    );
}
