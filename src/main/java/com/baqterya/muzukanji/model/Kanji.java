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
@Entity(name = "kanji_entity")
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
    private String meanings;
    private String kunyomi;
    private String kunyomiRomaji;
    private String onyomi;
    private String onyomiRomaji;
    private Integer strokes;
    private String jlptLevel;
    private Integer jyoyoGradeTaught;
    private Integer mostUsedInNewspapers;

    public Kanji(
        String kanji,
        String meanings,
        String kunyomi,
        String kunyomiRomaji,
        String onyomi,
        String onyomiRomaji,
        Integer strokes,
        String jlptLevel,
        Integer jyoyoGradeTaught,
        Integer mostUsedInNewspapers
    ) {
        this.kanji = kanji;
        this.meanings = meanings;
        this.kunyomi = kunyomi;
        this.kunyomiRomaji = kunyomiRomaji;
        this.onyomi = onyomi;
        this.onyomiRomaji = onyomiRomaji;
        this.strokes = strokes;
        this.jlptLevel = jlptLevel;
        this.jyoyoGradeTaught = jyoyoGradeTaught;
        this.mostUsedInNewspapers = mostUsedInNewspapers;
    }

}
