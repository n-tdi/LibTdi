package world.ntdi.libtdi.Json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSONList extends ArrayList<Object> implements JSONStorage {
    private JSONStorage parent;
    protected String key = null;

    public JSONList() {
    }

    public Integer getInt(int key) {
        Object o = this.get(key);
        return o instanceof Long ? (Integer) o : (Integer)o;
    }

    public Boolean getBoolean(int key) {
        return (Boolean)this.get(key);
    }

    public Long getLong(int key) {
        return (Long)this.get(key);
    }

    public Double getDouble(int key) {
        return (Double)this.get(key);
    }

    public JSONList getList(int key) {
        return (JSONList)this.get(key);
    }

    public JSONMap getMap(int key) {
        return (JSONMap)this.get(key);
    }

    public String getString(int key) {
        return (String)this.get(key);
    }

    public <T> List<T> cast(Class<T> clazz) {
        Stream var10000 = this.stream();
        Objects.requireNonNull(clazz);
        return (List)var10000.map(clazz::cast).collect(Collectors.toList());
    }

    public String toString() {
        if (this.size() == 0) {
            return "[]";
        } else {
            StringBuilder builder = new StringBuilder("[");
            Iterator var2 = this.iterator();

            while(var2.hasNext()) {
                Object o = var2.next();
                if (o instanceof CharSequence) {
                    builder.append('"').append(o.toString().replace("\\", "\\\\").replace("\"", "\\\"")).append("\", ");
                } else if (o instanceof Long) {
                    builder.append(o.toString()).append("L, ");
                } else {
                    builder.append(o).append(", ");
                }
            }

            return builder.replace(builder.length() - 2, builder.length(), "]").toString();
        }
    }

    public JSONStorage getParent() {
        return this.parent;
    }

    public void setParent(JSONStorage obj) {
        this.parent = obj;
    }

    public void add(String key, Object value) {
        this.add(value);
    }

    public String getTempKey() {
        return this.key;
    }

    public void setTempKey(String value) {
        this.key = value;
    }
}
