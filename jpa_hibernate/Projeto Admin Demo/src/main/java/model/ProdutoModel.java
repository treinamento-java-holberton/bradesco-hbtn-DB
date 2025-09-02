package model;

import entities.Produto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.logging.Logger;

public class ProdutoModel {

    private static final String PU_NAME = "admin-jpa";
    private static final Logger log = Logger.getLogger(ProdutoModel.class.getName());
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @FunctionalInterface
    private interface EntityManagerConsumer {
        void accept(EntityManager em);
    }

    public void create(Produto p) {
        executeInTransaction(em -> em.persist(p), "persistir");
    }

    public void update(Produto p) {
        executeInTransaction(em -> em.merge(p), "atualizar");
    }

    public void delete(Produto p) {
        executeInTransaction(em -> em.remove(em.contains(p) ? p : em.merge(p)), "excluir");
    }

    public Produto findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public List<Produto> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();
        } finally {
            em.close();
        }
    }

    private void executeInTransaction(EntityManagerConsumer action, String operacao) {
        EntityManager em = getEntityManager();
        try {
            log.info("Iniciando operação: " + operacao);
            em.getTransaction().begin();
            action.accept(em);
            em.getTransaction().commit();
            log.info("Operação '" + operacao + "' concluída com sucesso.");
        } catch (Exception ex) {
            log.severe("Erro ao " + operacao + ": " + ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }
}