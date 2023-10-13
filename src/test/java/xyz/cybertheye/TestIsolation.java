package xyz.cybertheye;

import org.junit.Before;
import org.junit.Test;
import xyz.cybertheye.bean.Person;
import xyz.cybertheye.engine.Engine;
import xyz.cybertheye.engine.tx.TxLevel;
import xyz.cybertheye.server.Executor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @description:
 */
public class TestIsolation {
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
    public void testRC() throws InterruptedException {
        engine.setTxLevel(TxLevel.RC);
        extracted();

    }


    @Test
    public void testRR() throws InterruptedException {
        extracted();
    }

    private void extracted() throws InterruptedException {
        Executor executor = new Executor(engine);
        Semaphore semaphore = new Semaphore(1);
        CountDownLatch latch = new CountDownLatch(2);
        Runnable read = new Runnable() {
            @Override
            public void run() {
                try {
                    executor.begin();

                    semaphore.acquire();
                    System.out.println("read++++++1++++++");
                    executor.select(3);
                    System.out.println("read=============");
                    semaphore.release();
                    Thread.sleep(10);

                    semaphore.acquire();
                    System.out.println("read++++++2++++++");
                    executor.select(3);
                    System.out.println("read=============");
                    semaphore.release();
                    Thread.sleep(10);

                    semaphore.acquire();
                    System.out.println("read++++++3++++++");
                    executor.select(3);
                    System.out.println("read=============");
                    semaphore.release();
                    Thread.sleep(10);

                    executor.commit();

                    System.out.println("read++++++4++++++");
                    executor.select(3);
                    System.out.println("read=============");

                    latch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        };

        Runnable update = new Runnable() {
            @Override
            public void run() {
                try {
                    executor.begin();
                    Thread.sleep(10);
                    semaphore.acquire();
                    executor.update(3, "name", "hello world");
                    System.out.println("write++++++1++++++");
                    executor.select(3);
                    System.out.println("write=============");
                    semaphore.release();

                    Thread.sleep(10);
                    semaphore.acquire();
//                    executor.commit();
                    executor.rollback();
                    semaphore.release();


                    latch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        new Thread(read,"read").start();
        new Thread(update,"update").start();

        latch.await();
    }
}
