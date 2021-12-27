package com.azurian.minidte.service.mapper;

import com.azurian.minidte.domain.Documento;
import com.azurian.minidte.service.dto.DocumentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Documento} and its DTO {@link DocumentoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocumentoMapper extends EntityMapper<DocumentoDTO, Documento> {}
