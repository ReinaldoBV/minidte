package com.azurian.minidte.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.azurian.minidte.IntegrationTest;
import com.azurian.minidte.domain.Documento;
import com.azurian.minidte.repository.DocumentoRepository;
import com.azurian.minidte.service.criteria.DocumentoCriteria;
import com.azurian.minidte.service.dto.DocumentoDTO;
import com.azurian.minidte.service.mapper.DocumentoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DocumentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentoResourceIT {

    private static final Integer DEFAULT_RUT_EMPRESA = 1;
    private static final Integer UPDATED_RUT_EMPRESA = 2;
    private static final Integer SMALLER_RUT_EMPRESA = 1 - 1;

    private static final Integer DEFAULT_ID_AMBIENTE = 1;
    private static final Integer UPDATED_ID_AMBIENTE = 2;
    private static final Integer SMALLER_ID_AMBIENTE = 1 - 1;

    private static final Integer DEFAULT_TIPO_DOCUMENTO = 1;
    private static final Integer UPDATED_TIPO_DOCUMENTO = 2;
    private static final Integer SMALLER_TIPO_DOCUMENTO = 1 - 1;

    private static final Integer DEFAULT_MONTO_TOTAL = 1;
    private static final Integer UPDATED_MONTO_TOTAL = 2;
    private static final Integer SMALLER_MONTO_TOTAL = 1 - 1;

    private static final String DEFAULT_CORREO_RECEPTOR = "AAAAAAAAAA";
    private static final String UPDATED_CORREO_RECEPTOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/documentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private DocumentoMapper documentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentoMockMvc;

    private Documento documento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documento createEntity(EntityManager em) {
        Documento documento = new Documento()
            .rutEmpresa(DEFAULT_RUT_EMPRESA)
            .idAmbiente(DEFAULT_ID_AMBIENTE)
            .tipoDocumento(DEFAULT_TIPO_DOCUMENTO)
            .montoTotal(DEFAULT_MONTO_TOTAL)
            .correoReceptor(DEFAULT_CORREO_RECEPTOR);
        return documento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documento createUpdatedEntity(EntityManager em) {
        Documento documento = new Documento()
            .rutEmpresa(UPDATED_RUT_EMPRESA)
            .idAmbiente(UPDATED_ID_AMBIENTE)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .montoTotal(UPDATED_MONTO_TOTAL)
            .correoReceptor(UPDATED_CORREO_RECEPTOR);
        return documento;
    }

    @BeforeEach
    public void initTest() {
        documento = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumento() throws Exception {
        int databaseSizeBeforeCreate = documentoRepository.findAll().size();
        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);
        restDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentoDTO)))
            .andExpect(status().isCreated());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeCreate + 1);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getRutEmpresa()).isEqualTo(DEFAULT_RUT_EMPRESA);
        assertThat(testDocumento.getIdAmbiente()).isEqualTo(DEFAULT_ID_AMBIENTE);
        assertThat(testDocumento.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testDocumento.getMontoTotal()).isEqualTo(DEFAULT_MONTO_TOTAL);
        assertThat(testDocumento.getCorreoReceptor()).isEqualTo(DEFAULT_CORREO_RECEPTOR);
    }

    @Test
    @Transactional
    void createDocumentoWithExistingId() throws Exception {
        // Create the Documento with an existing ID
        documento.setId(1L);
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        int databaseSizeBeforeCreate = documentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentos() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
            .andExpect(jsonPath("$.[*].rutEmpresa").value(hasItem(DEFAULT_RUT_EMPRESA)))
            .andExpect(jsonPath("$.[*].idAmbiente").value(hasItem(DEFAULT_ID_AMBIENTE)))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].montoTotal").value(hasItem(DEFAULT_MONTO_TOTAL)))
            .andExpect(jsonPath("$.[*].correoReceptor").value(hasItem(DEFAULT_CORREO_RECEPTOR)));
    }

    @Test
    @Transactional
    void getDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get the documento
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL_ID, documento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documento.getId().intValue()))
            .andExpect(jsonPath("$.rutEmpresa").value(DEFAULT_RUT_EMPRESA))
            .andExpect(jsonPath("$.idAmbiente").value(DEFAULT_ID_AMBIENTE))
            .andExpect(jsonPath("$.tipoDocumento").value(DEFAULT_TIPO_DOCUMENTO))
            .andExpect(jsonPath("$.montoTotal").value(DEFAULT_MONTO_TOTAL))
            .andExpect(jsonPath("$.correoReceptor").value(DEFAULT_CORREO_RECEPTOR));
    }

    @Test
    @Transactional
    void getDocumentosByIdFiltering() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        Long id = documento.getId();

        defaultDocumentoShouldBeFound("id.equals=" + id);
        defaultDocumentoShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentoShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentosByRutEmpresaIsEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where rutEmpresa equals to DEFAULT_RUT_EMPRESA
        defaultDocumentoShouldBeFound("rutEmpresa.equals=" + DEFAULT_RUT_EMPRESA);

        // Get all the documentoList where rutEmpresa equals to UPDATED_RUT_EMPRESA
        defaultDocumentoShouldNotBeFound("rutEmpresa.equals=" + UPDATED_RUT_EMPRESA);
    }

    @Test
    @Transactional
    void getAllDocumentosByRutEmpresaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where rutEmpresa not equals to DEFAULT_RUT_EMPRESA
        defaultDocumentoShouldNotBeFound("rutEmpresa.notEquals=" + DEFAULT_RUT_EMPRESA);

        // Get all the documentoList where rutEmpresa not equals to UPDATED_RUT_EMPRESA
        defaultDocumentoShouldBeFound("rutEmpresa.notEquals=" + UPDATED_RUT_EMPRESA);
    }

    @Test
    @Transactional
    void getAllDocumentosByRutEmpresaIsInShouldWork() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where rutEmpresa in DEFAULT_RUT_EMPRESA or UPDATED_RUT_EMPRESA
        defaultDocumentoShouldBeFound("rutEmpresa.in=" + DEFAULT_RUT_EMPRESA + "," + UPDATED_RUT_EMPRESA);

        // Get all the documentoList where rutEmpresa equals to UPDATED_RUT_EMPRESA
        defaultDocumentoShouldNotBeFound("rutEmpresa.in=" + UPDATED_RUT_EMPRESA);
    }

    @Test
    @Transactional
    void getAllDocumentosByRutEmpresaIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where rutEmpresa is not null
        defaultDocumentoShouldBeFound("rutEmpresa.specified=true");

        // Get all the documentoList where rutEmpresa is null
        defaultDocumentoShouldNotBeFound("rutEmpresa.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByRutEmpresaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where rutEmpresa is greater than or equal to DEFAULT_RUT_EMPRESA
        defaultDocumentoShouldBeFound("rutEmpresa.greaterThanOrEqual=" + DEFAULT_RUT_EMPRESA);

        // Get all the documentoList where rutEmpresa is greater than or equal to UPDATED_RUT_EMPRESA
        defaultDocumentoShouldNotBeFound("rutEmpresa.greaterThanOrEqual=" + UPDATED_RUT_EMPRESA);
    }

    @Test
    @Transactional
    void getAllDocumentosByRutEmpresaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where rutEmpresa is less than or equal to DEFAULT_RUT_EMPRESA
        defaultDocumentoShouldBeFound("rutEmpresa.lessThanOrEqual=" + DEFAULT_RUT_EMPRESA);

        // Get all the documentoList where rutEmpresa is less than or equal to SMALLER_RUT_EMPRESA
        defaultDocumentoShouldNotBeFound("rutEmpresa.lessThanOrEqual=" + SMALLER_RUT_EMPRESA);
    }

    @Test
    @Transactional
    void getAllDocumentosByRutEmpresaIsLessThanSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where rutEmpresa is less than DEFAULT_RUT_EMPRESA
        defaultDocumentoShouldNotBeFound("rutEmpresa.lessThan=" + DEFAULT_RUT_EMPRESA);

        // Get all the documentoList where rutEmpresa is less than UPDATED_RUT_EMPRESA
        defaultDocumentoShouldBeFound("rutEmpresa.lessThan=" + UPDATED_RUT_EMPRESA);
    }

    @Test
    @Transactional
    void getAllDocumentosByRutEmpresaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where rutEmpresa is greater than DEFAULT_RUT_EMPRESA
        defaultDocumentoShouldNotBeFound("rutEmpresa.greaterThan=" + DEFAULT_RUT_EMPRESA);

        // Get all the documentoList where rutEmpresa is greater than SMALLER_RUT_EMPRESA
        defaultDocumentoShouldBeFound("rutEmpresa.greaterThan=" + SMALLER_RUT_EMPRESA);
    }

    @Test
    @Transactional
    void getAllDocumentosByIdAmbienteIsEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where idAmbiente equals to DEFAULT_ID_AMBIENTE
        defaultDocumentoShouldBeFound("idAmbiente.equals=" + DEFAULT_ID_AMBIENTE);

        // Get all the documentoList where idAmbiente equals to UPDATED_ID_AMBIENTE
        defaultDocumentoShouldNotBeFound("idAmbiente.equals=" + UPDATED_ID_AMBIENTE);
    }

    @Test
    @Transactional
    void getAllDocumentosByIdAmbienteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where idAmbiente not equals to DEFAULT_ID_AMBIENTE
        defaultDocumentoShouldNotBeFound("idAmbiente.notEquals=" + DEFAULT_ID_AMBIENTE);

        // Get all the documentoList where idAmbiente not equals to UPDATED_ID_AMBIENTE
        defaultDocumentoShouldBeFound("idAmbiente.notEquals=" + UPDATED_ID_AMBIENTE);
    }

    @Test
    @Transactional
    void getAllDocumentosByIdAmbienteIsInShouldWork() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where idAmbiente in DEFAULT_ID_AMBIENTE or UPDATED_ID_AMBIENTE
        defaultDocumentoShouldBeFound("idAmbiente.in=" + DEFAULT_ID_AMBIENTE + "," + UPDATED_ID_AMBIENTE);

        // Get all the documentoList where idAmbiente equals to UPDATED_ID_AMBIENTE
        defaultDocumentoShouldNotBeFound("idAmbiente.in=" + UPDATED_ID_AMBIENTE);
    }

    @Test
    @Transactional
    void getAllDocumentosByIdAmbienteIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where idAmbiente is not null
        defaultDocumentoShouldBeFound("idAmbiente.specified=true");

        // Get all the documentoList where idAmbiente is null
        defaultDocumentoShouldNotBeFound("idAmbiente.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByIdAmbienteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where idAmbiente is greater than or equal to DEFAULT_ID_AMBIENTE
        defaultDocumentoShouldBeFound("idAmbiente.greaterThanOrEqual=" + DEFAULT_ID_AMBIENTE);

        // Get all the documentoList where idAmbiente is greater than or equal to UPDATED_ID_AMBIENTE
        defaultDocumentoShouldNotBeFound("idAmbiente.greaterThanOrEqual=" + UPDATED_ID_AMBIENTE);
    }

    @Test
    @Transactional
    void getAllDocumentosByIdAmbienteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where idAmbiente is less than or equal to DEFAULT_ID_AMBIENTE
        defaultDocumentoShouldBeFound("idAmbiente.lessThanOrEqual=" + DEFAULT_ID_AMBIENTE);

        // Get all the documentoList where idAmbiente is less than or equal to SMALLER_ID_AMBIENTE
        defaultDocumentoShouldNotBeFound("idAmbiente.lessThanOrEqual=" + SMALLER_ID_AMBIENTE);
    }

    @Test
    @Transactional
    void getAllDocumentosByIdAmbienteIsLessThanSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where idAmbiente is less than DEFAULT_ID_AMBIENTE
        defaultDocumentoShouldNotBeFound("idAmbiente.lessThan=" + DEFAULT_ID_AMBIENTE);

        // Get all the documentoList where idAmbiente is less than UPDATED_ID_AMBIENTE
        defaultDocumentoShouldBeFound("idAmbiente.lessThan=" + UPDATED_ID_AMBIENTE);
    }

    @Test
    @Transactional
    void getAllDocumentosByIdAmbienteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where idAmbiente is greater than DEFAULT_ID_AMBIENTE
        defaultDocumentoShouldNotBeFound("idAmbiente.greaterThan=" + DEFAULT_ID_AMBIENTE);

        // Get all the documentoList where idAmbiente is greater than SMALLER_ID_AMBIENTE
        defaultDocumentoShouldBeFound("idAmbiente.greaterThan=" + SMALLER_ID_AMBIENTE);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipoDocumento equals to DEFAULT_TIPO_DOCUMENTO
        defaultDocumentoShouldBeFound("tipoDocumento.equals=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the documentoList where tipoDocumento equals to UPDATED_TIPO_DOCUMENTO
        defaultDocumentoShouldNotBeFound("tipoDocumento.equals=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoDocumentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipoDocumento not equals to DEFAULT_TIPO_DOCUMENTO
        defaultDocumentoShouldNotBeFound("tipoDocumento.notEquals=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the documentoList where tipoDocumento not equals to UPDATED_TIPO_DOCUMENTO
        defaultDocumentoShouldBeFound("tipoDocumento.notEquals=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipoDocumento in DEFAULT_TIPO_DOCUMENTO or UPDATED_TIPO_DOCUMENTO
        defaultDocumentoShouldBeFound("tipoDocumento.in=" + DEFAULT_TIPO_DOCUMENTO + "," + UPDATED_TIPO_DOCUMENTO);

        // Get all the documentoList where tipoDocumento equals to UPDATED_TIPO_DOCUMENTO
        defaultDocumentoShouldNotBeFound("tipoDocumento.in=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipoDocumento is not null
        defaultDocumentoShouldBeFound("tipoDocumento.specified=true");

        // Get all the documentoList where tipoDocumento is null
        defaultDocumentoShouldNotBeFound("tipoDocumento.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoDocumentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipoDocumento is greater than or equal to DEFAULT_TIPO_DOCUMENTO
        defaultDocumentoShouldBeFound("tipoDocumento.greaterThanOrEqual=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the documentoList where tipoDocumento is greater than or equal to UPDATED_TIPO_DOCUMENTO
        defaultDocumentoShouldNotBeFound("tipoDocumento.greaterThanOrEqual=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoDocumentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipoDocumento is less than or equal to DEFAULT_TIPO_DOCUMENTO
        defaultDocumentoShouldBeFound("tipoDocumento.lessThanOrEqual=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the documentoList where tipoDocumento is less than or equal to SMALLER_TIPO_DOCUMENTO
        defaultDocumentoShouldNotBeFound("tipoDocumento.lessThanOrEqual=" + SMALLER_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoDocumentoIsLessThanSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipoDocumento is less than DEFAULT_TIPO_DOCUMENTO
        defaultDocumentoShouldNotBeFound("tipoDocumento.lessThan=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the documentoList where tipoDocumento is less than UPDATED_TIPO_DOCUMENTO
        defaultDocumentoShouldBeFound("tipoDocumento.lessThan=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllDocumentosByTipoDocumentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where tipoDocumento is greater than DEFAULT_TIPO_DOCUMENTO
        defaultDocumentoShouldNotBeFound("tipoDocumento.greaterThan=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the documentoList where tipoDocumento is greater than SMALLER_TIPO_DOCUMENTO
        defaultDocumentoShouldBeFound("tipoDocumento.greaterThan=" + SMALLER_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllDocumentosByMontoTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where montoTotal equals to DEFAULT_MONTO_TOTAL
        defaultDocumentoShouldBeFound("montoTotal.equals=" + DEFAULT_MONTO_TOTAL);

        // Get all the documentoList where montoTotal equals to UPDATED_MONTO_TOTAL
        defaultDocumentoShouldNotBeFound("montoTotal.equals=" + UPDATED_MONTO_TOTAL);
    }

    @Test
    @Transactional
    void getAllDocumentosByMontoTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where montoTotal not equals to DEFAULT_MONTO_TOTAL
        defaultDocumentoShouldNotBeFound("montoTotal.notEquals=" + DEFAULT_MONTO_TOTAL);

        // Get all the documentoList where montoTotal not equals to UPDATED_MONTO_TOTAL
        defaultDocumentoShouldBeFound("montoTotal.notEquals=" + UPDATED_MONTO_TOTAL);
    }

    @Test
    @Transactional
    void getAllDocumentosByMontoTotalIsInShouldWork() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where montoTotal in DEFAULT_MONTO_TOTAL or UPDATED_MONTO_TOTAL
        defaultDocumentoShouldBeFound("montoTotal.in=" + DEFAULT_MONTO_TOTAL + "," + UPDATED_MONTO_TOTAL);

        // Get all the documentoList where montoTotal equals to UPDATED_MONTO_TOTAL
        defaultDocumentoShouldNotBeFound("montoTotal.in=" + UPDATED_MONTO_TOTAL);
    }

    @Test
    @Transactional
    void getAllDocumentosByMontoTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where montoTotal is not null
        defaultDocumentoShouldBeFound("montoTotal.specified=true");

        // Get all the documentoList where montoTotal is null
        defaultDocumentoShouldNotBeFound("montoTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByMontoTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where montoTotal is greater than or equal to DEFAULT_MONTO_TOTAL
        defaultDocumentoShouldBeFound("montoTotal.greaterThanOrEqual=" + DEFAULT_MONTO_TOTAL);

        // Get all the documentoList where montoTotal is greater than or equal to UPDATED_MONTO_TOTAL
        defaultDocumentoShouldNotBeFound("montoTotal.greaterThanOrEqual=" + UPDATED_MONTO_TOTAL);
    }

    @Test
    @Transactional
    void getAllDocumentosByMontoTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where montoTotal is less than or equal to DEFAULT_MONTO_TOTAL
        defaultDocumentoShouldBeFound("montoTotal.lessThanOrEqual=" + DEFAULT_MONTO_TOTAL);

        // Get all the documentoList where montoTotal is less than or equal to SMALLER_MONTO_TOTAL
        defaultDocumentoShouldNotBeFound("montoTotal.lessThanOrEqual=" + SMALLER_MONTO_TOTAL);
    }

    @Test
    @Transactional
    void getAllDocumentosByMontoTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where montoTotal is less than DEFAULT_MONTO_TOTAL
        defaultDocumentoShouldNotBeFound("montoTotal.lessThan=" + DEFAULT_MONTO_TOTAL);

        // Get all the documentoList where montoTotal is less than UPDATED_MONTO_TOTAL
        defaultDocumentoShouldBeFound("montoTotal.lessThan=" + UPDATED_MONTO_TOTAL);
    }

    @Test
    @Transactional
    void getAllDocumentosByMontoTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where montoTotal is greater than DEFAULT_MONTO_TOTAL
        defaultDocumentoShouldNotBeFound("montoTotal.greaterThan=" + DEFAULT_MONTO_TOTAL);

        // Get all the documentoList where montoTotal is greater than SMALLER_MONTO_TOTAL
        defaultDocumentoShouldBeFound("montoTotal.greaterThan=" + SMALLER_MONTO_TOTAL);
    }

    @Test
    @Transactional
    void getAllDocumentosByCorreoReceptorIsEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where correoReceptor equals to DEFAULT_CORREO_RECEPTOR
        defaultDocumentoShouldBeFound("correoReceptor.equals=" + DEFAULT_CORREO_RECEPTOR);

        // Get all the documentoList where correoReceptor equals to UPDATED_CORREO_RECEPTOR
        defaultDocumentoShouldNotBeFound("correoReceptor.equals=" + UPDATED_CORREO_RECEPTOR);
    }

    @Test
    @Transactional
    void getAllDocumentosByCorreoReceptorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where correoReceptor not equals to DEFAULT_CORREO_RECEPTOR
        defaultDocumentoShouldNotBeFound("correoReceptor.notEquals=" + DEFAULT_CORREO_RECEPTOR);

        // Get all the documentoList where correoReceptor not equals to UPDATED_CORREO_RECEPTOR
        defaultDocumentoShouldBeFound("correoReceptor.notEquals=" + UPDATED_CORREO_RECEPTOR);
    }

    @Test
    @Transactional
    void getAllDocumentosByCorreoReceptorIsInShouldWork() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where correoReceptor in DEFAULT_CORREO_RECEPTOR or UPDATED_CORREO_RECEPTOR
        defaultDocumentoShouldBeFound("correoReceptor.in=" + DEFAULT_CORREO_RECEPTOR + "," + UPDATED_CORREO_RECEPTOR);

        // Get all the documentoList where correoReceptor equals to UPDATED_CORREO_RECEPTOR
        defaultDocumentoShouldNotBeFound("correoReceptor.in=" + UPDATED_CORREO_RECEPTOR);
    }

    @Test
    @Transactional
    void getAllDocumentosByCorreoReceptorIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where correoReceptor is not null
        defaultDocumentoShouldBeFound("correoReceptor.specified=true");

        // Get all the documentoList where correoReceptor is null
        defaultDocumentoShouldNotBeFound("correoReceptor.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentosByCorreoReceptorContainsSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where correoReceptor contains DEFAULT_CORREO_RECEPTOR
        defaultDocumentoShouldBeFound("correoReceptor.contains=" + DEFAULT_CORREO_RECEPTOR);

        // Get all the documentoList where correoReceptor contains UPDATED_CORREO_RECEPTOR
        defaultDocumentoShouldNotBeFound("correoReceptor.contains=" + UPDATED_CORREO_RECEPTOR);
    }

    @Test
    @Transactional
    void getAllDocumentosByCorreoReceptorNotContainsSomething() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentoList where correoReceptor does not contain DEFAULT_CORREO_RECEPTOR
        defaultDocumentoShouldNotBeFound("correoReceptor.doesNotContain=" + DEFAULT_CORREO_RECEPTOR);

        // Get all the documentoList where correoReceptor does not contain UPDATED_CORREO_RECEPTOR
        defaultDocumentoShouldBeFound("correoReceptor.doesNotContain=" + UPDATED_CORREO_RECEPTOR);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentoShouldBeFound(String filter) throws Exception {
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
            .andExpect(jsonPath("$.[*].rutEmpresa").value(hasItem(DEFAULT_RUT_EMPRESA)))
            .andExpect(jsonPath("$.[*].idAmbiente").value(hasItem(DEFAULT_ID_AMBIENTE)))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].montoTotal").value(hasItem(DEFAULT_MONTO_TOTAL)))
            .andExpect(jsonPath("$.[*].correoReceptor").value(hasItem(DEFAULT_CORREO_RECEPTOR)));

        // Check, that the count call also returns 1
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentoShouldNotBeFound(String filter) throws Exception {
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumento() throws Exception {
        // Get the documento
        restDocumentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Update the documento
        Documento updatedDocumento = documentoRepository.findById(documento.getId()).get();
        // Disconnect from session so that the updates on updatedDocumento are not directly saved in db
        em.detach(updatedDocumento);
        updatedDocumento
            .rutEmpresa(UPDATED_RUT_EMPRESA)
            .idAmbiente(UPDATED_ID_AMBIENTE)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .montoTotal(UPDATED_MONTO_TOTAL)
            .correoReceptor(UPDATED_CORREO_RECEPTOR);
        DocumentoDTO documentoDTO = documentoMapper.toDto(updatedDocumento);

        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getRutEmpresa()).isEqualTo(UPDATED_RUT_EMPRESA);
        assertThat(testDocumento.getIdAmbiente()).isEqualTo(UPDATED_ID_AMBIENTE);
        assertThat(testDocumento.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testDocumento.getMontoTotal()).isEqualTo(UPDATED_MONTO_TOTAL);
        assertThat(testDocumento.getCorreoReceptor()).isEqualTo(UPDATED_CORREO_RECEPTOR);
    }

    @Test
    @Transactional
    void putNonExistingDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentoWithPatch() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Update the documento using partial update
        Documento partialUpdatedDocumento = new Documento();
        partialUpdatedDocumento.setId(documento.getId());

        partialUpdatedDocumento.idAmbiente(UPDATED_ID_AMBIENTE);

        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumento))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getRutEmpresa()).isEqualTo(DEFAULT_RUT_EMPRESA);
        assertThat(testDocumento.getIdAmbiente()).isEqualTo(UPDATED_ID_AMBIENTE);
        assertThat(testDocumento.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testDocumento.getMontoTotal()).isEqualTo(DEFAULT_MONTO_TOTAL);
        assertThat(testDocumento.getCorreoReceptor()).isEqualTo(DEFAULT_CORREO_RECEPTOR);
    }

    @Test
    @Transactional
    void fullUpdateDocumentoWithPatch() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Update the documento using partial update
        Documento partialUpdatedDocumento = new Documento();
        partialUpdatedDocumento.setId(documento.getId());

        partialUpdatedDocumento
            .rutEmpresa(UPDATED_RUT_EMPRESA)
            .idAmbiente(UPDATED_ID_AMBIENTE)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .montoTotal(UPDATED_MONTO_TOTAL)
            .correoReceptor(UPDATED_CORREO_RECEPTOR);

        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumento))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
        Documento testDocumento = documentoList.get(documentoList.size() - 1);
        assertThat(testDocumento.getRutEmpresa()).isEqualTo(UPDATED_RUT_EMPRESA);
        assertThat(testDocumento.getIdAmbiente()).isEqualTo(UPDATED_ID_AMBIENTE);
        assertThat(testDocumento.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testDocumento.getMontoTotal()).isEqualTo(UPDATED_MONTO_TOTAL);
        assertThat(testDocumento.getCorreoReceptor()).isEqualTo(UPDATED_CORREO_RECEPTOR);
    }

    @Test
    @Transactional
    void patchNonExistingDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumento() throws Exception {
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();
        documento.setId(count.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documento in the database
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        int databaseSizeBeforeDelete = documentoRepository.findAll().size();

        // Delete the documento
        restDocumentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, documento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Documento> documentoList = documentoRepository.findAll();
        assertThat(documentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
