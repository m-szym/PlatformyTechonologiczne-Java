package lab5;

import java.util.Optional;

public class MageController {
    private final MageRepository mageRepository;
    public static final String DELETE_SUCCESS = "done";
    public static final String TO_DELETE_NOT_FOUND = "not found";
    public static final String MAGE_NOT_FOUND = "not found";
    public static final String SAVE_SUCCESS = "saved";
    public static final String SAVE_ID_REPEAT = "bad request";

    public MageController(MageRepository mageRepository) {
        this.mageRepository = mageRepository;
    }

    public String find(String name) {
        Optional<Mage> optionalMage = mageRepository.find(name);
        if (optionalMage.isPresent()) {
            return optionalMage.get().toString();
        } else {
            return MAGE_NOT_FOUND;
        }
    }

    public String delete(String name) {
        try {
            mageRepository.delete(name);
            return DELETE_SUCCESS;
        } catch (IllegalArgumentException e) {
            return TO_DELETE_NOT_FOUND;
        }
    }

    public String save(String name, int level) {
        try {
            mageRepository.save(new Mage(name, level));
            return SAVE_SUCCESS;
        } catch (IllegalArgumentException e) {
            return SAVE_ID_REPEAT;
        }
    }
}
