package br.com.restaurante.estoque_cp.Service;


import br.com.restaurante.estoque_cp.Repositories.AlimentoRepository;
import br.com.restaurante.estoque_cp.model.entidades.Alimento;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlimentoService {

    @Autowired
    private AlimentoRepository repository;

    @Transactional
    public Alimento salvar(Alimento a) {
        return repository.save(a); // volta com o ID gerado
    }

    public List<Alimento> listarAlimentos(String nome) {
        if(nome != null && !nome.isEmpty()){
            List<Alimento> lista = new ArrayList<>();
            lista.add(repository.findByName(nome));
            return lista;
        }
        return repository.findAll();
    }
    @Transactional
    public Alimento atualizar(long id, Alimento body) {
        Alimento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alimento id=" + id + " não encontrado"));

        // Atualização total (PUT): copiar todos os campos editáveis
        entity.setName(body.getName());
        entity.setFornecedor(body.getFornecedor());
        entity.setData_de_validade(body.getData_de_validade());
        entity.setQuantidade(body.getQuantidade());

        return repository.save(entity);
    }

    public Alimento buscarPorId (Long id) {
        return repository.findById(id).orElse(null);
    }

    public void remover(long id) {repository.deleteById(id);}

    @Transactional
    public Alimento consumir(long id, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade inválida");
        Alimento a = buscarPorId(id);
        if (a.getQuantidade() < quantidade) throw new IllegalArgumentException("Estoque insuficiente");
        a.setQuantidade(a.getQuantidade() - quantidade);
        return repository.save(a);
    }
    public List<Alimento> verificar_validade(Date data_de_validade) {
        List<Alimento> alimentos = listarAlimentos("");
        List<Alimento> a = new ArrayList<>();
        for (Alimento alimento : alimentos) {
            if(alimento.getData_de_validade().before(data_de_validade)) {
                long id = alimento.getId();
                a.add(alimento);
                remover(id);
            }
        } return a;

    }
}
