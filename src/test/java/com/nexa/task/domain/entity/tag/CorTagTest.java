package com.nexa.task.domain.entity.tag;

import com.nexa.task.domain.builder.tag.CorTagBuilder;
import com.nexa.task.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorTagTest {

    @Test
    void devePermitirCadastrarCorDaTagSeTodosOsAtributosEstiveremCorretos() {
        CorTag corTag = new CorTagBuilder().build();

        assertNotNull(corTag);
        assertEquals(1L, corTag.getId());
        assertEquals("#FFFFFF", corTag.getCor());
        assertTrue(corTag.getAtivo());
    }

    @Test
        void deveLancarDomainExceptionCasoCorEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new CorTagBuilder().comCor(null).build());

        assertEquals("Cor da tag é obrigatório.", exception.getMessage());
    }

    @Test
    void deveAlterarStatusParaAtivoQuandoAtivarCorDaTag() {

        CorTag corTag = new CorTagBuilder().comAtivo(false).build();

        corTag.ativar();

        assertTrue(corTag.getAtivo());
    }

    @Test
    void deveAlterarStatusParaInativoQuandoDesativarCorDaTag() {

        CorTag corTag = new CorTagBuilder().comAtivo(true).build();

        corTag.desativar();

        assertFalse(corTag.getAtivo());
    }
}