package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * You can find an example of this in the Role functionality.
 */
@Component
public class MapperUtil {

    private final ModelMapper modelMapper;

    @Autowired
    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <K, V> V map(K source, Class<V> destionationClass) {
        return modelMapper.map(source, destionationClass);
    }
}
