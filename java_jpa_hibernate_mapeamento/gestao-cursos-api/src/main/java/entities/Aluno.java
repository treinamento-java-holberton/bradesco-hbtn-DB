package entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Aluno {

    @Id
    private Long id;

    private String nomeCompleto;

    private String pessoa;

    private Date nascimento;

    private String email;

    @ManyToMany(mappedBy = "aluno")
    @JoinColumn(name = "curso_id")
    private Set<Curso> curso;

    @OneToMany(mappedBy = "aluno")
    private Set<Telefone> telefone;

    @OneToMany(mappedBy = "aluno")
    private Aluno aluno;


    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Set<Curso> getCurso() {
        return curso;
    }

    public void setCurso(Set<Curso> curso) {
        this.curso = curso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getPessoa() {
        return pessoa;
    }

    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }

    public Set<Telefone> getTelefone() {
        return telefone;
    }

    public void setTelefone(Set<Telefone> telefone) {
        this.telefone = telefone;
    }
}
