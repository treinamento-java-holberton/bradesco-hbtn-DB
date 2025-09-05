package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Endereco {

    @Id
    private Long id;

    private String logradouro;

    private String endereco;

    private String numero;

    private String bairro;

    private String cidade;

    private String estado;

    private Integer cep;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
}
