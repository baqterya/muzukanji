package com.baqterya.muzukanji.service;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiDto;
import com.baqterya.muzukanji.repository.KanjiRepository;
import com.baqterya.muzukanji.specification.KanjiSpecification;
import com.baqterya.muzukanji.util.KanjiMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.baqterya.muzukanji.util.Const.KANJI_ALREADY_EXISTS_MESSAGE;
import static com.baqterya.muzukanji.util.Const.KANJI_NOT_FOUND_BY_ID_MESSAGE;

@Service
@RequiredArgsConstructor
public class KanjiService {

    private final KanjiRepository kanjiRepository;
    @Autowired
    private final KanjiMapper kanjiMapper = Mappers.getMapper(KanjiMapper.class);

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

    public Kanji createKanji(KanjiDto newKanjiDto) {
        Kanji newKanji = kanjiMapper.kanjiDtoToKanji(newKanjiDto);
        kanjiRepository.findByKanji(newKanji.getKanji()).ifPresent(
            foundKanji -> {
                throw new EntityExistsException(String.format(
                        KANJI_ALREADY_EXISTS_MESSAGE, newKanji.getKanji()
                ));
            }
        );
        kanjiRepository.save(newKanji);
        return newKanji;
    }

    public Kanji updateKanji(Integer kanjiId, KanjiDto updatedKanjiDto) {
        Kanji kanjiToUpdate = kanjiRepository.findById(kanjiId)
            .orElseThrow(
                    () -> new EntityNotFoundException(String.format(KANJI_NOT_FOUND_BY_ID_MESSAGE, kanjiId))
            );
        kanjiMapper.updateKanjiFromDto(updatedKanjiDto, kanjiToUpdate);
        kanjiRepository.save(kanjiToUpdate);
        return kanjiToUpdate;
    }

    public void deleteKanji(Integer kanjiId) {
        kanjiRepository.findById(kanjiId).ifPresentOrElse(
            foundKanji -> kanjiRepository.deleteById(kanjiId),
            () -> {
                throw new EntityNotFoundException(String.format(
                        KANJI_NOT_FOUND_BY_ID_MESSAGE, kanjiId
                ));
            }
        );
    }
}
