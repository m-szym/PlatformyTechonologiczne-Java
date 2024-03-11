package lab4;

import jakarta.persistence.EntityManager;

import java.util.List;

public class MageTowersRepository {
    private final EntityManager entityManager;

    public MageTowersRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Mage mage) {
        try {
//            if (entityManager.find(Mage.class, mage.getName() ) != null) {
//                entityManager.getTransaction().begin();
//                entityManager.remove(mage);
//                entityManager.getTransaction().commit();
//            }

            if (entityManager.find(Tower.class, mage.getTower().getName() ) == null) {
                entityManager.getTransaction().begin();
                entityManager.persist(mage.getTower());
                entityManager.getTransaction().commit();
            }
            entityManager.getTransaction().begin();
            entityManager.persist(mage);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void save(Tower tower) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(tower);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void close() {
        entityManager.close();
    }

    public List<Mage> findAllMages() {
        return entityManager.createQuery("from Mage", Mage.class).getResultList();
    }

    public List<Mage> findAllMagesWithLevelGreaterThan(int level) {
        return entityManager.createQuery("SELECT m from Mage m where m.level > :level", Mage.class)
                .setParameter("level", level)
                .getResultList();
    }
    public List<Tower> findAllTowers() {
        return entityManager.createQuery("from Tower", Tower.class).getResultList();
    }

    public List<Tower> findAllTowersWithHeightLowerThan(int height) {
        return entityManager.createQuery("SELECT t from Tower t where t.height < :height", Tower.class)
                .setParameter("height", height)
                .getResultList();
    }

    public List<Mage> findAllMagesInTowerWithLevelGreaterThan(String towerName, int level) {
        return entityManager.createQuery("SELECT m from Mage m " +
                                            "where m.tower.name = :towerName " +
                                            "and m.level > :level", Mage.class)
                .setParameter("towerName", towerName)
                .setParameter("level", level)
                .getResultList();
    }

    public List<Mage> queryMages(String query) {
        return entityManager.createQuery(query, Mage.class).getResultList();
    }

    public List<Tower> queryTowers(String query) {
        return entityManager.createQuery(query, Tower.class).getResultList();
    }

    public void printAllMages() {
        findAllMages().forEach(System.out::println);
    }

    public void printAllTowers() {
        findAllTowers().forEach(System.out::println);
    }

    public void printAll() {
        printAllMages();
        printAllTowers();
    }

    public void removeMage(Mage mage) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(mage);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void removeMage(String name) {
        try {
            entityManager.getTransaction().begin();
            Mage mage = entityManager.find(Mage.class, name);
            entityManager.remove(mage);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void removeTower(Tower tower) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(tower);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void removeTower(String name) {
        try {
            entityManager.getTransaction().begin();
            Tower tower = entityManager.find(Tower.class, name);
            entityManager.remove(tower);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
