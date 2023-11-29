package com.baqterya.muzukanji.service;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.repository.KanjiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KanjiService {

    private final KanjiRepository kanjiRepository;

    public Page<Kanji> getAllKanji(Pageable pagingSort){
        return kanjiRepository.findAll(pagingSort);
    }

    public void addNewKanji(Kanji newKanji) {
        Optional<Kanji> kanjiById = kanjiRepository.findById(newKanji.getId());
        if (kanjiById.isPresent()) {
            throw new IllegalStateException(String.format(
                    "Kanji %s already in the database", newKanji.getKanji()
            ));
        }
        kanjiRepository.save(newKanji);
    }
}
