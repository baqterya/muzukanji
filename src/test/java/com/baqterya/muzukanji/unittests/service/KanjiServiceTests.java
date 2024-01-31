package com.baqterya.muzukanji.unittests.service;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiDto;
import com.baqterya.muzukanji.repository.KanjiRepository;
import com.baqterya.muzukanji.service.KanjiService;
import com.baqterya.muzukanji.specification.KanjiSpecification;
import com.baqterya.muzukanji.util.Util;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;


import java.lang.reflect.Executable;
import java.util.List;
import java.util.Optional;

import static com.baqterya.muzukanji.util.Const.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void GivenId_GetKanjiById_ReturnKanji() {
        Integer kanjiId = 1;

        when(kanjiRepository.findById(kanjiId)).thenReturn(Optional.of(TEST_KANJI));

        Kanji savedKanji = kanjiService.getKanjiById(kanjiId);
        assertThat(savedKanji).isNotNull();
    }

    @Test
    public void GivenKanjiDto_CreateKanji_ReturnKanji() {
        when(kanjiRepository.save(any(Kanji.class))).thenReturn(TEST_KANJI);

        Kanji savedKanji = kanjiService.createKanji(TEST_KANJI_DTO);
        assertThat(savedKanji).isNotNull();
    }

    @Test
    public void GivenIdAndKanjiDto_UpdateKanji_ReturnKanji() {
        Integer kanjiId = 1;

        when(kanjiRepository.findById(kanjiId)).thenReturn(Optional.of(TEST_KANJI));
        when(kanjiRepository.save(TEST_KANJI)).thenReturn(TEST_KANJI);

        Kanji updatedKanji = kanjiService.updateKanji(kanjiId, TEST_KANJI_DTO);
        assertThat(updatedKanji).isNotNull();
    }

    @Test
    public void GivenId_DeleteKanji() {
        Integer kanjiId = 1;
        when(kanjiRepository.findById(kanjiId)).thenReturn(Optional.of(TEST_KANJI));

        kanjiService.deleteKanji(kanjiId);

        verify(kanjiRepository, times(1)).deleteById(kanjiId);
    }

    @Test
    public void GivenPageableAndSpecification_SearchKanji_ReturnKanjiPage() {
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
        assertThat(returnedKanjiPage).isNotNull();
    }

    @Test
    public void GivenIdNotInDatabase_GetKanjiById_ThrowEntityNotFoundException() {
        Integer kanjiId = 1;

        when(kanjiRepository.findById(kanjiId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> kanjiService.getKanjiById(kanjiId))
            .withMessage(KANJI_NOT_FOUND_BY_ID_MESSAGE, kanjiId);
    }

    @Test
    public void GivenExistingKanji_CreateKanji_ThrowEntityExistsException() {
        when(kanjiRepository.findByKanji(anyString())).thenReturn(Optional.of(TEST_KANJI));

        assertThatExceptionOfType(EntityExistsException.class)
            .isThrownBy(() -> kanjiService.createKanji(TEST_KANJI_DTO))
            .withMessage(KANJI_ALREADY_EXISTS_MESSAGE, TEST_KANJI_DTO.getKanji());
    }

    @Test
    public void GivenIdNotInDatabase_UpdateKanji_ThrowEntityNotFoundException() {
        Integer kanjiId = 1;

        when(kanjiRepository.findById(kanjiId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> kanjiService.updateKanji(kanjiId, TEST_KANJI_DTO))
            .withMessage(KANJI_NOT_FOUND_BY_ID_MESSAGE, kanjiId);
    }

    @Test
    public void GivenIdNotInDatabase_DeleteKanji_ThrowEntityNotFoundException() {
        Integer kanjiId = 1;

        when(kanjiRepository.findById(kanjiId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> kanjiService.deleteKanji(kanjiId))
            .withMessage(KANJI_NOT_FOUND_BY_ID_MESSAGE, kanjiId);
    }

}
