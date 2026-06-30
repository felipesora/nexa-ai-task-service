package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.application.usecase.tarefa.*;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.persistence.adapter.JpaTarefaRepository;
import com.nexa.task.infra.persistence.mapper.TagPersistenceMapper;
import com.nexa.task.infra.persistence.mapper.TarefaPersistenceMapper;
import com.nexa.task.infra.persistence.mapper.WorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataTarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
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
                                                                          WorkspaceRepository workspaceRepository,
                                                                          TarefaControllerMapper mapper,
                                                                          AuthenticationService authService) {
        return new ListarTarefasPorIdWorkspaceUseCase(tarefaRepository, workspaceRepository, mapper, authService);
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
    IniciarTarefaUseCase iniciarTarefaUseCase(TarefaRepository repository) {
        return new IniciarTarefaUseCase(repository);
    }

    @Bean
    ReabrirTarefaUseCase reabrirTarefaUseCase(TarefaRepository repository) {
        return new ReabrirTarefaUseCase(repository);
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
    AdicionarTagNaTarefaUseCase adicionarTagNaTarefaUseCase(TagRepository tagRepository, TarefaRepository tarefaRepository) {
        return new AdicionarTagNaTarefaUseCase(tagRepository, tarefaRepository);
    }

    @Bean
    RemoverTagDaTarefaUseCase removerTagDaTarefaUseCase(TagRepository tagRepository, TarefaRepository tarefaRepository) {
        return new RemoverTagDaTarefaUseCase(tagRepository, tarefaRepository);
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
    TarefaPersistenceMapper tarefaPersistenceMapper(WorkspacePersistenceMapper workspaceMapper, TagPersistenceMapper tagMapper) {
        return new TarefaPersistenceMapper(workspaceMapper, tagMapper);
    }
}
