package com.project.cron.converter;

import java.nio.charset.Charset;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class RottenTomatoesMessageConverter extends MappingJackson2HttpMessageConverter {

    public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

    private MediaType textJavascriptMediaType = new MediaType("text", "javascript", DEFAULT_CHARSET);

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        return textJavascriptMediaType.compareTo(mediaType) == 0;
    }
}
