package com.baqterya.muzukanji.service;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.repository.KanjiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KanjiService {

    private final KanjiRepository kanjiRepository;

    public List<Kanji> getAllKanji() {
        return kanjiRepository.findAll();
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
