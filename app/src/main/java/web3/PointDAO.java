package web3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class PointDAO {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pointsPres");

    public void save(Point point) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(point);
        em.getTransaction().commit();
        em.close();
    }

    public List<Point> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Point> points = em.createQuery("SELECT u FROM Point u", Point.class).getResultList();
        em.close();
        return points;
    }

    public Point findById(Long id) {
        EntityManager em = emf.createEntityManager();
        Point user = em.find(Point.class, id);
        em.close();
        return user;
    }

    public void delete(Point user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.contains(user) ? user : em.merge(user));
        em.getTransaction().commit();
        em.close();
    }

    public void deleteAll() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("DELETE FROM Point").executeUpdate();

        em.getTransaction().commit();
        em.close();
    }
}
