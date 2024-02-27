package lab1;

public class Lab1Main {
    public static void main(String[] args) {
        String orderMode = args[0];
        ModelClass mage1 = new ModelClass("Gandalf", 20, 100, orderMode);
        ModelClass mage11 = new ModelClass("App 1 of Gandalf", 2, 10, orderMode);
        ModelClass mage111 = new ModelClass("App 1 of App 1 of Gandalf", 1, 50, orderMode);
        ModelClass mage12 = new ModelClass("App 2 of Gandalf", 1, 30, orderMode);
        ModelClass mage2 = new ModelClass("Saruman", 20, 100, orderMode);

        mage1.addApprentice(mage11);
        mage11.addApprentice(mage111);
        mage1.addApprentice(mage12);

        mage1.print(0);

        System.out.println(mage1.getApprenticeStats());
    }
}
