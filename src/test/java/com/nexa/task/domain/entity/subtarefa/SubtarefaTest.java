package com.nexa.task.domain.entity.subtarefa;

import com.nexa.task.domain.builder.subtarefa.SubtarefaBuilder;
import com.nexa.task.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtarefaTest {

    @Test
    void devePermitirCadastrarSubtarefaSeTodosOsAtributosEstiveremCorretos() {
        Subtarefa subtarefa = new SubtarefaBuilder().build();

        assertNotNull(subtarefa);
        assertEquals(1L, subtarefa.getId());
        assertEquals("Título da subtarefa", subtarefa.getTitulo());
        assertFalse(subtarefa.getConcluida());
        assertTrue(subtarefa.getAtivo());
        assertEquals(1L, subtarefa.getTarefa().getId());
    }

    @Test
    void deveLancarDomainExceptionCasoTituloEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new SubtarefaBuilder().comTitulo(null).build());

        assertEquals("Título da subtarefa é obrigatório.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoWorkspaceEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new SubtarefaBuilder().comTarefa(null).build());

        assertEquals("Tarefa da subtarefa é obrigatório.", exception.getMessage());
    }

    @Test
    void deveConcluirTarefaAoUsarMetodoMarcarConluido() {
        Subtarefa subtarefa = new SubtarefaBuilder().comConcluida(false).build();

        subtarefa.marcarConcluida();

        assertTrue(subtarefa.getConcluida());
    }

    @Test
    void deveDesmarcarConcluirTarefaAoUsarMetodoDesmarcarConluido() {
        Subtarefa subtarefa = new SubtarefaBuilder().comConcluida(true).build();

        subtarefa.desmarcarConcluida();

        assertFalse(subtarefa.getConcluida());
    }

    @Test
    void deveAlterarStatusParaAtivoQuandoAtivarSubtarefa() {

        Subtarefa subtarefa = new SubtarefaBuilder().comAtivo(false).build();

        subtarefa.ativar();

        assertTrue(subtarefa.getAtivo());
    }

    @Test
    void deveAlterarStatusParaInativoQuandoDesativarSubtarefa() {

        Subtarefa subtarefa = new SubtarefaBuilder().comAtivo(true).build();

        subtarefa.desativar();

        assertFalse(subtarefa.getAtivo());
    }
}