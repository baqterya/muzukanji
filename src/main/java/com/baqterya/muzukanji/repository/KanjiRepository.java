package com.baqterya.muzukanji.repository;

import com.baqterya.muzukanji.model.Kanji;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Integer> {

    @Override
    @NonNull Page<Kanji> findAll(@NonNull Pageable pageable);

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
