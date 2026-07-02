package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.application.usecase.tarefa.*;
import com.nexa.task.domain.repository.SubtarefaRepository;
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
                                                  TarefaControllerMapper mapper,
                                                  AuthenticationService authService) {
        return new CadastrarTarefaUseCase(tarefaRepository, workspaceRepository, mapper, authService);
    }

    @Bean
    BuscarTarefaPorIdUseCase buscarTarefaPorIdUseCase(TarefaRepository tarefaRepository,
                                                      TarefaControllerMapper mapper,
                                                      AuthenticationService authService) {
        return new BuscarTarefaPorIdUseCase(tarefaRepository, mapper, authService);
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
                                                                      TarefaControllerMapper mapper,
                                                                      AuthenticationService authService) {
        return new ListarTarefasPorIdUsuarioUseCase(tarefaRepository, mapper, authService);
    }

    @Bean
    ListarTarefasPorIdUsuarioETituloUseCase listarTarefasPorIdUsuarioETituloUseCase(TarefaRepository tarefaRepository,
                                                                                     TarefaControllerMapper mapper,
                                                                                    AuthenticationService authService) {
        return new ListarTarefasPorIdUsuarioETituloUseCase(tarefaRepository, mapper, authService);
    }

    @Bean
    AtualizarTarefaUseCase atualizarTarefaUseCase(TarefaRepository repository,
                                                  AuthenticationService authService) {
        return new AtualizarTarefaUseCase(repository, authService);
    }

    @Bean
    ConcluirTarefaUseCase concluirTarefaUseCase(TarefaRepository repository, AuthenticationService authService) {
        return new ConcluirTarefaUseCase(repository, authService);
    }

    @Bean
    IniciarTarefaUseCase iniciarTarefaUseCase(TarefaRepository repository, AuthenticationService authService) {
        return new IniciarTarefaUseCase(repository, authService);
    }

    @Bean
    ReabrirTarefaUseCase reabrirTarefaUseCase(TarefaRepository repository, AuthenticationService authService) {
        return new ReabrirTarefaUseCase(repository, authService);
    }

    @Bean
    DesativarTarefaUseCase desativarTarefaUseCase(TarefaRepository tarefaRepository,
                                                  SubtarefaRepository subtarefaRepository,
                                                  AuthenticationService authService) {
        return new DesativarTarefaUseCase(tarefaRepository, subtarefaRepository, authService);
    }

    @Bean
    AtivarTarefaUseCase ativarTarefaUseCase(TarefaRepository tarefaRepository) {
        return new AtivarTarefaUseCase(tarefaRepository);
    }

    @Bean
    AdicionarTagNaTarefaUseCase adicionarTagNaTarefaUseCase(TagRepository tagRepository, TarefaRepository tarefaRepository,
                                                            AuthenticationService authService) {
        return new AdicionarTagNaTarefaUseCase(tagRepository, tarefaRepository, authService);
    }

    @Bean
    RemoverTagDaTarefaUseCase removerTagDaTarefaUseCase(TagRepository tagRepository, TarefaRepository tarefaRepository, AuthenticationService authService) {
        return new RemoverTagDaTarefaUseCase(tagRepository, tarefaRepository, authService);
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
