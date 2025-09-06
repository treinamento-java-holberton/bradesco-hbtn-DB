package models;

import entities.Aluno;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AlunoModel {

    private static final Logger log = Logger.getLogger(AlunoModel.class.getName());

    private static final String NOME_PU = "gestao-cursos-jpa";

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory(NOME_PU);


    //get entity manager
    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    //interface funcional execute query
    @FunctionalInterface
    private interface QueryConsumer {
        void consume(EntityManager em);
    }

    private static void logException(String action, Exception e) {
        log.severe("Erro ao executar ação '%s': %s".formatted(action, e.getMessage()));
    }

    //metodo execute query
    private void executeAction(QueryConsumer consumer, String action) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            consumer.consume(em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            logException(action, e);
        } finally {
            em.close();
        }
    }

    //create
    public void create(Aluno aluno) {
        executeAction(em -> em.persist(aluno), "create");
    }

    //find by id
    public Optional<Aluno> findById(Long id) {
        Optional<Aluno> output = Optional.empty();
        EntityManager em = getEntityManager();
        try {
            output = Optional.of(em.find(Aluno.class, id));
        } catch (Exception e) {
            logException("findById", e);
        } finally {
            em.close();
        }
        return output;
    }

    //find all
    public Set<Aluno> findAll() {
        EntityManager em = getEntityManager();
        Set<Aluno> output = Set.of();
        try {
            output = em.createQuery("SELECT a FROM Aluno a", Aluno.class).getResultStream()
                    .collect(Collectors.toSet());
        }catch (Exception e) {
            logException("findAll", e);
        } finally {
            em.close();
        }
        return new HashSet<>(output);
    }

    //update
    public void update(Aluno aluno){
        executeAction(em -> em.merge(aluno), "update");
    }

    //delete
    public void delete(Aluno aluno) {
        EntityManager em = getEntityManager();
        em.remove(em.contains(aluno)? aluno: em.merge(aluno));
    }
}
