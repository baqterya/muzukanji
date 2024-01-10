package com.baqterya.muzukanji.service;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiDto;
import com.baqterya.muzukanji.repository.KanjiRepository;
import com.baqterya.muzukanji.specification.KanjiSpecification;
import com.baqterya.muzukanji.util.KanjiMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.baqterya.muzukanji.util.Const.KANJI_ALREADY_EXISTS_MESSAGE;
import static com.baqterya.muzukanji.util.Const.KANJI_NOT_FOUND_BY_ID_MESSAGE;

@Service
@RequiredArgsConstructor
public class KanjiService {

    private final KanjiRepository kanjiRepository;
    private final KanjiMapper kanjiMapper;

    public Page<Kanji> searchKanji(
        String kanji,
        String meaning,
        String kunyomi,
        String kunyomiRomaji,
        String onyomi,
        String onyomiRomaji,
        Integer minStrokes,
        Integer maxStrokes,
        String minJlptLevel,
        String maxJlptLevel,
        Integer minJyoyoGrade,
        Integer maxJyoyoGrade,
        Integer minUsage,
        Integer maxUsage,
        Pageable pagingSort
    ) {

        Specification<Kanji> kanjiSpecification = KanjiSpecification
        .builder()
        .kanji(kanji)
        .meaning(meaning)
        .kunyomi(kunyomi)
        .kunyomiRomaji(kunyomiRomaji)
        .onyomi(onyomi)
        .onyomiRomaji(onyomiRomaji)
        .minStrokes(minStrokes)
        .maxStrokes(maxStrokes)
        .minJlptLevel(minJlptLevel)
        .maxJlptLevel(maxJlptLevel)
        .minJyoyoGrade(minJyoyoGrade)
        .maxJyoyoGrade(maxJyoyoGrade)
        .minUsage(minUsage)
        .maxUsage(maxUsage)
        .build();

        return kanjiRepository.findAll(kanjiSpecification, pagingSort);
    }

    public Kanji getKanjiById(Integer kanjiId) {
        Optional<Kanji> kanjiById = kanjiRepository.findById(kanjiId);
        if (kanjiById.isPresent()) {
            return kanjiById.get();
        }
        throw new EntityNotFoundException(String.format(
                KANJI_NOT_FOUND_BY_ID_MESSAGE, kanjiId
        ));
    }

    public void addNewKanji(Kanji newKanji) {
        Optional<Kanji> kanjiByKanji = kanjiRepository.findByKanji(newKanji.getKanji());
        if (kanjiByKanji.isPresent()) {
            throw new EntityExistsException(String.format(
                    KANJI_ALREADY_EXISTS_MESSAGE, newKanji.getKanji()
            ));
        }
        kanjiRepository.save(newKanji);
    }

    public void deleteKanji(Integer kanjiId) {
        if (!kanjiRepository.existsById(kanjiId)) {
            throw new EntityNotFoundException(String.format(
                    KANJI_NOT_FOUND_BY_ID_MESSAGE, kanjiId
            ));
        }
        kanjiRepository.deleteById(kanjiId);
    }

    public void updateKanji(KanjiDto dto) {
        Kanji kanjiToUpdate = kanjiRepository.findById(dto.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format(KANJI_NOT_FOUND_BY_ID_MESSAGE, dto.getId()))
                );
        kanjiMapper.updateKanjiFromDto(dto, kanjiToUpdate);
        kanjiRepository.save(kanjiToUpdate);
    }
}
