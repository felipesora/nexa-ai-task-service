package com.nexa.task.domain.entity.tarefa;

import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tag.Tag;
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

    @Test
    void deveAdicionarTagNaTarefa() {

        Tag tag = new TagBuilder().build();

        Tarefa tarefa = new TarefaBuilder().build();

        tarefa.adicionarTag(tag);

        assertEquals(1, tarefa.getTags().size());
        assertTrue(tarefa.getTags().contains(tag));
    }

    @Test
    void deveLancarDomainExceptionAoAdicionarTagNula() {

        Tarefa tarefa = new TarefaBuilder().build();

        DomainException exception = assertThrows(
                DomainException.class,
                () -> tarefa.adicionarTag(null)
        );

        assertEquals("Tag é obrigatória.", exception.getMessage());
    }

    @Test
    void deveRemoverTagPeloId() {

        Tag tag1 = new TagBuilder()
                .comId(1L)
                .build();

        Tag tag2 = new TagBuilder()
                .comId(2L)
                .build();

        Tarefa tarefa = new TarefaBuilder()
                .adicionarTag(tag1)
                .adicionarTag(tag2)
                .build();

        tarefa.removerTagPeloId(1L);

        assertEquals(1, tarefa.getTags().size());
        assertEquals(2L, tarefa.getTags().get(0).getId());
    }
}