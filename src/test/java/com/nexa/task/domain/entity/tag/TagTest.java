package com.nexa.task.domain.entity.tag;

import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    @Test
    void devePermitirCadastrarTagSeTodosOsAtributosEstiveremCorretos() {
        Tag tag = new TagBuilder().build();

        assertNotNull(tag);
        assertEquals(1L, tag.getId());
        assertEquals(1L, tag.getIdUsuario());
        assertEquals("Tag Padrão", tag.getNome());
        assertTrue(tag.getAtivo());
    }

    @Test
    void deveLancarDomainExceptionCasoIdUsuarioEstejaVazioOuIgualAZero() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new TagBuilder().comIdUsuario(null).build());

        assertEquals("Id do Usuário da tag é obrigatório.", exception.getMessage());
    }

    @Test
    void deveLancarDomainExceptionCasoTituloEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new TagBuilder().comNome(null).build());

        assertEquals("Nome da tag é obrigatório.", exception.getMessage());
    }

    @Test
    void deveAlterarStatusParaAtivoQuandoAtivarTag() {

        Tag tag = new TagBuilder().comAtivo(false).build();

        tag.ativar();

        assertTrue(tag.getAtivo());
    }

    @Test
    void deveAlterarStatusParaInativoQuandoDesativarTag() {

        Tag tag = new TagBuilder().comAtivo(true).build();

        tag.desativar();

        assertFalse(tag.getAtivo());
    }
}