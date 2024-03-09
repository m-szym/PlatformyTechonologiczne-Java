package lab4;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name = "towers")
public class Tower {
    @Id
    @Column(name = "towerName")
    private String name;

    @Column(name = "towerHeight")
    private int height;

    @OneToMany
    private List<Mage> mages;


    public Tower() {

    }

    public Tower(String name, int height, List<Mage> mages) {
        this.name = name;
        this.height = height;
        this.mages = mages;
    }

    public Tower(String name, int height) {
        this.name = name;
        this.height = height;
        this.mages = new ArrayList<Mage>();
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public List<Mage> getMages() {
        return mages;
    }

    public void setMages(List<Mage> mages) {
        this.mages = mages;
    }

    public void addMage(Mage mage) {
        mages.add(mage);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tower tower = (Tower) o;
        return Objects.equals(name, tower.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Tower{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", mages=" + mages +
                '}';
    }
}
