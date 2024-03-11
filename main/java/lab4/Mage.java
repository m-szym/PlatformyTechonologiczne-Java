package lab4;

import jakarta.persistence.*;

@Entity
@Table(name = "mages")
public class Mage {
    @Id
    @Column(name = "mageName")
    private String name;

    @Column(name = "mageLevel")
    private int level;

    @ManyToOne
    @JoinColumn(name = "tower")
    private Tower tower;

    public Mage() {

    }

    public Mage(String name, int level, Tower tower) {
        this.name = name;
        this.level = level;
        this.tower = tower;
    }

    public Mage(String name, int level) {
        this.name = name;
        this.level = level;
        this.tower = null;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mage mage = (Mage) o;
        return name.equals(mage.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Mage{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", tower=" + tower.getName() +
                '}';
    }

}
