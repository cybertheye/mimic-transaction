package xyz.cybertheye;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;
import xyz.cybertheye.bean.Person;
import xyz.cybertheye.engine.Engine;
import xyz.cybertheye.engine.store.StorageManager;
import xyz.cybertheye.engine.store.StorageManagerImpl;
import xyz.cybertheye.server.Executor;

/**
 * @description:
 */
@OrderWith(Alphanumeric.class)
public class TestBasic {
    Engine engine = new Engine();
    @Before
    public void fabricateData(){
        Executor executor = new Executor(engine);

        executor.insert(new Person("cy1",1,2,3));
        executor.insert(new Person("cy2",3,2,34));
        executor.insert(new Person("cy3",14,25,3));
        executor.insert(new Person("cy4",1,24,3));
        executor.insert(new Person("cy5",19,2,38));
    }


    @Test
    public void test1(){
        Executor executor = new Executor(engine);
        executor.select(3);
        executor.update(3,"name","hello world");
        executor.select(3);
    }


}
