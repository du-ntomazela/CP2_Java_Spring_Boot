package br.com.restaurante.estoque_cp.Controller;

import br.com.restaurante.estoque_cp.Service.AlimentoService;
import br.com.restaurante.estoque_cp.model.entidades.Alimento;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/estoque/alimentos")
public class AlimentoController {

    private final AlimentoService alimentoService;

    public AlimentoController(AlimentoService alimentoService) {
        this.alimentoService = alimentoService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Alimento> criar(@Valid @RequestBody Alimento dto) {
        Alimento salvo = alimentoService.salvar(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alimento> buscarPorId(@PathVariable Long id) {
        Alimento found = alimentoService.buscarPorId(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Alimento>> listarAlimento(
            @RequestParam(name = "nome", required = false) String nome) {
        return ResponseEntity.ok(alimentoService.listarAlimentos(nome));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Alimento> atualizar(
            @PathVariable long id,
            @Valid @RequestBody Alimento body
    ) {
        Alimento atualizado = alimentoService.atualizar(id, body);
        return ResponseEntity.ok(atualizado);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable long id) {
        alimentoService.remover(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping(value = "/{id}/consumir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Alimento> consumir(@PathVariable long id, @Valid @RequestBody br.com.restaurante.estoque_cp.Controller.dto.ConsumirRequest req) {
        Alimento atualizado = alimentoService.consumir(id, req.getQuantidade());
        return ResponseEntity.ok(atualizado);
    }
    @DeleteMapping("/verificar_validade/{data}")
    public List<Alimento> verificar_validade(
            @PathVariable @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
            java.time.LocalDate data) {
        return alimentoService.verificar_validade(java.sql.Date.valueOf(data));
    }
}
