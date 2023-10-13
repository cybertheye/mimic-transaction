package xyz.cybertheye.bean;

/**
 * @description:
 */
public class Person extends Item implements Cloneable{
    private static int count = 1;


    private String name;
    private int age;
    private int weight;
    private int height;


    public Person(String name, int age, int weight, int height) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    @Override
    protected int getCount() {
        return count++;
    }


    //tostring

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", trxId=" + trxId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                '}';
    }


    //clone

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    //getter, setter

    public static void setCount(int count) {
        Person.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
