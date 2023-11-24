package com.baqterya.muzukanji.config;

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
            Kanji hito = Kanji.builder()
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
                    .radicals(List.of("man, human 人 (亻)"))
                    .build();

            repository.save(hito);
        };
    }
}
