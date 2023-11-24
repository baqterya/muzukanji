package com.baqterya.muzukanji.model;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/kanji")
@RequiredArgsConstructor
public class KanjiController {

    private final KanjiService kanjiService;

    @GetMapping
    public List<Kanji> getAllKanji() {
        return kanjiService.getAllKanji();
    }

}
