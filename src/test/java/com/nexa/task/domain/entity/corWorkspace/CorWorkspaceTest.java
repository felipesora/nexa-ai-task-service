package com.nexa.task.domain.entity.corWorkspace;

import com.nexa.task.domain.builder.corWorkspace.CorWorkspaceBuilder;
import com.nexa.task.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorWorkspaceTest {

    @Test
    void devePermitirCadastrarCorDoWorkspaceSeTodosOsAtributosEstiveremCorretos() {
        CorWorkspace corWorkspace = new CorWorkspaceBuilder().build();

        assertNotNull(corWorkspace);
        assertEquals(1L, corWorkspace.getId());
        assertEquals("#FFFFFF", corWorkspace.getCor());
        assertTrue(corWorkspace.getAtivo());
    }

    @Test
    void deveLancarDomainExceptionCasoCorEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new CorWorkspaceBuilder().comCor(null).build());

        assertEquals("O valor da cor é obrigatório.", exception.getMessage());
    }

    @Test
    void deveAlterarStatusParaAtivoQuandoAtivarCor() {

        CorWorkspace corWorkspace = new CorWorkspaceBuilder().comAtivo(false).build();

        corWorkspace.ativar();

        assertTrue(corWorkspace.getAtivo());
    }

    @Test
    void deveAlterarStatusParaInativoQuandoDesativarCor() {

        CorWorkspace corWorkspace = new CorWorkspaceBuilder().comAtivo(true).build();

        corWorkspace.desativar();

        assertFalse(corWorkspace.getAtivo());
    }
}