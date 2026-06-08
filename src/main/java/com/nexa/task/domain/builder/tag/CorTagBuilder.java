package com.nexa.task.domain.builder.tag;

import com.nexa.task.domain.entity.tag.CorTag;

public class CorTagBuilder {

    private Long id = 1L;
    private String cor = "#FFFFFF";
    private Boolean ativo = true;

    public CorTagBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public CorTagBuilder comCor(String cor) {
        this.cor = cor;
        return this;
    }

    public CorTagBuilder comAtivo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public CorTag build() {
        return new CorTag(
                id,
                cor,
                ativo
        );
    }
}
