package com.baqterya.muzukanji.util;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.repository.KanjiRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PopulateDatabase {

    @Bean
    CommandLineRunner commandLineRunner(KanjiRepository repository) {
        return args -> {
            List<Kanji> testData = List.of(
                    Kanji.builder()
                            .kanji("人")
                            .meanings(List.of("person"))
                            .kunyomi(List.of("ひと", "-り", "-と"))
                            .kunyomi_romaji(List.of("hito", "-ri", "-to"))
                            .onyomi(List.of("ジン", "ニン"))
                            .onyomi_romaji(List.of("jin", "nin"))
                            .strokes(2)
                            .jlptLevel("N5")
                            .jyoyoGradeTaught(1)
                            .mostUsedInNewspapers(5)
                            .build(),
                    Kanji.builder()
                            .kanji("空")
                            .meanings(List.of("empty", "sky", "void", "vacant", "vacuum"))
                            .kunyomi(List.of("そら", "あ.く", "あ.き", "あ.ける", "から", "す.く", "す.かす", "むな.しい"))
                            .kunyomi_romaji(List.of("sora", "a.ku", "a.ki", "a.keru", "kara", "su.ku", "su.kasu", "muna.shii"))
                            .onyomi(List.of("クウ"))
                            .onyomi_romaji(List.of("kuu"))
                            .strokes(8)
                            .jlptLevel("N4")
                            .jyoyoGradeTaught(1)
                            .mostUsedInNewspapers(304)
                            .build(),
                    Kanji.builder()
                            .kanji("私")
                            .meanings(List.of("private", "I", "me"))
                            .kunyomi(List.of("わたくし", "わたし"))
                            .kunyomi_romaji(List.of("watakushi", "watashi"))
                            .onyomi(List.of("シ"))
                            .onyomi_romaji(List.of("shi"))
                            .strokes(7)
                            .jlptLevel("N4")
                            .jyoyoGradeTaught(6)
                            .mostUsedInNewspapers(242)
                            .build(),
                    Kanji.builder()
                            .kanji("女")
                            .meanings(List.of("woman", "female"))
                            .kunyomi(List.of("おんな", "め"))
                            .kunyomi_romaji(List.of("onna", "me"))
                            .onyomi(List.of("ジョ", "ニョ", "ニョウ"))
                            .onyomi_romaji(List.of("jyo", "nyo", "nyou"))
                            .strokes(3)
                            .jlptLevel("N5")
                            .jyoyoGradeTaught(1)
                            .mostUsedInNewspapers(151)
                            .build(),
                    Kanji.builder()
                            .kanji("愛")
                            .meanings(List.of("love", "affection", "favourite"))
                            .kunyomi(List.of("いと.しい", "かな.しい", "め.でる", "お.しむ", "まな"))
                            .kunyomi_romaji(List.of("ito.shii", "kana.shii", "me.deru", "o.shimu", "mana"))
                            .onyomi(List.of("アイ"))
                            .onyomi_romaji(List.of("ai"))
                            .strokes(13)
                            .jlptLevel("N3")
                            .jyoyoGradeTaught(4)
                            .mostUsedInNewspapers(640)
                            .build(),
                    Kanji.builder()
                            .kanji("空")
                            .meanings(List.of("empty", "sky", "void", "vacant", "vacuum"))
                            .kunyomi(List.of("そら", "あ.く", "あ.き", "あ.ける", "から", "す.く", "す.かす", "むな.しい"))
                            .kunyomi_romaji(List.of("sora", "a.ku", "a.ki", "a.keru", "kara", "su.ku", "su.kasu", "muna.shii"))
                            .onyomi(List.of("クウ"))
                            .onyomi_romaji(List.of("kuu"))
                            .strokes(8)
                            .jlptLevel("N4")
                            .jyoyoGradeTaught(1)
                            .mostUsedInNewspapers(304)
                            .build(),
                    Kanji.builder()
                            .kanji("空")
                            .meanings(List.of("empty", "sky", "void", "vacant", "vacuum"))
                            .kunyomi(List.of("そら", "あ.く", "あ.き", "あ.ける", "から", "す.く", "す.かす", "むな.しい"))
                            .kunyomi_romaji(List.of("sora", "a.ku", "a.ki", "a.keru", "kara", "su.ku", "su.kasu", "muna.shii"))
                            .onyomi(List.of("クウ"))
                            .onyomi_romaji(List.of("kuu"))
                            .strokes(8)
                            .jlptLevel("N4")
                            .jyoyoGradeTaught(1)
                            .mostUsedInNewspapers(304)
                            .build(),
                    Kanji.builder()
                            .kanji("空")
                            .meanings(List.of("empty", "sky", "void", "vacant", "vacuum"))
                            .kunyomi(List.of("そら", "あ.く", "あ.き", "あ.ける", "から", "す.く", "す.かす", "むな.しい"))
                            .kunyomi_romaji(List.of("sora", "a.ku", "a.ki", "a.keru", "kara", "su.ku", "su.kasu", "muna.shii"))
                            .onyomi(List.of("クウ"))
                            .onyomi_romaji(List.of("kuu"))
                            .strokes(8)
                            .jlptLevel("N4")
                            .jyoyoGradeTaught(1)
                            .mostUsedInNewspapers(304)
                            .build(),
                    Kanji.builder()
                            .kanji("空")
                            .meanings(List.of("empty", "sky", "void", "vacant", "vacuum"))
                            .kunyomi(List.of("そら", "あ.く", "あ.き", "あ.ける", "から", "す.く", "す.かす", "むな.しい"))
                            .kunyomi_romaji(List.of("sora", "a.ku", "a.ki", "a.keru", "kara", "su.ku", "su.kasu", "muna.shii"))
                            .onyomi(List.of("クウ"))
                            .onyomi_romaji(List.of("kuu"))
                            .strokes(8)
                            .jlptLevel("N4")
                            .jyoyoGradeTaught(1)
                            .mostUsedInNewspapers(304)
                            .build()
            );
            repository.saveAll(testData);
        };
    }
}
