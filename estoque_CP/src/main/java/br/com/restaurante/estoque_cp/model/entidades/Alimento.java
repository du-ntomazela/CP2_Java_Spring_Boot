package br.com.restaurante.estoque_cp.model.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name="Alimento")
public class Alimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALIMENTO")
    private long id;

    @NotBlank(message = "O campo nome é obrigatório.")
    @Size(max = 100, message = "O tamanho máximo do campo nome deve ser 100 caracteres.")
    @Column(name ="NOME_ALIMENTO", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "O campo fornecedor é obrigatório.")
    @Column(name = "FORNECEDOR", nullable = false, length = 100)
    private String fornecedor;

    @NotNull(message = "O campo de data de validade é obrigatório.")
    @Column(name = "DATA_DE_VALIDADE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data_de_validade;

    @NotNull(message = "O campo quantidade é obrigatório.")
    @Column(name = "QUANTIDADE", nullable = false)
    private int quantidade;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Date getData_de_validade() {
        return data_de_validade;
    }

    public void setData_de_validade(Date data_de_validade) {
        this.data_de_validade = data_de_validade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
