package com.nexa.task.domain.entity.tarefa;

/**
 * Filtros opcionais da listagem de tarefas. Campo nulo = filtro não aplicado.
 */
public record FiltroTarefas(
        String titulo,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        Long idTag
) {

    public FiltroTarefas {
        // Título em branco conta como "sem filtro"
        titulo = (titulo == null || titulo.isBlank()) ? null : titulo.trim();
    }

    public static FiltroTarefas vazio() {
        return new FiltroTarefas(null, null, null, null);
    }
}
