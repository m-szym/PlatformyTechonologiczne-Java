package lab4;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class Lab4Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
        MageTowersRepository repo = new MageTowersRepository(entityManagerFactory.createEntityManager());

        Mage gandalf = new Mage("Gandalf the Grey", 100);
        Mage saruman = new Mage("Saruman the White", 150);
        Tower orthanc = new Tower("Orthanc", 100);
        orthanc.addMage(saruman);
        orthanc.addMage(gandalf);
        saruman.setTower(orthanc);
        gandalf.setTower(orthanc);


        repo.save(gandalf);
        repo.save(saruman);
        repo.save(orthanc);

        repo.printAllMages();
        repo.printAllTowers();
        repo.printAll();

        repo.findAllMagesWithLevelGreaterThan(100).forEach(mage -> System.out.println(mage.getName()));
        repo.findAllTowersWithHeightLowerThan(200).forEach(tower -> System.out.println(tower.getName()));
        repo.findAllMagesInTowerWithLevelGreaterThan("Orthanc", 100).forEach(mage -> System.out.println(mage.getName()));

        repo.close();
        entityManagerFactory.close();
    }
}
