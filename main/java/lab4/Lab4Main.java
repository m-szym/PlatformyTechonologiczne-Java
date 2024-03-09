package lab4;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class Lab4Main {
    public static void main(String[] args) {

        //Student student = new Student("Ramesh", "Fadatare", "rameshfadatare@javaguides.com");
        //Student student1 = new Student("John", "Cena", "john@javaguides.com");
        Mage gandalf = new Mage("Gandalf the Grey", 100);
        Mage saruman = new Mage("Saruman the White", 150);
        Tower orthanc = new Tower("Orthanc", 100);
        orthanc.addMage(saruman);
        orthanc.addMage(gandalf);
        saruman.setTower(orthanc);
        gandalf.setTower(orthanc);

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the student objects
            //session.save(student);
            //session.save(student1);
            System.out.println("\n\n\n\tSaving tower and mages");
            session.save(orthanc);
            session.save(saruman);
            session.save(gandalf);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            List <Student> students = session.createQuery("from Student", Student.class).list();
//            students.forEach(System.out::println);
            System.out.println("\n\n\n\tGetting towers and mages");
            List <Tower> towers = session.createQuery("from Tower", Tower.class).list();
            towers.forEach(System.out::println);
            List<Mage> mages = session.createQuery("from Mage", Mage.class).list();
            mages.forEach(System.out::println);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
