package lab1;

import java.util.HashSet;
import java.util.TreeSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.Objects;

public class ModelClass
    implements Comparable<ModelClass>,
                Comparator<ModelClass> {
    public static int MODE_UNORDERED = 0;
    public static int MODE_ORDER_NAT = 1;
    public static int MODE_ORDER_ALT= 2;

    // Mage
    private String Name;
    private int level;
    private int power;
    private Set<ModelClass> apprentices;

    public ModelClass(String name, int level, int power, Set<ModelClass> apprentices) {
        Name = name;
        this.level = level;
        this.power = power;
        this.apprentices = apprentices;
    }

    public ModelClass(String name, int level, int power, int orderMode) {
        Name = name;
        this.level = level;
        this.power = power;

        if (orderMode == MODE_UNORDERED)
            this.apprentices = new HashSet<>();
        else if (orderMode == MODE_ORDER_NAT)
            this.apprentices = new TreeSet<>();
        else if (orderMode == MODE_ORDER_ALT)
            this.apprentices = new TreeSet<>(this::compare);
    }

    public ModelClass(String name, int level, int power, String orderMode) {
        Name = name;
        this.level = level;
        this.power = power;

        if (Objects.equals(orderMode, "un"))
            this.apprentices = new HashSet<>();
        else if (Objects.equals(orderMode, "nat"))
            this.apprentices = new TreeSet<>();
        else if (Objects.equals(orderMode, "alt"))
            this.apprentices = new TreeSet<>(this::compare);
    }

    public void addApprentice(ModelClass mage) {
        apprentices.add(mage);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ModelClass))
            return false;

        ModelClass other = (ModelClass) o;
        return  level == other.level &&
                power == other.power &&
                Name.equals(other.Name);
    }

    public int hashCode() {
        return Name.hashCode() + level + power;
    }

    public String toString() {
        return "Mage{" +
                "Name='" + Name + '\'' +
                ", level=" + level +
                ", power=" + power +
                '}';
    }

    public void print(int recursionLevel) {
        for (int i = 0; i < recursionLevel; i++)
            System.out.print("-");
        System.out.println(this);

        if (apprentices != null) {
            for (ModelClass apprentice : apprentices) {
                apprentice.print(recursionLevel + 1);
            }
        }
    }

    public int countApprentices() {
        int count = 0;
        if (apprentices != null) {
            count = apprentices.size();
            for (ModelClass apprentice : apprentices) {
                count += apprentice.countApprentices();
            }
        }
        return count;
    }

    public Map getApprenticeStats() {
        Map<String, Integer> stats;
        if (this.apprentices instanceof HashSet<ModelClass>)
            stats = new HashMap<>();
        else if (this.apprentices instanceof TreeSet<ModelClass>)
            stats = new TreeMap<>();
        else
            return null;

        for (ModelClass apprentice : apprentices) {
            stats.put(apprentice.Name, apprentice.countApprentices());
        }

        return stats;
    }

    // From Comparable
    @Override
    public int compareTo(ModelClass o) {
        return Integer.compare(level, o.level);
    }

    // From Comparator
    @Override
    public int compare(ModelClass o1, ModelClass o2) {
        int res = Integer.compare(o1.power, o2.power);
        if (res == 0)
            res = Integer.compare(o1.apprentices.size(), o2.apprentices.size());
        return res;
    }



}
