package com.baqterya.muzukanji.unittests.service;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiDto;
import com.baqterya.muzukanji.repository.KanjiRepository;
import com.baqterya.muzukanji.service.KanjiService;
import com.baqterya.muzukanji.specification.KanjiSpecification;
import com.baqterya.muzukanji.util.Util;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;
import java.util.Optional;

import static com.baqterya.muzukanji.util.Const.TEST_KANJI;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KanjiServiceTests {

    @Mock
    private KanjiRepository kanjiRepository;

    @InjectMocks
    private KanjiService kanjiService;

    @Test
    public void KanjiService_AddNewKanji_ReturnKanji() {
        when(kanjiRepository.save(any(Kanji.class))).thenReturn(TEST_KANJI);

        Kanji savedKanji = kanjiService.addNewKanji(TEST_KANJI);
        Assertions.assertThat(savedKanji).isNotNull();
    }

    @Test
    public void KanjiService_GetKanjiById_ReturnKanji() {
        Integer kanjiId = 1;

        when(kanjiRepository.findById(kanjiId)).thenReturn(Optional.of(TEST_KANJI));

        Kanji savedKanji = kanjiService.getKanjiById(kanjiId);
        Assertions.assertThat(savedKanji).isNotNull();
    }

    @Test
    public void KanjiService_UpdateKanji_ReturnKanji() {
        Integer kanjiId = 1;
        KanjiDto kanjiDto = new KanjiDto(
                1, "a", "One, One Radical (no.1)",
                "ひと-, ひと.つ", "hito-, hito.tsu", "イチ, イツ", "ichi, itsu",
                1, "N5", 1, 2
        );

        when(kanjiRepository.findById(kanjiId)).thenReturn(Optional.of(TEST_KANJI));
        when(kanjiRepository.save(TEST_KANJI)).thenReturn(TEST_KANJI);

        Kanji updatedKanji = kanjiService.updateKanji(kanjiDto);
        Assertions.assertThat(updatedKanji).isNotNull();
    }

    @Test
    public void KanjiService_DeleteKanji_ReturnNull() {
        Integer kanjiId = 1;
        when(kanjiRepository.existsById(kanjiId)).thenReturn(true);
        doNothing().when(kanjiRepository).deleteById(kanjiId);

        assertAll(() -> kanjiService.deleteKanji(kanjiId));
    }

    @Test
    public void KanjiService_SearchKanji_ReturnKanjiPage() {
        Page<Kanji> kanjiPage = mock(Page.class);

        String[] sort = {"id", "asc"};
        List<Sort.Order> orders = Util.getSortingOrder(sort);
        Pageable pagingSort = PageRequest.of(0, 50, Sort.by(orders));
        Specification<Kanji> kanjiSpecification = KanjiSpecification.builder().build();

        when(kanjiRepository.findAll(kanjiSpecification, pagingSort)).thenReturn(kanjiPage);

        Page<Kanji> returnedKanjiPage = kanjiService.searchKanji(
            null,null,null,null,null,null,
            null,null,null,null,null,
            null,null,null, pagingSort
        );
        Assertions.assertThat(returnedKanjiPage).isNotNull();
    }
}
