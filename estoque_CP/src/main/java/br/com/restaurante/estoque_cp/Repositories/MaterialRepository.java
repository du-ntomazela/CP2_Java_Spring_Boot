package br.com.restaurante.estoque_cp.Repositories;

import br.com.restaurante.estoque_cp.model.entidades.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MaterialRepository extends JpaRepository<Material,Long> {
    public Material findByName(String nome);
}
