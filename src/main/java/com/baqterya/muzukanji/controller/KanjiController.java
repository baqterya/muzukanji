package com.baqterya.muzukanji.controller;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiModel;
import com.baqterya.muzukanji.model.KanjiModelAssembler;
import com.baqterya.muzukanji.service.KanjiService;
import com.baqterya.muzukanji.util.Util;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.PagedModel.*;

@RestController
@RequestMapping(path = "api/v1/kanji")
@RequiredArgsConstructor
public class KanjiController {

    private final KanjiService kanjiService;
    private final KanjiModelAssembler kanjiModelAssembler;
    private final PagedResourcesAssembler<Kanji> pagedResourcesAssembler;


    // OPEN ENDPOINTS


    @GetMapping()
    @PreAuthorize("permitAll")
    public ResponseEntity<Map<String, Object>> getAllKanji(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "50") Integer size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        try {
            List<Order> orders = Util.getSortingOrder(sort);

            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Kanji> kanjiPage = kanjiService.getAllKanji(pagingSort);
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

    @GetMapping("/{id}")
    @PreAuthorize("permitAll")
    public Kanji getKanjiById(
            @PathVariable Integer id
    ) {
        return kanjiService.getKanjiById(id);
    }


    // PROTECTED ENDPOINTS

    @PostMapping
    public void addNewKanji(
            @RequestBody Kanji kanji
    ) {
        kanjiService.addNewKanji(kanji);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public void deleteKanji(
            @PathVariable Integer id
    ) {
        kanjiService.deleteKanji(id);
    }

}
