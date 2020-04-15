package ru.otus.hw11.cachehw;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
  private final WeakHashMap<K, V> map = new WeakHashMap<>();
  private List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();


  @Override
  public void put(K key, V value) {
    map.put(key, value);
    notifyAll(key, value, "put");
  }

  @Override
  public void remove(K key) {
    var value = map.remove(key);
    notifyAll(key, value, "remove");
  }

  @Override
  public V get(K key) {
    return map.get(key);
  }

  private void notifyAll(K key, V value, String action) {
    listeners.forEach(x -> {
      var l = x.get();
      if (l != null) {
        l.notify(key, value, action);
      }
    });
  }

  @Override
  public void addListener(HwListener<K, V> listener) {
    listeners.add(new WeakReference<>(listener));
  }

  @Override
  public void removeListener(HwListener<K, V> listener) {
    listeners.removeIf(x -> x.get() == null || x.get() == listener);
  }
}
