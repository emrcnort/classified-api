package com.sahibinden.mapper;

import com.sahibinden.dto.ClassifiedStateDto;
import com.sahibinden.model.ClassifiedState;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassifiedStateMapper extends BaseMapper<ClassifiedState, ClassifiedStateDto> {
}
