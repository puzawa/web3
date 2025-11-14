package web3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PointDAO {
    private static volatile EntityManagerFactory emf;

    private static EntityManagerFactory getEMF() {
        if (emf == null) {
            synchronized (PointDAO.class) {
                if (emf == null) {
                    try {
                        emf = Persistence.createEntityManagerFactory("pointsPres");
                    } catch (PersistenceException ex) {
                        System.err.println("DB unavailable: " + ex.getMessage());
                        emf = null;
                    }
                }
            }
        }
        return emf;
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = getEMF();
        if (factory == null) {
            return null;
        }
        try {
            return factory.createEntityManager();
        } catch (PersistenceException ex) {
            System.err.println("Failed to create EntityManager: " + ex.getMessage());
            return null;
        }
    }

    private void executeInTransaction(Consumer<EntityManager> operation, String errorContext) {
        EntityManager em = getEntityManager();
        if (em == null) {
            System.err.println("Cannot " + errorContext + ", DB unavailable.");
            return;
        }
        try {
            em.getTransaction().begin();
            operation.accept(em);
            em.getTransaction().commit();
        } catch (PersistenceException ex) {
            System.err.println("Error " + errorContext + ": " + ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    private <T> T executeRead(Function<EntityManager, T> operation, T defaultValue, String errorContext) {
        EntityManager em = getEntityManager();
        if (em == null) {
            System.err.println("Cannot " + errorContext + ", DB unavailable.");
            return defaultValue;
        }
        try {
            return operation.apply(em);
        } catch (PersistenceException ex) {
            System.err.println("Error " + errorContext + ": " + ex.getMessage());
            return defaultValue;
        } finally {
            em.close();
        }
    }

    public void save(Point point) {
        executeInTransaction(em -> em.persist(point), "saving Point");
    }

    public List<Point> findAll() {
        return executeRead(
                em -> em.createQuery("SELECT u FROM Point u", Point.class).getResultList(),
                new ArrayList<>(),
                "fetching all Points"
        );
    }

    public Point findById(Long id) {
        return executeRead(
                em -> em.find(Point.class, id),
                null,
                "fetching Point by id"
        );
    }

    public void delete(Point point) {
        executeInTransaction(
                em -> em.remove(em.contains(point) ? point : em.merge(point)),
                "deleting Point"
        );
    }

    public void deleteAll() {
        executeInTransaction(
                em -> em.createQuery("DELETE FROM Point").executeUpdate(),
                "deleting all Points"
        );
    }

    public static void resetEMF() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }
}