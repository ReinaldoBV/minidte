package com.azurian.minidte.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentoMapperTest {

    private DocumentoMapper documentoMapper;

    @BeforeEach
    public void setUp() {
        documentoMapper = new DocumentoMapperImpl();
    }
}
