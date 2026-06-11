package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.CorTagControllerMapper;
import com.nexa.task.application.usecase.corTag.*;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.infra.persistence.adapter.JpaCorTagRepository;
import com.nexa.task.infra.persistence.mapper.CorTagPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataCorTagRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorTagBeanConfig {

    @Bean
    CadastrarCorTagUseCase cadastrarCorTagUseCase(CorTagRepository repository, CorTagControllerMapper mapper) {
        return new CadastrarCorTagUseCase(repository, mapper);
    }

    @Bean
    ListarTodasCoresTagUseCase listarTodasCoresTagUseCase(CorTagRepository repository, CorTagControllerMapper mapper) {
        return new ListarTodasCoresTagUseCase(repository, mapper);
    }

    @Bean
    BuscarCorTagPorIdUseCase buscarCorTagPorIdUserCase(CorTagRepository repository, CorTagControllerMapper mapper) {
        return new BuscarCorTagPorIdUseCase(repository, mapper);
    }

    @Bean
    DesativarCorTagUseCase desativarCorTagUseCase(CorTagRepository repository) {
        return new DesativarCorTagUseCase(repository);
    }

    @Bean
    AtivarCorTagUseCase ativarCorTagUseCase(CorTagRepository repository) {
        return new AtivarCorTagUseCase(repository);
    }

    @Bean
    CorTagControllerMapper corTagControllerMapper() {
        return new CorTagControllerMapper();
    }

    @Bean
    JpaCorTagRepository jpaCorTagRepository(SpringDataCorTagRepository repository, CorTagPersistenceMapper mapper) {
        return new JpaCorTagRepository(repository, mapper);
    }

    @Bean
    CorTagPersistenceMapper corTagPersistenceMapper() {
        return new CorTagPersistenceMapper();
    }
}
