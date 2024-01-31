package com.baqterya.muzukanji.model;

import com.baqterya.muzukanji.controller.KanjiController;
import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiModel;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class KanjiModelAssembler extends RepresentationModelAssemblerSupport<Kanji, KanjiModel> {

    public KanjiModelAssembler() {
        super(KanjiController.class, KanjiModel.class);
    }

    @Override
    @NonNull public KanjiModel toModel(@NonNull Kanji entity) {
        KanjiModel kanjiModel = new KanjiModel();
        BeanUtils.copyProperties(entity, kanjiModel);
        return kanjiModel;
    }

}
