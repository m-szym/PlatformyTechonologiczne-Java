package lab5;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MageRepositoryTest {
    @Test
    public void testProperSave() {
        Mage mage = new Mage("Gandalf", 100);
        ArrayList<Mage> mages = new ArrayList<>();
        MageRepository mageRepo = new MageRepository(mages);

        mageRepo.save(mage);

        assertTrue(mages.contains(mage));
    }

    @Test
    public void testIdRepeatSave() {
        ArrayList<Mage> mages = new ArrayList<>();
        Mage mage = new Mage("Gandalf", 100);
        mages.add(new Mage("Gandalf", 150));
        MageRepository mageRepo = new MageRepository(new ArrayList<>(mages));

        assertThrows(IllegalArgumentException.class, () -> mageRepo.save(mage));
    }

    @Test
    public void testProperDelete() {
        ArrayList<Mage> mages = new ArrayList<>();
        Mage mage = new Mage("Gandalf", 100);
        mages.add(mage);
        MageRepository mageRepo = new MageRepository(mages);

        mageRepo.delete("Gandalf");

        assertFalse(mages.contains(mage));
    }

    @Test
    public void testNonExistentDelete() {
        MageRepository mageRepo = new MageRepository(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> mageRepo.delete("Gandalf"));
    }

    @Test
    public void testProperFind() {
        ArrayList<Mage> mages = new ArrayList<>();
        Mage mage = new Mage("Gandalf", 100);
        mages.add(mage);
        MageRepository mageRepo = new MageRepository(new ArrayList<>(mages));

        assertTrue(mageRepo.find("Gandalf").isPresent());
        assertEquals(mage, mageRepo.find("Gandalf").get());
    }

    @Test
    public void testNonExistentFind() {
        MageRepository mageRepo = new MageRepository(new ArrayList<>());

        assertFalse(mageRepo.find("Gandalf").isPresent());
    }
}
