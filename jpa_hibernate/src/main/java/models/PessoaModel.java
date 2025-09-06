package model;

import entities.Pessoa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.logging.Logger;

public class PessoaModel {

    private static final String PU_NAME = "admin-jpa";
    private static final Logger log = Logger.getLogger(PessoaModel.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    @FunctionalInterface
    private interface EntityManagerConsumer {
        void consume(EntityManager em);
    }

    public void create(Pessoa pessoa){
        executeInTransaction(em -> em.persist(pessoa), "persist");
    }

    public void update(Pessoa pessoa) {
        executeInTransaction(em -> em.merge(pessoa), "update");
    }

    //delete
    public void delete(Pessoa p) {
        executeInTransaction(em -> em.remove(em.contains(p)? p:em.merge(p)), "delete");
    }

    //findById
    public Pessoa findById(Long id) {
        var em = getEntityManager();
        try {
            return em.find(Pessoa.class, id);
        } finally {
            em.close();
        }
    }

    public List<Pessoa> findAll() {
        var em = getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pessoa p", Pessoa.class).getResultList();
        } finally {
            em.close();
        }
    }

    private void executeInTransaction(EntityManagerConsumer lambdaOp, String nomeOperacao) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            log.info("iniciando operação: %s".formatted(nomeOperacao));
            transaction.begin();
            lambdaOp.consume(em);
            transaction.commit();
            log.info("Operação '%s' bem sucedida.".formatted(nomeOperacao));
        } catch (Exception e) {
            log.severe("Erro ao realizar operação '%s'. Causa: %s".formatted(nomeOperacao,e.getMessage()));
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            em.close();
        }
    }
}
