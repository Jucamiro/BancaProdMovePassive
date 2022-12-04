package com.nttdata.BancaProdMovePassive.web.mapper;

import com.nttdata.BancaProdMovePassive.domain.MovePassive;
import com.nttdata.BancaProdMovePassive.web.model.MovePassiveModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovePassiveMapper {
    MovePassive modelToEntity (MovePassiveModel model);

    MovePassiveModel entityToModel (MovePassive event);

    @Mapping(target="idMovePassive", ignore = true)
    void update(@MappingTarget MovePassive entity, MovePassive updateEntity);
}
