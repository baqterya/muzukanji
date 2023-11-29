package com.baqterya.muzukanji.controller;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.service.KanjiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/kanji")
@RequiredArgsConstructor
public class KanjiController {

    private final KanjiService kanjiService;

    @GetMapping()
    @PreAuthorize("permitAll")
    public List<Kanji> getAllKanji() {
        return kanjiService.getAllKanji();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('client_admin')")
    public String helloAdmin() {
        return "hello admin";
    }

}
