package models;

import entities.Curso;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CursoModel {

    private static final Logger log = Logger.getLogger(CursoModel.class.getName());

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
    public void create(Curso curso) {
        executeAction(em -> em.persist(curso), "create");
    }

    //find by id
    public Optional<Curso> findById(Long id) {
        Optional<Curso> output = Optional.empty();
        EntityManager em = getEntityManager();
        try {
            output = Optional.of(em.find(Curso.class, id));
        } catch (Exception e) {
            logException("findById", e);
        } finally {
            em.close();
        }
        return output;
    }

    //find all
    public Set<Curso> findAll() {
        EntityManager em = getEntityManager();
        Set<Curso> output = Set.of();
        try {
            output = em.createQuery("SELECT a FROM Curso a", Curso.class).getResultStream()
                    .collect(Collectors.toSet());
        }catch (Exception e) {
            logException("findAll", e);
        } finally {
            em.close();
        }
        return new HashSet<>(output);
    }

    //update
    public void update(Curso curso){
        executeAction(em -> em.merge(curso), "update");
    }

    //delete
    public void delete(Curso curso) {
        EntityManager em = getEntityManager();
        em.remove(em.contains(curso)? curso: em.merge(curso));
    }
}
