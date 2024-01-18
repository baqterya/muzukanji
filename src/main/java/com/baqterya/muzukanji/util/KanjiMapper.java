package com.baqterya.muzukanji.util;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface KanjiMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateKanjiFromDto(KanjiDto dto, @MappingTarget Kanji kanji);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Kanji kanjiDtoToKanji(KanjiDto dto);

}
