package com.nexa.task.domain.entity.tarefa;

import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TarefaTest {


    @Test
    void devePermitirCadastrarTarefaSeTodosOsAtributosEstiveremCorretos() {
        Tarefa tarefa = new TarefaBuilder().build();

        assertNotNull(tarefa);
        assertEquals(1L, tarefa.getId());
        assertEquals(1L, tarefa.getIdUsuario());
        assertEquals("Título da tarefa", tarefa.getTitulo());
        assertEquals("Descrição da tarefa", tarefa.getDescricao());
        assertEquals(PrioridadeTarefa.BAIXA, tarefa.getPrioridade());
        assertEquals(StatusTarefa.EM_ANDAMENTO, tarefa.getStatus());
        assertEquals(DificuldadeTarefa.BAIXA, tarefa.getDificuldade());
        assertTrue(tarefa.getAtivo());
        assertEquals(1L, tarefa.getWorkspace().getId());
    }

    @Test
    void deveLancarDomainExceptionCasoIdUsuarioEstejaVazioOuIgualAZero() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new TarefaBuilder().comIdUsuario(null).build());

        assertEquals("Id do Usuário da tarefa é obrigatório.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoTituloEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new TarefaBuilder().comTitulo(null).build());

        assertEquals("Título da tarefa é obrigatório.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoDescricaoEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new TarefaBuilder().comDescricao(null).build());

        assertEquals("Descrição da tarefa é obrigatória.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoPrioridadeEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new TarefaBuilder().comPrioridade(null).build());

        assertEquals("Prioridade da tarefa é obrigatória.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoStatusEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new TarefaBuilder().comStatus(null).build());

        assertEquals("Status da tarefa é obrigatório.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoDificuldadeEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new TarefaBuilder().comDificuldade(null).build());

        assertEquals("Dificuldade da tarefa é obrigatório.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoWorkspaceEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new TarefaBuilder().comWorkspace(null).build());

        assertEquals("Workspace da tarefa é obrigatório.", exception.getMessage());
    }

    @Test
    void deveAlterarStatusParaAtivoQuandoAtivarTarefa() {

        Tarefa tarefa = new TarefaBuilder().comAtivo(false).build();

        tarefa.ativar();

        assertTrue(tarefa.getAtivo());
    }

    @Test
    void deveAlterarStatusParaInativoQuandoDesativarTarefa() {

        Tarefa tarefa = new TarefaBuilder().comAtivo(true).build();

        tarefa.desativar();

        assertFalse(tarefa.getAtivo());
    }
}