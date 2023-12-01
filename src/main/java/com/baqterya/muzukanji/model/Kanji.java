package com.baqterya.muzukanji.model;


import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "kanji_table")
public class Kanji {

    @Id
    @SequenceGenerator(
            name = "kanji_sequence",
            sequenceName = "kanji_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "kanji_sequence"
    )
    @Column(columnDefinition = "serial")
    private Integer id;
    private String kanji;
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> meanings;
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> kunyomi;
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> kunyomi_romaji;
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> onyomi;
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    private List<String> onyomi_romaji;
    private Integer strokes;
    private String jlptLevel;
    private Integer jyoyoGradeTaught;
    private Integer mostUsedInNewspapers;

    public Kanji(
            String kanji,
            List<String> meanings,
            List<String> kunyomi,
            List<String> kunyomi_romaji,
            List<String> onyomi,
            List<String> onyomi_romaji,
            Integer strokes,
            String jlptLevel,
            Integer jyoyoGradeTaught,
            Integer mostUsedInNewspapers
    ) {
        this.kanji = kanji;
        this.meanings = meanings;
        this.kunyomi = kunyomi;
        this.kunyomi_romaji = kunyomi_romaji;
        this.onyomi = onyomi;
        this.onyomi_romaji = onyomi_romaji;
        this.strokes = strokes;
        this.jlptLevel = jlptLevel;
        this.jyoyoGradeTaught = jyoyoGradeTaught;
        this.mostUsedInNewspapers = mostUsedInNewspapers;
    }
}
