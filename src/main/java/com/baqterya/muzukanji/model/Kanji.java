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
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private Integer id;
    private String kanji;
    private String romaji;
    @Type(ListArrayType.class)
    private List<String> meanings;
    @Type(ListArrayType.class)
    private List<String> kunyomi;
    @Type(ListArrayType.class)
    private List<String> onyomi;
    @Type(ListArrayType.class)
    private List<String> kunyomi_romaji;
    @Type(ListArrayType.class)
    private List<String> onyomi_romaji;
    private Integer strokes;
    private String jlptLevel;
    private Integer jyoyoGradeTaught;
    private Integer mostUsedInNewspapers;
    @Type(ListArrayType.class)
    private List<String> radicals;

}
