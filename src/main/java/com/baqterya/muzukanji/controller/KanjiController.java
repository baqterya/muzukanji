package com.baqterya.muzukanji.controller;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiDto;
import com.baqterya.muzukanji.model.KanjiModel;
import com.baqterya.muzukanji.model.KanjiModelAssembler;
import com.baqterya.muzukanji.service.KanjiService;
import com.baqterya.muzukanji.util.Util;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baqterya.muzukanji.util.Const.*;
import static org.springframework.hateoas.PagedModel.*;

@RestController
@RequestMapping(path = "api/v1/kanji")
@RequiredArgsConstructor
@Validated
public class KanjiController {

    private final KanjiService kanjiService;
    private final KanjiModelAssembler kanjiModelAssembler;
    private final PagedResourcesAssembler<Kanji> pagedResourcesAssembler;


    // OPEN ENDPOINTS


    @GetMapping()
    @PreAuthorize("permitAll")
    public ResponseEntity<Map<String, Object>> getKanji(
        @RequestParam(required = false) String kanji,
        @RequestParam(required = false) String meaning,
        @RequestParam(required = false) String kunyomi,
        @RequestParam(required = false) String kunyomiRomaji,
        @RequestParam(required = false) String onyomi,
        @RequestParam(required = false) String onyomiRomaji,
        @RequestParam(required = false) @Min(1) @Max(34) Integer minStrokes,
        @RequestParam(required = false) @Min(1) @Max(34) Integer maxStrokes,
        @RequestParam(required = false) @Pattern(regexp = JLPT_REGEX, message = JLPT_ERROR_MESSAGE)
        String minJlptLevel,
        @RequestParam(required = false) @Pattern(regexp = JLPT_REGEX, message = JLPT_ERROR_MESSAGE)
        String maxJlptLevel,
        @RequestParam(required = false) @Min(1) @Max(10) Integer minJyoyoGrade,
        @RequestParam(required = false) @Min(1) @Max(10) Integer maxJyoyoGrade,
        @RequestParam(required = false) @Min(1) @Max(2501) Integer minUsage,
        @RequestParam(required = false) @Min(1) @Max(2501) Integer maxUsage,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "50") Integer size,
        @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        try {
            List<Order> orders = Util.getSortingOrder(sort);

            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Kanji> kanjiPage = kanjiService.searchKanji(
                    kanji, meaning, kunyomi, kunyomiRomaji, onyomi, onyomiRomaji, minStrokes, maxStrokes,
                    minJlptLevel, maxJlptLevel, minJyoyoGrade, maxJyoyoGrade, minUsage, maxUsage, pagingSort
            );
            PagedModel<KanjiModel> kanjiPagedModel = pagedResourcesAssembler.toModel(kanjiPage, kanjiModelAssembler);

            Collection<KanjiModel> kanjiList = kanjiPagedModel.getContent();
            for (KanjiModel kanjiModel : kanjiList) {
                Link selfLink = WebMvcLinkBuilder.linkTo(KanjiController.class).slash(kanjiModel.getId()).withSelfRel();
                kanjiModel.add(selfLink);
            }
            if (kanjiList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            PageMetadata metadata = kanjiPagedModel.getMetadata();
            if (metadata == null) {
                throw new Exception();
            }

            Map<String, Object> paginationData = new HashMap<>();

            paginationData.put("warning", "entries and pages are counted from 0 (eg. last page is 262)");
            paginationData.put("metadata", kanjiPagedModel.getMetadata());
            paginationData.put("links", kanjiPagedModel.getLinks());

            Map<String, Object> response = new HashMap<>();
            response.put("pagination", paginationData);
            response.put("data", kanjiList);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{kanjiId}")
    @PreAuthorize("permitAll")
    public Kanji getKanjiById(
        @PathVariable Integer kanjiId
    ) {
        return kanjiService.getKanjiById(kanjiId);
    }


    // PROTECTED ENDPOINTS

    @PostMapping()
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Kanji> createKanji(
        @RequestBody KanjiDto newKanjiDto
    ) {
        return new ResponseEntity<>(kanjiService.createKanji(newKanjiDto), HttpStatus.CREATED);
    }

    @PutMapping("/{kanjiId}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Kanji> updateKanji(
        @PathVariable Integer kanjiId,
        @RequestBody KanjiDto updatedKanjiDto
    ) {
        return new ResponseEntity<>(kanjiService.updateKanji(kanjiId, updatedKanjiDto), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<String> deleteKanji(
        @PathVariable Integer id
    ) {
        return new ResponseEntity<>(String.format(KANJI_DELETED_MESSAGE, id), HttpStatus.OK);
    }

}
