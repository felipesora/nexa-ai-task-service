package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.IconeWorkspaceControllerMapper;
import com.nexa.task.application.usecase.iconeWorkspace.*;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.infra.persistence.adapter.JpaIconeWorkspaceRepository;
import com.nexa.task.infra.persistence.mapper.IconeWorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataIconeWorkspaceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IconeWorkspaceBeanConfig {

    @Bean
    CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase(IconeWorkspaceRepository repository, IconeWorkspaceControllerMapper mapper) {
        return new CadastrarIconeWorkspaceUseCase(repository, mapper);
    }

    @Bean
    ListarTodosIconesWorkspaceUseCase listarTodosIconesWorkspaceUseCase(IconeWorkspaceRepository repository, IconeWorkspaceControllerMapper mapper) {
        return new ListarTodosIconesWorkspaceUseCase(repository, mapper);
    }

    @Bean
    BuscarIconeWorkspacePorIdUserCase buscarIconeWorkspacePorIdUserCase(IconeWorkspaceRepository repository, IconeWorkspaceControllerMapper mapper) {
        return new BuscarIconeWorkspacePorIdUserCase(repository, mapper);
    }

    @Bean
    DesativarIconeWorkspaceUseCase DesativarIconeWorkspaceUseCase(IconeWorkspaceRepository repository) {
        return new DesativarIconeWorkspaceUseCase(repository);
    }

    @Bean
    AtivarIconeWorkspaceUseCase ativarIconeWorkspaceUseCase(IconeWorkspaceRepository repository) {
        return new AtivarIconeWorkspaceUseCase(repository);
    }

    @Bean
    IconeWorkspaceControllerMapper iconeWorkspaceControllerMapper() {
        return new IconeWorkspaceControllerMapper();
    }

    @Bean
    JpaIconeWorkspaceRepository jpaIconeWorkspaceRepository(SpringDataIconeWorkspaceRepository iconeWorkspaceRepository, IconeWorkspacePersistenceMapper mapper) {
        return new JpaIconeWorkspaceRepository(iconeWorkspaceRepository, mapper);
    }

    @Bean
    IconeWorkspacePersistenceMapper iconeWorkspacePersistenceMapper() {
        return new IconeWorkspacePersistenceMapper();
    }
}
