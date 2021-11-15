package br.com.alura.forumproject.controller;

import br.com.alura.forumproject.DTO.DetalhesTopicoDto;
import br.com.alura.forumproject.DTO.TopicoDto;
import br.com.alura.forumproject.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forumproject.controller.form.TopicoForm;
import br.com.alura.forumproject.model.Topico;
import br.com.alura.forumproject.repository.CursoRepository;
import br.com.alura.forumproject.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    /**
     * Como não marcamos os parâmetros com @RequestBody, serão inseridos diretamente na URL.
     * Marcamos então com a notação @RequestParam sendo que o parâmetro se torna obrigatório.
     */
    @GetMapping
    @Cacheable(value = "listarTopicos")
    public Page<TopicoDto> listar(@RequestParam(required = false) String nomeCurso,
                                  @PageableDefault(sort = "id", direction = Sort.Direction.ASC,
                                          page = 0, size = 10) Pageable pageable) {

        if (nomeCurso == null) {
            Page<Topico> topico = topicoRepository.findAll(pageable);
            return TopicoDto.converter(topico);
        } else {
            Page<Topico> topico = topicoRepository.findByCursoNome(nomeCurso, pageable);
            return TopicoDto.converter(topico);
        }
    }

    @PostMapping
    @Transactional //Faz o commit no banco de dados
    @CacheEvict(value = "listarTopicos", allEntries = true) // Faz com o cache seja limpo ao cadastrar ou atualizar dados
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
        /* Converte o Topico para TopicoForm */
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);
        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional //Faz o commit no banco de dados
    @CacheEvict(value = "listarTopicos", allEntries = true) // Faz com o cache seja limpo ao cadastrar ou atualizar dados
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional //Faz o commit no banco de dados
    @CacheEvict(value = "listarTopicos", allEntries = true) // Faz com o cache seja limpo ao cadastrar ou atualizar dados
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}