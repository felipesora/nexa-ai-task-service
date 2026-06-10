package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.CorWorkspaceControllerMapper;
import com.nexa.task.application.usecase.corWorkspace.*;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.infra.persistence.adapter.JpaCorWorkspaceRepository;
import com.nexa.task.infra.persistence.mapper.CorWorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataCorWorkspaceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorWorkspaceBeanConfig {

    @Bean
    CadastrarCorWorkspaceUseCase cadastrarCorWorkspaceUseCase(CorWorkspaceRepository repository, CorWorkspaceControllerMapper mapper) {
        return new CadastrarCorWorkspaceUseCase(repository, mapper);
    }

    @Bean
    ListarTodasCoresWorkspaceUseCase listarTodasCoresWorkspaceUseCase(CorWorkspaceRepository repository, CorWorkspaceControllerMapper mapper) {
        return new ListarTodasCoresWorkspaceUseCase(repository, mapper);
    }

    @Bean
    BuscarCorWorkspacePorIdUserCase buscarCorWorkspacePorIdUserCase(CorWorkspaceRepository repository, CorWorkspaceControllerMapper mapper) {
        return new BuscarCorWorkspacePorIdUserCase(repository, mapper);
    }

    @Bean
    DesativarCorWorkspaceUseCase desativarCorWorkspaceUseCase(CorWorkspaceRepository repository) {
        return new DesativarCorWorkspaceUseCase(repository);
    }

    @Bean
    AtivarCorWorkspaceUseCase ativarCorWorkspaceUseCase(CorWorkspaceRepository repository) {
        return new AtivarCorWorkspaceUseCase(repository);
    }

    @Bean
    CorWorkspaceControllerMapper corWorkspaceControllerMapper() {
        return new CorWorkspaceControllerMapper();
    }

    @Bean
    CorWorkspaceRepository corWorkspaceRepository(SpringDataCorWorkspaceRepository repository, CorWorkspacePersistenceMapper mapper) {
        return new JpaCorWorkspaceRepository(repository, mapper);
    }

    @Bean
    CorWorkspacePersistenceMapper corWorkspacePersistenceMapper() {
        return new CorWorkspacePersistenceMapper();
    }
}
