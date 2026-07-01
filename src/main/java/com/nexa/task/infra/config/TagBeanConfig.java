package com.nexa.task.infra.config;

import com.nexa.task.application.mapper.CorTagControllerMapper;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.application.usecase.tag.AtivarTagUseCase;
import com.nexa.task.application.usecase.tag.AtualizarTagUseCase;
import com.nexa.task.application.usecase.tag.BuscarTagPorIdUseCase;
import com.nexa.task.application.usecase.tag.CadastrarTagUseCase;
import com.nexa.task.application.usecase.tag.DesativarTagUseCase;
import com.nexa.task.application.usecase.tag.ListarTagsPorIdTarefaUseCase;
import com.nexa.task.application.usecase.tag.ListarTagsPorIdUsuarioUseCase;
import com.nexa.task.application.usecase.tag.ListarTodasTagsUseCase;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.persistence.adapter.JpaTagRepository;
import com.nexa.task.infra.persistence.mapper.CorTagPersistenceMapper;
import com.nexa.task.infra.persistence.mapper.TagPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataTagRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TagBeanConfig {

    @Bean
    CadastrarTagUseCase cadastrarTagUseCase(TagRepository tagRepository,
                                            CorTagRepository corTagRepository,
                                            TagControllerMapper mapper,
                                            AuthenticationService authService) {
        return new CadastrarTagUseCase(tagRepository, corTagRepository, mapper, authService);
    }

    @Bean
    ListarTodasTagsUseCase listarTodasTagsUseCase(TagRepository repository, TagControllerMapper mapper) {
        return new ListarTodasTagsUseCase(repository, mapper);
    }

    @Bean
    BuscarTagPorIdUseCase buscarTagPorIdUseCase(TagRepository repository, TagControllerMapper mapper,
                                                AuthenticationService authService) {
        return new BuscarTagPorIdUseCase(repository, mapper, authService);
    }

    @Bean
    ListarTagsPorIdUsuarioUseCase listarTagsPorIdUsuarioUseCase(TagRepository repository, TagControllerMapper mapper,
                                                                AuthenticationService authService) {
        return new ListarTagsPorIdUsuarioUseCase(repository, mapper, authService);
    }

    @Bean
    ListarTagsPorIdTarefaUseCase listarTagsPorIdTarefaUseCase(TagRepository repository, TarefaRepository tarefaRepository, TagControllerMapper mapper,
                                                              AuthenticationService authService) {
        return new ListarTagsPorIdTarefaUseCase(repository, tarefaRepository, mapper, authService);
    }

    @Bean
    AtualizarTagUseCase atualizarTagUseCase(TagRepository tagRepository, CorTagRepository corTagRepository,
                                            AuthenticationService authService) {
        return new AtualizarTagUseCase(tagRepository, corTagRepository, authService);
    }

    @Bean
    DesativarTagUseCase desativarTagUseCase(TagRepository tagRepository, AuthenticationService authService) {
        return new DesativarTagUseCase(tagRepository, authService);
    }

    @Bean
    AtivarTagUseCase ativarTagUseCase(TagRepository tagRepository) {
        return new AtivarTagUseCase(tagRepository);
    }

    @Bean
    TagControllerMapper tagControllerMapper(CorTagControllerMapper corTagControllerMapper) {
        return new TagControllerMapper(corTagControllerMapper);
    }

    @Bean
    JpaTagRepository jpaTagRepository(SpringDataTagRepository repository, TagPersistenceMapper mapper) {
        return new JpaTagRepository(repository, mapper);
    }

    @Bean
    TagPersistenceMapper tagPersistenceMapper(CorTagPersistenceMapper corTagPersistenceMapper) {
        return new TagPersistenceMapper(corTagPersistenceMapper);
    }
}
