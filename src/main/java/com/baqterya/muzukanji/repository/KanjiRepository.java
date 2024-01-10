package com.baqterya.muzukanji.repository;

import com.baqterya.muzukanji.model.Kanji;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Integer>, JpaSpecificationExecutor<Kanji> {

    @Override
    @NonNull Page<Kanji> findAll(@NonNull Pageable pageable);

    Optional<Kanji> findByKanji(String kanji);

}
