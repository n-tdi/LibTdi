package world.ntdi.libtdi.Json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JSONMap extends HashMap<String, Object> implements JSONStorage {
    private JSONStorage parent;
    protected String key;

    public JSONMap() {
    }

    public Integer getInt(String key) {
        Object o = this.get(key);
        return o instanceof Long ? (int) o : (Integer)o;
    }

    public Boolean getBoolean(String key) {
        return (Boolean)this.get(key);
    }

    public Double getDouble(String key) {
        return (Double)this.get(key);
    }

    public Long getLong(String key) {
        return (Long)this.get(key);
    }

    public JSONList getList(String key) {
        return (JSONList)this.get(key);
    }

    public JSONMap getMap(String key) {
        return (JSONMap)this.get(key);
    }

    public String getString(String key) {
        return (String)this.get(key);
    }

    public String toString() {
        if (this.size() == 0) {
            return "{}";
        } else {
            StringBuilder builder = new StringBuilder("{");
            Iterator var2 = this.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var2.next();
                builder.append('"').append((String)entry.getKey()).append('"').append(':');
                Object o = entry.getValue();
                if (o instanceof CharSequence) {
                    builder.append('"').append(o.toString().replace("\\", "\\\\").replace("\"", "\\\"")).append("\", ");
                } else if (o instanceof Long) {
                    builder.append(o.toString()).append("L, ");
                } else {
                    builder.append(o).append(", ");
                }
            }

            return builder.replace(builder.length() - 2, builder.length(), "}").toString();
        }
    }

    public JSONStorage getParent() {
        return this.parent;
    }

    public void setParent(JSONStorage obj) {
        this.parent = obj;
    }

    public void add(String key, Object value) {
        this.put(key, value);
    }

    public String getTempKey() {
        return this.key;
    }

    public void setTempKey(String value) {
        this.key = value;
    }
}
