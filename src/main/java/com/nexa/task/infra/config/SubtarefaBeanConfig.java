package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.application.usecase.subtarefa.*;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.persistence.adapter.JpaSubtarefaRepository;
import com.nexa.task.infra.persistence.mapper.SubtarefaPersistenceMapper;
import com.nexa.task.infra.persistence.mapper.TarefaPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataSubtarefaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubtarefaBeanConfig {

    @Bean
    CadastrarSubtarefaUseCase cadastrarSubtarefaUseCase(SubtarefaRepository subtarefaRepository, TarefaRepository tarefaRepository,
                                                        SubtarefaControllerMapper mapper) {
        return new CadastrarSubtarefaUseCase(subtarefaRepository, tarefaRepository, mapper);
    }

    @Bean
    ListarTodasSubtarefasUseCase listarTodasSubtarefasUseCase(SubtarefaRepository subtarefaRepository, SubtarefaControllerMapper mapper) {
        return new ListarTodasSubtarefasUseCase(subtarefaRepository, mapper);
    }

    @Bean
    ListarSubtarefasPorIdTarefaUseCase listarSubtarefasPorIdTarefaUseCase(SubtarefaRepository subtarefaRepository, SubtarefaControllerMapper mapper) {
        return new ListarSubtarefasPorIdTarefaUseCase(subtarefaRepository, mapper);
    }

    @Bean
    BuscarSubtarefaPorIdUseCase buscarSubtarefaPorIdUseCase(SubtarefaRepository subtarefaRepository, SubtarefaControllerMapper mapper) {
        return new BuscarSubtarefaPorIdUseCase(subtarefaRepository, mapper);
    }

    @Bean
    AtualizarSubtarefaUseCase atualizarSubtarefaUseCase(SubtarefaRepository subtarefaRepository) {
        return new AtualizarSubtarefaUseCase(subtarefaRepository);
    }

    @Bean
    DesativarSubtarefaUseCase desativarSubtarefaUseCase(SubtarefaRepository subtarefaRepository) {
        return new DesativarSubtarefaUseCase(subtarefaRepository);
    }

    @Bean
    AtivarSubtarefaUseCase ativarSubtarefaUseCase(SubtarefaRepository subtarefaRepository) {
        return new AtivarSubtarefaUseCase(subtarefaRepository);
    }

    @Bean
    SubtarefaControllerMapper subtarefaControllerMapper() {
        return new SubtarefaControllerMapper();
    }

    @Bean
    JpaSubtarefaRepository jpaSubtarefaRepository(SpringDataSubtarefaRepository repository, SubtarefaPersistenceMapper mapper) {
        return new JpaSubtarefaRepository(repository, mapper);
    }

    @Bean
    SubtarefaPersistenceMapper subtarefaPersistenceMapper(TarefaPersistenceMapper tarefaMapper) {
        return new SubtarefaPersistenceMapper(tarefaMapper);
    }
}
