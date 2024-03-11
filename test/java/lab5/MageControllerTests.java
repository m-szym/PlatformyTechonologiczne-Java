package lab5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MageControllerTests {
    @Mock
    MageRepository mageRepo;
    @Test
    public void testProperSave() {
        //Mockito.doNothing().when(mageRepo).save(new Mage("Gandalf", 100));
        //doNothing().when(mageRepo).save(new Mage("Gandalf", 100));
        MageController mageController = new MageController(mageRepo);

        assertEquals(MageController.SAVE_SUCCESS, mageController.save("Gandalf", 100));
    }

    @Test
    public void testIdRepeatSave() {
        Mockito.doThrow(new IllegalArgumentException(MageRepository.MAGE_ALREADY_EXISTS))
                .when(mageRepo).save((Mage) Mockito.any());
        MageController mageController = new MageController(mageRepo);

        assertEquals(MageController.SAVE_ID_REPEAT, mageController.save("Gandalf", 100));
    }

    @Test
    public void testProperDelete() {
        doNothing().when(mageRepo).delete("Gandalf");
        MageController mageController = new MageController(mageRepo);

        assertEquals(MageController.DELETE_SUCCESS, mageController.delete("Gandalf"));
    }

    @Test
    public void testNonExistentDelete() {
        Mockito.doThrow(new IllegalArgumentException(MageRepository.MAGE_NOT_FOUND)).when(mageRepo).delete("Gandalf");
        MageController mageController = new MageController(mageRepo);

        assertEquals(MageController.TO_DELETE_NOT_FOUND, mageController.delete("Gandalf"));
    }

    @Test
    public void testProperFind() {
        Mage mage = new Mage("Gandalf", 100);
        doReturn(Optional.of(mage)).when(mageRepo).find("Gandalf");
        MageController mageController = new MageController(mageRepo);

        assertEquals(mage.toString(), mageController.find("Gandalf"));
    }

    @Test
    public void testNonExistentFind() {
        doReturn(Optional.empty()).when(mageRepo).find("Gandalf");
        MageController mageController = new MageController(mageRepo);

        assertEquals(MageController.MAGE_NOT_FOUND, mageController.find("Gandalf"));
    }


}
