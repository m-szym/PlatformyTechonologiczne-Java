package lab5;

import java.util.Collection;
import java.util.Optional;

public class MageRepository {
    private final Collection<Mage> mages;
    public static final String MAGE_NOT_FOUND = "not found";
    public static final String MAGE_ALREADY_EXISTS = "bad request";

    public MageRepository(Collection<Mage> mages) {
        this.mages = mages;
    }

    public Optional<Mage> find(String name) {
        try {
            return mages.stream().filter(mage -> mage.getName().equals(name)).findFirst();
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public void delete(String name) {
        Optional<Mage> mage = find(name);
        if (mage.isPresent()) {
            mages.remove(mage.get());
        } else {
            throw new IllegalArgumentException(MAGE_NOT_FOUND);
        }
    }

    public void save(Mage mage) {
        Optional<Mage> existingMage = find(mage.getName());
        if (existingMage.isPresent()) {
            throw new IllegalArgumentException(MAGE_ALREADY_EXISTS);
        }
        mages.add(mage);
    }
}
