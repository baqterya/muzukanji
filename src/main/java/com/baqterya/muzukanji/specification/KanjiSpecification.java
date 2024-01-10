package com.baqterya.muzukanji.specification;

import com.baqterya.muzukanji.model.Kanji;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static io.micrometer.common.util.StringUtils.isNotBlank;
import static java.util.Optional.ofNullable;

@Data
@Builder
public class KanjiSpecification implements Specification<Kanji> {

    String kanji;
    String meaning;
    String kunyomi;
    String kunyomiRomaji;
    String onyomi;
    String onyomiRomaji;
    Integer minStrokes;
    Integer maxStrokes;
    String minJlptLevel;
    String maxJlptLevel;
    Integer minJyoyoGrade;
    Integer maxJyoyoGrade;
    Integer minUsage;
    Integer maxUsage;

    @Override
    public Predicate toPredicate(
            @NonNull Root<Kanji> root,
            @NonNull CriteriaQuery<?> query,
            @NonNull CriteriaBuilder criteriaBuilder
    ) {
        Predicate kanjiPredicate = ofNullable(kanji)
                .map(k -> criteriaBuilder.equal(root.get("kanji"), kanji))
                .orElse(null);
        
        Predicate meaningPredicate = searchPredicate(criteriaBuilder, root.get("meanings"), meaning);
        Predicate kunyomiPredicate = searchPredicate(criteriaBuilder, root.get("kunyomi"), kunyomi);
        Predicate kunyomiRomajiPredicate = searchPredicate(criteriaBuilder, root.get("kunyomiRomaji"), kunyomiRomaji);
        Predicate onyomiPredicate = searchPredicate(criteriaBuilder, root.get("onyomi"), onyomi);
        Predicate onyomiRomajiPredicate = searchPredicate(criteriaBuilder, root.get("onyomiRomaji"), onyomiRomaji);

        Predicate minStrokesPredicate = minPredicate(criteriaBuilder, root.get("strokes"), minStrokes);
        Predicate minJyoyoGradePredicate = minPredicate(criteriaBuilder, root.get("jyoyoGradeTaught"), minJyoyoGrade);
        Predicate minUsagePredicate = minPredicate(criteriaBuilder, root.get("mostUsedInNewspapers"), minUsage);

        Predicate maxStrokesPredicate = maxPredicate(criteriaBuilder, root.get("strokes"), maxStrokes);
        Predicate maxJyoyoGradePredicate = maxPredicate(criteriaBuilder, root.get("jyoyoGradeTaught"), maxJyoyoGrade);
        Predicate maxUsagePredicate = maxPredicate(criteriaBuilder, root.get("mostUsedInNewspapers"), maxUsage);

        Predicate minJlptPredicate = minJlptPredicate(criteriaBuilder, root.get("jlptLevel"), minJlptLevel);
        Predicate maxJlptPredicate = maxJlptPredicate(criteriaBuilder, root.get("jlptLevel"), maxJlptLevel);

        List<Predicate> predicates = new ArrayList<>();

        ofNullable(kanjiPredicate).ifPresent(predicates::add);

        ofNullable(meaningPredicate).ifPresent(predicates::add);
        ofNullable(kunyomiPredicate).ifPresent(predicates::add);
        ofNullable(kunyomiRomajiPredicate).ifPresent(predicates::add);
        ofNullable(onyomiPredicate).ifPresent(predicates::add);
        ofNullable(onyomiRomajiPredicate).ifPresent(predicates::add);

        ofNullable(minStrokesPredicate).ifPresent(predicates::add);
        ofNullable(minJyoyoGradePredicate).ifPresent(predicates::add);
        ofNullable(minUsagePredicate).ifPresent(predicates::add);

        ofNullable(maxStrokesPredicate).ifPresent(predicates::add);
        ofNullable(maxJyoyoGradePredicate).ifPresent(predicates::add);
        ofNullable(maxUsagePredicate).ifPresent(predicates::add);

        ofNullable(minJlptPredicate).ifPresent(predicates::add);
        ofNullable(maxJlptPredicate).ifPresent(predicates::add);

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private Predicate searchPredicate(CriteriaBuilder criteriaBuilder, Path<String> field, String searchField) {
        if (isNotBlank(searchField)) {
            return criteriaBuilder.like(
                    criteriaBuilder.lower(field), "%" + searchField.toLowerCase() + "%"
            );
        } else {
            return null;
        }
    }

    private Predicate minPredicate(CriteriaBuilder criteriaBuilder, Path<Integer> field, Integer searchField) {
        return ofNullable(searchField)
                .map(sf -> criteriaBuilder.greaterThanOrEqualTo(field, searchField))
                .orElse(null);
    }

    private Predicate maxPredicate(CriteriaBuilder criteriaBuilder, Path<Integer> field, Integer searchField) {
        return ofNullable(searchField)
                .map(sf -> criteriaBuilder.lessThanOrEqualTo(field, searchField))
                .orElse(null);
    }

    private Predicate minJlptPredicate(CriteriaBuilder criteriaBuilder, Path<String> field, String searchField) {
        return ofNullable(searchField)
                .map(sf -> criteriaBuilder.greaterThanOrEqualTo(
                        criteriaBuilder.lower(field),
                        searchField.toLowerCase()
                ))
                .orElse(null);
    }

    private Predicate maxJlptPredicate(CriteriaBuilder criteriaBuilder, Path<String> field, String searchField) {
        return ofNullable(searchField)
                .map(sf -> criteriaBuilder.lessThanOrEqualTo(
                        criteriaBuilder.lower(field),
                        searchField.toLowerCase()
                ))
                .orElse(null);
    }
}
