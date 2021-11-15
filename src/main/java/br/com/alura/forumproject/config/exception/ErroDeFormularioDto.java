package br.com.alura.forumproject.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErroDeFormularioDto {
    private String campo;
    private String erro;
}
