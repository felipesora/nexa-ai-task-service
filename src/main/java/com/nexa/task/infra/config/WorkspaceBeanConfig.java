package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.CorWorkspaceControllerMapper;
import com.nexa.task.application.mapper.IconeWorkspaceControllerMapper;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.application.usecase.workspace.*;
import com.nexa.task.domain.repository.*;
import com.nexa.task.infra.persistence.adapter.JpaWorkspaceRepository;
import com.nexa.task.infra.persistence.mapper.CorWorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.mapper.IconeWorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.mapper.WorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataWorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkspaceBeanConfig {

    @Bean
    CadastrarWorkspaceUseCase cadastrarWorkspaceUseCase(WorkspaceRepository workspaceRepository, CorWorkspaceRepository corWorkspaceRepository,
                                                        IconeWorkspaceRepository iconeWorkspaceRepository, WorkspaceControllerMapper mapper,
                                                        AuthenticationService authService) {
        return new CadastrarWorkspaceUseCase(workspaceRepository, corWorkspaceRepository, iconeWorkspaceRepository, mapper, authService);
    }

    @Bean
    ListarTodosWorkspacesUseCase listarTodosWorkspacesUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper) {
        return new ListarTodosWorkspacesUseCase(workspaceRepository, mapper);
    }

    @Bean
    BuscarWorkspacePorIdUseCase buscarWorkspacePorIdUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper, AuthenticationService authService) {
        return new BuscarWorkspacePorIdUseCase(workspaceRepository, mapper, authService);
    }

    @Bean
    ListarWorkspacesPorIdUsuarioUseCase listarWorkspacesPorIdUsuarioUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper, AuthenticationService authService) {
        return new ListarWorkspacesPorIdUsuarioUseCase(workspaceRepository, mapper, authService);
    }

    @Bean
    ListarWorkspacesPorIdUsuarioENomeUseCase listarWorkspacesPorIdUsuarioENomeUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper, AuthenticationService authService) {
        return new ListarWorkspacesPorIdUsuarioENomeUseCase(workspaceRepository, mapper, authService);
    }

    @Bean
    AtualizarWorkspaceUseCase atualizarWorkspaceUseCase(WorkspaceRepository workspaceRepository, CorWorkspaceRepository corWorkspaceRepository,
                                                        IconeWorkspaceRepository iconeWorkspaceRepository, AuthenticationService authService) {
        return new AtualizarWorkspaceUseCase(workspaceRepository, corWorkspaceRepository, iconeWorkspaceRepository, authService);
    }

    @Bean
    DesativarWorkspaceUseCase desativarWorkspaceUseCase(WorkspaceRepository workspaceRepository,
                                                        TarefaRepository tarefaRepository,
                                                        SubtarefaRepository subtarefaRepository,
                                                        AuthenticationService authService) {
        return new DesativarWorkspaceUseCase(workspaceRepository, tarefaRepository, subtarefaRepository, authService);
    }

    @Bean
    AtivarWorkspaceUseCase ativarWorkspaceUseCase(WorkspaceRepository workspaceRepository) {
        return new AtivarWorkspaceUseCase(workspaceRepository);
    }

    @Bean
    JpaWorkspaceRepository jpaWorkspaceRepository(SpringDataWorkspaceRepository repository, WorkspacePersistenceMapper mapper) {
        return new JpaWorkspaceRepository(repository, mapper);
    }

    @Bean
    WorkspaceControllerMapper workspaceControllerMapper(CorWorkspaceControllerMapper corWorkspaceMapper, IconeWorkspaceControllerMapper iconeWorkspaceMapper) {
        return new WorkspaceControllerMapper(corWorkspaceMapper, iconeWorkspaceMapper);
    }

    @Bean
    WorkspacePersistenceMapper workspacePersistenceMapper(CorWorkspacePersistenceMapper corWorkspaceMapper, IconeWorkspacePersistenceMapper iconeWorkspaceMapper) {
        return new WorkspacePersistenceMapper(corWorkspaceMapper, iconeWorkspaceMapper);
    }
}
