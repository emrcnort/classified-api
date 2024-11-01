package com.sahibinden.mapper;

import java.util.List;

public interface BaseMapper <E,D>{

    D convertEntityToDto (E entity);

    E convertDtoToEntity (D dto);

    List<D> convertEntityListToDtoList (List<E> entityList);

    List<E> convertDtoListToEntityList (List<D> dtoList);


}
