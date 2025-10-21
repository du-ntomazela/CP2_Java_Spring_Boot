package br.com.restaurante.estoque_cp.Repositories;

import br.com.restaurante.estoque_cp.model.entidades.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Long> {

    public Alimento findByName(String nome);
}
