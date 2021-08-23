package com.spring.modelMapperGen;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class ModelMapperGen
{

    static ModelMapper modelMapper = null;
    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }
    public static ModelMapper getModelMapperSingleton()
    {
        if(modelMapper == null)
        {
            modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.getConfiguration().setDeepCopyEnabled(true);
        }
        return modelMapper;
    }
}
