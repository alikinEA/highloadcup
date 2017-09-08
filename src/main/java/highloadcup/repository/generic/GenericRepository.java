package highloadcup.repository.generic;

import highloadcup.models.common.Identificable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Alikin E.A. on 26.08.17.
 */
public abstract class GenericRepository<T extends Identificable>{

    //protected final Map<Integer, T> map = new ConcurrentHashMap<>(810_000, 1f);
    protected final Map<Integer, T> map = new HashMap<>(810_000, 1f);

    protected final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    public T getById(Integer id) {
        lock.readLock().lock();
        try {
            return this.map.get(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public T put(T item) {
        lock.writeLock().lock();
        try {
            return map.put(item.getId(),item);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

}
