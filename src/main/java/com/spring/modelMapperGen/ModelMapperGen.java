package com.spring.modelMapperGen;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class ModelMapperGen
{
    static ModelMapper modelMapper = null;

    public static ModelMapper getModelMapperSingleton()
    {
        if(modelMapper == null)
        {
            modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
        }
        return modelMapper;
    }
}
