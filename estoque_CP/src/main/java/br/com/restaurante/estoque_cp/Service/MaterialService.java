package br.com.restaurante.estoque_cp.Service;

import br.com.restaurante.estoque_cp.Repositories.MaterialRepository;
import br.com.restaurante.estoque_cp.model.entidades.Material;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Transactional
    public Material salvar(Material material) {
        return repository.save(material); // volta com o ID gerado
    }

    @Transactional
    public Material atualizar(long id, Material body) {
        Material entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material id=" + id + " não encontrado"));

        entity.setName(body.getName());
        entity.setQuantidade(body.getQuantidade());

        return repository.save(entity);
    }

    public List<Material> listarMateriais(String nome) {
        if(nome != null && !nome.isEmpty()){
            List<Material> lista = new ArrayList<>();
            lista.add(repository.findByName(nome));
            return  lista;
        }
        return repository.findAll();
    }

    public Material buscarPorId (Long id) {
        return repository.findById(id).orElse(null);
    }

    public void remover(long id) {repository.deleteById(id);}

    @Transactional
    public Material consumir(long id, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade inválida");
        Material m = buscarPorId(id);
        if (m.getQuantidade() < quantidade) throw new IllegalArgumentException("Estoque insuficiente");
        m.setQuantidade(m.getQuantidade() - quantidade);
        return repository.save(m);
    }
}
