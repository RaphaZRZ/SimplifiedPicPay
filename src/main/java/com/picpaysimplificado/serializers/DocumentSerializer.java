package com.picpaysimplificado.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DocumentSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String document, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        String maskedDocument = document;

        if (document.length() == 11) // CPF
            maskedDocument = document.substring(0, 3) + ".***.***-" + document.substring(9, 11);
        else if (document.length() == 14) // CNPJ
            maskedDocument = document.substring(0, 2) + ".***.***/" + document.substring(8, 12) + "-" + document.substring(12, 14);


        gen.writeString(maskedDocument); // substituir o documento ao fazer requisições GET
    }
}
