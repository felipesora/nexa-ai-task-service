package com.nexa.task.infra.config;

import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final IconeWorkspaceRepository iconeWorkspaceRepository;
    private final CorWorkspaceRepository corWorkspaceRepository;
    private final CorTagRepository corTagRepository;

    public DataInitializer(IconeWorkspaceRepository iconeWorkspaceRepository, CorWorkspaceRepository corWorkspaceRepository, CorTagRepository corTagRepository) {
        this.iconeWorkspaceRepository = iconeWorkspaceRepository;
        this.corWorkspaceRepository = corWorkspaceRepository;
        this.corTagRepository = corTagRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // icones workspace
        cadastrarIconeWorkspace("Padrão", "Folder");
        cadastrarIconeWorkspace("Trabalho", "Briefcase");
        cadastrarIconeWorkspace("Estudos", "GraduationCap");
        cadastrarIconeWorkspace("Projetos", "SquareChartGantt");
        cadastrarIconeWorkspace("Tecnologia", "Laptop");
        cadastrarIconeWorkspace("Aprendizado", "Brain");
        cadastrarIconeWorkspace("Leitura", "Book");
        cadastrarIconeWorkspace("Academia", "Dumbbell");
        cadastrarIconeWorkspace("Objetivos", "Target");
        cadastrarIconeWorkspace("Pessoal", "SquareUserRound");
        cadastrarIconeWorkspace("Startup", "Rocket");

        // cores workspace
        cadastrarCorWorkspace("#D9E2FF");
        cadastrarCorWorkspace("#EC4899");
        cadastrarCorWorkspace("#8B5CF6");
        cadastrarCorWorkspace("#12B5FD");
        cadastrarCorWorkspace("#BEF4FF");
        cadastrarCorWorkspace("#22C55E");
        cadastrarCorWorkspace("#EAB308");
        cadastrarCorWorkspace("#F97316");
        cadastrarCorWorkspace("#EF4444");

        // cores tags
        cadastrarCorTag("#D9E2FF");
        cadastrarCorTag("#EC4899");
        cadastrarCorTag("#8B5CF6");
        cadastrarCorTag("#12B5FD");
        cadastrarCorTag("#BEF4FF");
        cadastrarCorTag("#22C55E");
        cadastrarCorTag("#EAB308");
        cadastrarCorTag("#F97316");
        cadastrarCorTag("#EF4444");
    }
    
    private void cadastrarIconeWorkspace(String nome, String caminho) {
        if (iconeWorkspaceRepository.findByCaminho(caminho).isPresent()) return;

        IconeWorkspace iconeWorkspace = new IconeWorkspace(null, nome, caminho, true);
        iconeWorkspaceRepository.save(iconeWorkspace);
    }

    private void cadastrarCorWorkspace(String cor) {
        if (corWorkspaceRepository.findByCor(cor).isPresent()) return;

        CorWorkspace corWorkspace = new CorWorkspace(null, cor, true);
        corWorkspaceRepository.save(corWorkspace);
    }

    private void cadastrarCorTag(String cor) {
        if (corTagRepository.findByCor(cor).isPresent()) return;

        CorTag corTag = new CorTag(null, cor, true);
        corTagRepository.save(corTag);
    }
}
