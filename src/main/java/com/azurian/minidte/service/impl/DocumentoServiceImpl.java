package com.azurian.minidte.service.impl;

import com.azurian.minidte.domain.Documento;
import com.azurian.minidte.repository.DocumentoRepository;
import com.azurian.minidte.service.DocumentoService;
import com.azurian.minidte.service.dto.DocumentoDTO;
import com.azurian.minidte.service.mapper.DocumentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Documento}.
 */
@Service
@Transactional
public class DocumentoServiceImpl implements DocumentoService {

    private final Logger log = LoggerFactory.getLogger(DocumentoServiceImpl.class);

    private final DocumentoRepository documentoRepository;

    private final DocumentoMapper documentoMapper;

    public DocumentoServiceImpl(DocumentoRepository documentoRepository, DocumentoMapper documentoMapper) {
        this.documentoRepository = documentoRepository;
        this.documentoMapper = documentoMapper;
    }

    @Override
    public DocumentoDTO save(DocumentoDTO documentoDTO) {
        log.debug("Request to save Documento : {}", documentoDTO);
        Documento documento = documentoMapper.toEntity(documentoDTO);
        documento = documentoRepository.save(documento);
        return documentoMapper.toDto(documento);
    }

    @Override
    public Optional<DocumentoDTO> partialUpdate(DocumentoDTO documentoDTO) {
        log.debug("Request to partially update Documento : {}", documentoDTO);

        return documentoRepository
            .findById(documentoDTO.getId())
            .map(existingDocumento -> {
                documentoMapper.partialUpdate(existingDocumento, documentoDTO);

                return existingDocumento;
            })
            .map(documentoRepository::save)
            .map(documentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Documentos");
        return documentoRepository.findAll(pageable).map(documentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentoDTO> findOne(Long id) {
        log.debug("Request to get Documento : {}", id);
        return documentoRepository.findById(id).map(documentoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Documento : {}", id);
        documentoRepository.deleteById(id);
    }
}
