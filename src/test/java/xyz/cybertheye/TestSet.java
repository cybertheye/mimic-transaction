package xyz.cybertheye;

import org.junit.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 */
public class TestSet {
    @Test
    public void test(){
        Set<Integer> test = new HashSet<>();
        test.add(1);

        Integer integer = test.stream().min(Comparator.comparingInt(o -> o)).get();
        System.out.println(integer);
    }
}
