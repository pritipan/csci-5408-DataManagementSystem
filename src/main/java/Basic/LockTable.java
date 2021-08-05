package Basic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static Basic.LogGenerator.logQueryExecute;

public class LockTable {
    public static void acquireLocks(Lock lock, String opName) throws InterruptedException {
        TimeUnit unit = TimeUnit.valueOf("SECONDS");
        try {
            lock.tryLock(100000, unit);    // Thread tries to get the lock
        } finally {
            logQueryExecute(opName, Thread.currentThread().getName() + " thread " + " acquires the lock ");
            System.out.println(opName + " in the " + Thread.currentThread().getName() + " thread " + " acquires the lock ");
        }
    }

    public static void releaseLock(Lock lock, String opName) {
        try {
            lock.unlock();        // Thread releases the lock
            logQueryExecute(opName, Thread.currentThread().getName() + " releases the lock ");
            System.out.println(Thread.currentThread().getName() + " thread " + " releases the lock ");
        } finally {
        }
    }
}
