package com.baqterya.muzukanji.unittests.repository;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.repository.KanjiRepository;
import com.baqterya.muzukanji.util.Util;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import java.util.List;
import java.util.Optional;

import static com.baqterya.muzukanji.util.Const.TEST_KANJI;
import static com.baqterya.muzukanji.util.Const.TEST_KANJI_2;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class KanjiRepositoryTests {

    @Autowired
    private KanjiRepository kanjiRepository;

    @Test
    public void KanjiRepository_FindById_ReturnKanji() {
        kanjiRepository.save(TEST_KANJI);
        Kanji savedKanji = kanjiRepository.findById(TEST_KANJI.getId()).get();

        Assertions.assertThat(savedKanji).isNotNull();
        Assertions.assertThat(savedKanji)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(TEST_KANJI);
    }

    @Test
    public void KanjiRepository_FindByKanji_ReturnKanji() {
        kanjiRepository.save(TEST_KANJI);
        Kanji savedKanji = kanjiRepository.findByKanji(TEST_KANJI.getKanji()).get();

        Assertions.assertThat(savedKanji).isNotNull();
        Assertions.assertThat(savedKanji)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(TEST_KANJI);
    }

    @Test
    public void KanjiRepository_FindAll_ReturnSavedKanji() {
        kanjiRepository.saveAll(List.of(TEST_KANJI, TEST_KANJI_2));
        List<Kanji> savedKanjiList = kanjiRepository.findAll();

        Assertions.assertThat(savedKanjiList).isNotNull();
        Assertions.assertThat(savedKanjiList.size()).isEqualTo(2);

        Assertions.assertThat(savedKanjiList.get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(TEST_KANJI);
        Assertions.assertThat(savedKanjiList.get(1))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(TEST_KANJI_2);
    }

    @Test
    public void KanjiRepository_FindAll_ReturnKanjiPage() {
        kanjiRepository.saveAll(List.of(TEST_KANJI, TEST_KANJI_2));

        String[] sort = {"id", "asc"};
        List<Order> orders = Util.getSortingOrder(sort);
        Pageable pagingSort = PageRequest.of(0, 50, Sort.by(orders));
        Page<Kanji> kanjiPage = kanjiRepository.findAll(pagingSort);

        Assertions.assertThat(kanjiPage).isNotNull();
        Assertions.assertThat(kanjiPage.getPageable()).isNotNull();
        Assertions.assertThat(kanjiPage.getContent().size()).isEqualTo(2);
    }

    @Test
    public void KanjiRepository_UpdateKanji_ReturnUpdatedKanji() {
        kanjiRepository.save(TEST_KANJI);
        String changedValue = "a";

        Kanji savedKanji = kanjiRepository.findById(1).get();
        savedKanji.setKanji(changedValue);
        kanjiRepository.save(savedKanji);
        Kanji updatedKanji = kanjiRepository.findById(1).get();

        Assertions.assertThat(updatedKanji).isNotNull();
        Assertions.assertThat(updatedKanji.getKanji()).isEqualTo(changedValue);
    }

    @Test
    public void KanjiRepository_DeleteKanji_ReturnNull() {
        kanjiRepository.save(TEST_KANJI);
        kanjiRepository.deleteById(1);

        Optional<Kanji> emptyKanji = kanjiRepository.findById(1);
        Assertions.assertThat(emptyKanji).isEmpty();
    }

}
