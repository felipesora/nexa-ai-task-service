package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.application.usecase.tarefa.*;
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
    BuscarTarefaPorIdUseCase buscarTarefaPorIdUseCase(TarefaRepository tarefaRepository,
                                                      TarefaControllerMapper mapper) {
        return new BuscarTarefaPorIdUseCase(tarefaRepository, mapper);
    }

    @Bean
    ListarTodasTarefasUseCase listarTodasTarefasUseCase(TarefaRepository tarefaRepository,
                                                        TarefaControllerMapper mapper) {
        return new ListarTodasTarefasUseCase(tarefaRepository, mapper);
    }

    @Bean
    ListarTarefasPorIdWorkspaceUseCase listarTarefasPorIdWorkspaceUseCase(TarefaRepository tarefaRepository,
                                                                          TarefaControllerMapper mapper) {
        return new ListarTarefasPorIdWorkspaceUseCase(tarefaRepository, mapper);
    }

    @Bean
    ListarTarefasPorIdUsuarioUseCase listarTarefasPorIdUsuarioUseCase(TarefaRepository tarefaRepository,
                                                                      TarefaControllerMapper mapper) {
        return new ListarTarefasPorIdUsuarioUseCase(tarefaRepository, mapper);
    }

    @Bean
    ListarTarefasPorIdUsuarioETituloUseCase listarTarefasPorIdUsuarioETituloUseCase(TarefaRepository tarefaRepository,
                                                                                     TarefaControllerMapper mapper) {
        return new ListarTarefasPorIdUsuarioETituloUseCase(tarefaRepository, mapper);
    }

    @Bean
    AtualizarTarefaUseCase atualizarTarefaUseCase(TarefaRepository repository) {
        return new AtualizarTarefaUseCase(repository);
    }

    @Bean
    ConcluirTarefaUseCase concluirTarefaUseCase(TarefaRepository repository) {
        return new ConcluirTarefaUseCase(repository);
    }

    @Bean
    DesativarTarefaUseCase desativarTarefaUseCase(TarefaRepository tarefaRepository) {
        return new DesativarTarefaUseCase(tarefaRepository);
    }

    @Bean
    AtivarTarefaUseCase ativarTarefaUseCase(TarefaRepository tarefaRepository) {
        return new AtivarTarefaUseCase(tarefaRepository);
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
