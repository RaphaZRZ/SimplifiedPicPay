package com.picpaysimplificado.serializers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PasswordSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String password, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeString("***"); // Replace the password when making a GET request
    }
}
