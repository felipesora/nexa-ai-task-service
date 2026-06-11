package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.application.usecase.tarefa.CadastrarTarefaUseCase;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.persistence.adapter.JpaTarefaRepository;
import com.nexa.task.infra.persistence.mapper.TarefaPersistenceMapper;
import com.nexa.task.infra.persistence.mapper.WorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataTarefaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TarefaBeanConfig {

    @Bean
    CadastrarTarefaUseCase cadastrarTarefaUseCase(TarefaRepository tarefaRepository,
                                                  WorkspaceRepository workspaceRepository,
                                                  TarefaControllerMapper mapper) {
        return new CadastrarTarefaUseCase(tarefaRepository, workspaceRepository, mapper);
    }

    @Bean
    TarefaControllerMapper tarefaControllerMapper() {
        return new TarefaControllerMapper();
    }

    @Bean
    JpaTarefaRepository jpaTarefaRepository(SpringDataTarefaRepository repository, TarefaPersistenceMapper mapper) {
        return new JpaTarefaRepository(repository, mapper);
    }

    @Bean
    TarefaPersistenceMapper tarefaPersistenceMapper(WorkspacePersistenceMapper workspaceMapper) {
        return new TarefaPersistenceMapper(workspaceMapper);
    }
}
