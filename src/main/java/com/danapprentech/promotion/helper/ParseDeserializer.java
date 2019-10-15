package com.danapprentech.promotion.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

public class ParseDeserializer extends StdDeserializer<LocalDateTime> {
    public ParseDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws  IOException {
        return LocalDateTime.parse(p.getValueAsString()); // or overloaded with an appropriate format
    }
}
