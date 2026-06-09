package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.usecase.iconeWorkspace.CadastrarIconeWorkspaceUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/icones-workspace")
@Tag(name = "", description = "")
public class IconeWorkspaceController {

    private final CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase;

    public IconeWorkspaceController(CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase) {
        this.cadastrarIconeWorkspaceUseCase = cadastrarIconeWorkspaceUseCase;
    }

    @PostMapping
    public ResponseEntity<IconeWorkspaceResponseDTO> cadastrarIcone(@RequestBody @Valid IconeWorkspaceRequestDTO request,
                                                                    UriComponentsBuilder uriBuilder) {
        IconeWorkspaceResponseDTO response = cadastrarIconeWorkspaceUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/icones-workspace/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }
}
