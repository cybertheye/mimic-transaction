package xyz.cybertheye;

import org.junit.Before;
import org.junit.Test;
import xyz.cybertheye.bean.Person;
import xyz.cybertheye.engine.Engine;
import xyz.cybertheye.server.Executor;

/**
 * @description:
 */
public class TestAtomic {
    Engine engine = new Engine();
    @Before
    public void init(){
        Executor executor = new Executor(engine);

        executor.insert(new Person("cy1",1,2,3));
        executor.insert(new Person("cy2",3,2,34));
        executor.insert(new Person("cy3",14,25,3));
        executor.insert(new Person("cy4",1,24,3));
        executor.insert(new Person("cy5",19,2,38));
    }

    @Test
    public void test(){
        Executor executor = new Executor(engine);
        executor.select(3);
        executor.update(3,"name","ccccccc");

        executor.select(3);
    }
    @Test
    public void testRollback(){
        Executor executor = new Executor(engine);
        executor.begin();

        executor.select(3);
        executor.update(3,"name","hello world");
        executor.select(3);
        executor.update(3,"age",999);
        executor.select(3);

        executor.rollback();
        executor.select(3);

    }

    @Test
    public void testCommit(){
        Executor executor = new Executor(engine);
        executor.begin();

        executor.select(3);
        executor.update(3,"name","hello world");
        executor.select(3);
        executor.update(3,"age",999);
        executor.select(3);

        executor.commit();
        executor.select(3);
    }
}
