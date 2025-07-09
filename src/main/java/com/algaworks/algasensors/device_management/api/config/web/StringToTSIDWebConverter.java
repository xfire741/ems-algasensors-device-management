package com.algaworks.algasensors.device_management.api.config.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import io.hypersistence.tsid.TSID;

public class StringToTSIDWebConverter implements Converter<String, TSID> {

    @Override
    public TSID convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return TSID.from(source);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid TSID format: " + source, e);
        }
    }

}
