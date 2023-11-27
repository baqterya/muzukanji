package com.baqterya.muzukanji.model;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiRepository;
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
                            .radicals(List.of("人"))
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
                            .radicals(List.of("儿", "宀", "工", "穴"))
                            .build()
            );
            repository.saveAll(testData);
        };
    }
}
