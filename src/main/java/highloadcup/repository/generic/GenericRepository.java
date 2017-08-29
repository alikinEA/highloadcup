package highloadcup.repository.generic;

import highloadcup.models.common.Identificable;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alikin E.A. on 26.08.17.
 */
public abstract class GenericRepository<T extends Identificable>{

    protected final ConcurrentHashMap<Integer,T> map = new ConcurrentHashMap<>();

    public T getById(Integer id) {
        return this.map.get(id);
    }

    public T put(T item) {
        return map.put(item.getId(),item);
    }

}
