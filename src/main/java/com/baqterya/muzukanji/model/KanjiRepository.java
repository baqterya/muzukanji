package com.baqterya.muzukanji.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Integer> {
// TODO make paginable in the future

    Optional<Kanji> findByKanji(String kanji);

//    List<Optional<Kanji>> findByMeaning(String meaning);

//    List<Optional<Kanji>> findByReadingKana(String reading);

//    List<Optional<Kanji>> findByReadingRomaji(String reading);

//    List<Optional<Kanji>> findByJlptLevel(String level);

//    List<Optional<Kanji>> findByStrokes(Integer strokes);

//    List<Optional<Kanji>> findByJyoyo(Integer grade);

//    List<Optional<Kanji>> findByUsage(Integer usage);

//    List<Optional<Kanji>> findByRadical(List<String> radicals);

}
