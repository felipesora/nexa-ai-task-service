package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.CorWorkspaceControllerMapper;
import com.nexa.task.application.mapper.IconeWorkspaceControllerMapper;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.application.usecase.workspace.BuscarWorkspacePorIdUseCase;
import com.nexa.task.application.usecase.workspace.CadastrarWorkspaceUseCase;
import com.nexa.task.application.usecase.workspace.ListarTodosWorkspacesUseCase;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.persistence.adapter.JpaWorkspaceRepository;
import com.nexa.task.infra.persistence.mapper.CorWorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.mapper.IconeWorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.mapper.WorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataWorkspaceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkspaceBeanConfig {

    @Bean
    CadastrarWorkspaceUseCase cadastrarWorkspaceUseCase(WorkspaceRepository workspaceRepository, CorWorkspaceRepository corWorkspaceRepository,
                                                        IconeWorkspaceRepository iconeWorkspaceRepository, WorkspaceControllerMapper mapper) {
        return new CadastrarWorkspaceUseCase(workspaceRepository, corWorkspaceRepository, iconeWorkspaceRepository, mapper);
    }

    @Bean
    ListarTodosWorkspacesUseCase listarTodosWorkspacesUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper) {
        return new ListarTodosWorkspacesUseCase(workspaceRepository, mapper);
    }

    @Bean
    BuscarWorkspacePorIdUseCase buscarWorkspacePorIdUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper) {
        return new BuscarWorkspacePorIdUseCase(workspaceRepository, mapper);
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
