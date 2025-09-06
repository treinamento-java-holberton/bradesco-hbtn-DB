package demo;

import entities.Pessoa;
import entities.Produto;
import model.PessoaModel;
import model.ProdutoModel;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class AdministrativoApp {

    public static void main(String[] args) {
        ProdutoModel produtoModel = new ProdutoModel();

        Produto p1 = new Produto();
        p1.setNome("TV");
        p1.setPreco(300.0);
        p1.setQuantidade(100);
        p1.setStatus(true);

        // 1) Criando um produto
        produtoModel.create(p1);

        //2) Buscando todos os produtos na base de dados
        List<Produto> produtos = produtoModel.findAll();
        System.out.println("Qtde de produtos encontrados : " + produtos.size());

        //3) update
        p1.setQuantidade(40);
        produtoModel.update(p1);

        //5) findById;
        Produto produto = produtoModel.findById(p1.getId());

        //4) delete
        produtoModel.delete(p1);

        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Fulano");
        pessoa.setCpf("123456");
        pessoa.setEmail("email@email.com");
        pessoa.setDataNascimento(LocalDate.of(1980, Month.DECEMBER, 13));

        PessoaModel pessoaModel = new PessoaModel();

        pessoaModel.create(pessoa);

        List<Pessoa> pessoas = pessoaModel.findAll();

        Pessoa p = pessoas.get(0);

        p.setEmail("email@novoemail.com");
        pessoaModel.update(p);

        Pessoa pessoa1 = pessoaModel.findById(p.getId());
        pessoaModel.delete(pessoa1);

    }
}