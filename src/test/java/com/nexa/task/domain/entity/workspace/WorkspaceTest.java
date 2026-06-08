package com.nexa.task.domain.entity.workspace;

import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkspaceTest {

    @Test
    void devePermitirCadastrarWorkspaceSeTodosOsAtributosEstiveremCorretos() {
        Workspace workspace = new WorkspaceBuilder().build();

        assertNotNull(workspace);
        assertEquals(1L, workspace.getId());
        assertEquals(1L, workspace.getIdUsuario());
        assertEquals("Nome do workspace", workspace.getNome());
        assertEquals("Descrição do workspace", workspace.getDescricao());
        assertTrue(workspace.getAtivo());
        assertEquals(1L, workspace.getIconeWorkspace().getId());
        assertEquals(1L, workspace.getCorWorkspace().getId());
    }

    @Test
    void deveLancarDomainExceptionCasoIdUsuarioEstejaVazioOuIgualAZero() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new WorkspaceBuilder().comIdUsuario(null).build());

        assertEquals("Id do Usuário do workspace é obrigatório.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoTituloEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new WorkspaceBuilder().comNome(null).build());

        assertEquals("Nome do workspace é obrigatório.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoDescricaoEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new WorkspaceBuilder().comDescricao(null).build());

        assertEquals("Descrição do workspace é obrigatória.", exception.getMessage());
    }

    @Test
    void deveAlterarStatusParaAtivoQuandoAtivarWorkspace() {

        Workspace workspace = new WorkspaceBuilder().comAtivo(false).build();

        workspace.ativar();

        assertTrue(workspace.getAtivo());
    }

    @Test
    void deveAlterarStatusParaInativoQuandoDesativarWorkspace() {

        Workspace workspace = new WorkspaceBuilder().comAtivo(true).build();

        workspace.desativar();

        assertFalse(workspace.getAtivo());
    }
}