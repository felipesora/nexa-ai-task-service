package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.tarefa.FiltroTarefas;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;
import com.nexa.task.infra.persistence.mapper.TarefaPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataTarefaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class JpaTarefaRepository implements TarefaRepository {

    private final SpringDataTarefaRepository repository;
    private final TarefaPersistenceMapper mapper;

    public JpaTarefaRepository(SpringDataTarefaRepository repository, TarefaPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Tarefa save(Tarefa tarefa) {
        TarefaEntity entity = mapper.toEntity(tarefa);
        repository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Page<Tarefa> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tarefa> findByIdWorkspace(Long idWorkspace, Pageable pageable) {
        return repository.findByWorkspace_Id(idWorkspace, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tarefa> findByIdWorkspaceAndAtivo(Long idWorkspace, FiltroTarefas filtro, Pageable pageable) {
        FiltroTarefas f = filtro != null ? filtro : FiltroTarefas.vazio();
        // O LIKE já recebe o padrão pronto, em minúsculas, para a busca ignorar maiúsculas/minúsculas
        String titulo = f.titulo() != null ? "%" + f.titulo().toLowerCase() + "%" : null;

        // "Atrasada" não é um status gravado: o filtro vira a condição prazo vencido e não concluída
        boolean filtrarAtrasadas = f.status() == StatusTarefa.ATRASADA;
        StatusTarefa status = filtrarAtrasadas ? null : f.status();
        LocalDateTime atrasadasAntesDe = filtrarAtrasadas ? LocalDateTime.now() : null;

        return repository.buscarAtivasPorWorkspaceComFiltros(idWorkspace, titulo, status, atrasadasAntesDe, f.prioridade(), f.idTag(), pageable)
                .map(mapper::toDomain);
    }

    @Override
    public long countAtrasadasByIdUsuario(Long idUsuario) {
        return repository.countByIdUsuarioAndAtivoTrueAndStatusNotAndDataLimiteBefore(
                idUsuario, StatusTarefa.CONCLUIDA, LocalDateTime.now());
    }

    @Override
    public long countAtrasadasByIdWorkspace(Long idWorkspace) {
        return repository.countByWorkspace_IdAndAtivoTrueAndStatusNotAndDataLimiteBefore(
                idWorkspace, StatusTarefa.CONCLUIDA, LocalDateTime.now());
    }

    @Override
    public List<Tarefa> findAllByWorkspace(Long idWorkspace) {
        return repository.findAllByWorkspace_Id(idWorkspace)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Page<Tarefa> findByIdUsuario(Long idUsuario, Pageable pageable) {
        return repository.findByIdUsuario(idUsuario, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tarefa> findByIdUsuarioAndAtivo(Long idUsuario, Pageable pageable) {
        return repository.findByIdUsuarioAndAtivoTrue(idUsuario, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tarefa> findByIdUsuarioAndTitulo(Long idUsuario, String titulo, Pageable pageable) {
        return repository.findByIdUsuarioAndTituloContainingIgnoreCase(idUsuario, titulo, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tarefa> findByIdUsuarioAndTituloAndAtivo(Long idUsuario, String titulo, Pageable pageable) {
        return repository.findByIdUsuarioAndTituloContainingIgnoreCaseAndAtivoTrue(idUsuario, titulo, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Tarefa> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Tarefa> findByIdAtivo(Long id) {
        return repository.findByIdAndAtivoTrue(id)
                .map(mapper::toDomain);
    }

    @Override
    public long countAtivasByIdWorkspace(Long idWorkspace) {
        return repository.countByWorkspace_IdAndAtivoTrue(idWorkspace);
    }

    @Override
    public long countAtivasByIdUsuarioAndStatus(Long idUsuario, StatusTarefa status) {
        return repository.countByIdUsuarioAndStatusAndAtivoTrue(idUsuario, status);
    }

    @Override
    public List<Tarefa> findPrioritariasByIdUsuario(Long idUsuario, int limite) {
        return repository.buscarPrioritariasPorIdUsuario(idUsuario, PageRequest.of(0, limite))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Tarefa> findAtivasByIdUsuarioAndDataLimiteEntre(Long idUsuario, LocalDateTime inicio, LocalDateTime fim) {
        return repository.findByIdUsuarioAndAtivoTrueAndDataLimiteBetweenOrderByDataLimiteAsc(idUsuario, inicio, fim)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long countAtivasByIdWorkspaceAndStatus(Long idWorkspace, StatusTarefa status) {
        return repository.countByWorkspace_IdAndStatusAndAtivoTrue(idWorkspace, status);
    }
}
