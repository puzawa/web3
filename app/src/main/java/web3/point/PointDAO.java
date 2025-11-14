package web3.point;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
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

    public boolean isDBAvailable() {
        EntityManagerFactory factory = getEMF();
        if (factory == null) {
            return false;
        }
        try (EntityManager em = factory.createEntityManager()) {
            return true;
        } catch (PersistenceException ex) {
            return false;
        }
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

    public List<Point> loadAll() {
        return executeRead(
                em -> em.createQuery("SELECT u FROM Point u", Point.class).getResultList(),
                new ArrayList<>(),
                "fetching all Points"
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