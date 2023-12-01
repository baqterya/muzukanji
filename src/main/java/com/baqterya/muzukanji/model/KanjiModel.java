package com.baqterya.muzukanji.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KanjiModel extends RepresentationModel<KanjiModel> {
    private Integer id;
    private String kanji;
    private List<String> meanings;
}
