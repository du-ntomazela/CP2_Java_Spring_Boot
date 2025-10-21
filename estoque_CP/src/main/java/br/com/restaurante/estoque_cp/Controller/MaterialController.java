package br.com.restaurante.estoque_cp.Controller;

import br.com.restaurante.estoque_cp.Service.MaterialService;
import br.com.restaurante.estoque_cp.model.entidades.Material;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/estoque/materiais")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Material>> listarMaterial(
            @RequestParam(name = "nome", required = false) String nome) {
        return ResponseEntity.ok(materialService.listarMateriais(nome));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Material> criar(@Valid @RequestBody Material dto) {
        Material salvo = materialService.salvar(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(salvo);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Material> listarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.buscarPorId(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Material> atualizar(
            @PathVariable long id,
            @Valid @RequestBody Material body) {
        Material atualizado = materialService.atualizar(id, body);
        return ResponseEntity.ok(atualizado);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable long id) {
        materialService.remover(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping(value = "/{id}/consumir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Material> consumir(@PathVariable long id, @Valid @RequestBody br.com.restaurante.estoque_cp.Controller.dto.ConsumirRequest req) {
        Material atualizado = materialService.consumir(id, req.getQuantidade());
        return ResponseEntity.ok(atualizado);
    }
}
