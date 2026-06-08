package com.nexa.task.domain.entity.workspace;

import com.nexa.task.domain.builder.workspace.IconeWorkspaceBuilder;
import com.nexa.task.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IconeWorkspaceTest {

    @Test
    void devePermitirCadastrarIconeDoWorkspaceSeTodosOsAtributosEstiveremCorretos() {
        IconeWorkspace iconeWorkspace = new IconeWorkspaceBuilder().build();

        assertNotNull(iconeWorkspace);
        assertEquals(1L, iconeWorkspace.getId());
        assertEquals("Ícone Padrão", iconeWorkspace.getNome());
        assertEquals("padrao.png", iconeWorkspace.getCaminho());
        assertTrue(iconeWorkspace.getAtivo());
    }

    @Test
    void deveLancarDomainExceptionCasoNomeDoIconeEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new IconeWorkspaceBuilder().comNome(null).build());

        assertEquals("Nome do ícone é obrigatório.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoCaminhoDoIconeEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new IconeWorkspaceBuilder().comCaminho(null).build());

        assertEquals("Caminho do ícone é obrigatório.", exception.getMessage());
    }

    @Test
    void deveAlterarStatusParaAtivoQuandoAtivarCor() {

        IconeWorkspace iconeWorkspace = new IconeWorkspaceBuilder().comAtivo(false).build();

        iconeWorkspace.ativar();

        assertTrue(iconeWorkspace.getAtivo());
    }

    @Test
    void deveAlterarStatusParaInativoQuandoDesativarCor() {

        IconeWorkspace iconeWorkspace = new IconeWorkspaceBuilder().comAtivo(true).build();

        iconeWorkspace.desativar();

        assertFalse(iconeWorkspace.getAtivo());
    }
}