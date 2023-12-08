package com.baqterya.muzukanji.service;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.repository.KanjiRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.baqterya.muzukanji.util.Const.KANJI_NOT_FOUND_BY_ID_MESSAGE;

@Service
@RequiredArgsConstructor
public class KanjiService {

    private final KanjiRepository kanjiRepository;

    public Page<Kanji> getAllKanji(Pageable pagingSort){
        return kanjiRepository.findAll(pagingSort);
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
        //TODO IMPLEMENT CHECK BY KANJI
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
}
