package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.application.usecase.subtarefa.*;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.persistence.adapter.JpaSubtarefaRepository;
import com.nexa.task.infra.persistence.mapper.SubtarefaPersistenceMapper;
import com.nexa.task.infra.persistence.mapper.TarefaPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataSubtarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubtarefaBeanConfig {

    @Bean
    CadastrarSubtarefaUseCase cadastrarSubtarefaUseCase(SubtarefaRepository subtarefaRepository, TarefaRepository tarefaRepository,
                                                        SubtarefaControllerMapper mapper, AuthenticationService authService) {
        return new CadastrarSubtarefaUseCase(subtarefaRepository, tarefaRepository, mapper, authService);
    }

    @Bean
    ListarTodasSubtarefasUseCase listarTodasSubtarefasUseCase(SubtarefaRepository subtarefaRepository, SubtarefaControllerMapper mapper) {
        return new ListarTodasSubtarefasUseCase(subtarefaRepository, mapper);
    }

    @Bean
    ListarSubtarefasPorIdTarefaUseCase listarSubtarefasPorIdTarefaUseCase(SubtarefaRepository subtarefaRepository,
                                                                          TarefaRepository tarefaRepository,
                                                                          SubtarefaControllerMapper mapper,
                                                                          AuthenticationService authService) {
        return new ListarSubtarefasPorIdTarefaUseCase(subtarefaRepository, tarefaRepository, mapper, authService);
    }

    @Bean
    BuscarSubtarefaPorIdUseCase buscarSubtarefaPorIdUseCase(SubtarefaRepository subtarefaRepository, SubtarefaControllerMapper mapper,
                                                            AuthenticationService authService) {
        return new BuscarSubtarefaPorIdUseCase(subtarefaRepository, mapper, authService);
    }

    @Bean
    AtualizarSubtarefaUseCase atualizarSubtarefaUseCase(SubtarefaRepository subtarefaRepository, AuthenticationService authService) {
        return new AtualizarSubtarefaUseCase(subtarefaRepository, authService);
    }

    @Bean
    DesativarSubtarefaUseCase desativarSubtarefaUseCase(SubtarefaRepository subtarefaRepository, AuthenticationService authService) {
        return new DesativarSubtarefaUseCase(subtarefaRepository, authService);
    }

    @Bean
    AtivarSubtarefaUseCase ativarSubtarefaUseCase(SubtarefaRepository subtarefaRepository) {
        return new AtivarSubtarefaUseCase(subtarefaRepository);
    }

    @Bean
    ConcluirSubtarefaUseCase concluirSubtarefaUseCase(SubtarefaRepository subtarefaRepository, AuthenticationService authService) {
        return new ConcluirSubtarefaUseCase(subtarefaRepository, authService);
    }

    @Bean
    DesmarcarSubtarefaConcluidaUseCase desmarcarSubtarefaConcluidaUseCase(SubtarefaRepository subtarefaRepository, AuthenticationService authService) {
        return new DesmarcarSubtarefaConcluidaUseCase(subtarefaRepository, authService);
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
