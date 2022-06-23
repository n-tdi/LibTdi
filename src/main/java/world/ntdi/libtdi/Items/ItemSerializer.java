package world.ntdi.libtdi.Items;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import world.ntdi.libtdi.Json.JSONList;
import world.ntdi.libtdi.Json.JSONMap;
import world.ntdi.libtdi.NMS.NMSHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

class ItemSerializer {
    private static Map<Class<?>, Function<Map<String, Object>, ?>> deserializers = new HashMap();

    ItemSerializer() {
    }

    private static Object invokeDeserialize(Class<?> clazz, Map<String, Object> data) {
        if (!deserializers.containsKey(clazz)) {
            DelegateDeserialization annotation = (DelegateDeserialization)clazz.getAnnotation(DelegateDeserialization.class);
            Class<?> target = annotation == null ? clazz : annotation.value();
            boolean found = false;

            try {
                Method method = target.getDeclaredMethod("deserialize", Map.class);
                deserializers.put(clazz, ItemSerializer.EFunction.wrap((m) -> {
                    return method.invoke((Object)null, m);
                }));
                found = true;
            } catch (NoSuchMethodException var7) {
            }

            if (!found) {
                try {
                    Constructor<?> con = target.getDeclaredConstructor(Map.class);
                    Map var10000 = deserializers;
                    Objects.requireNonNull(con);
                    var10000.put(clazz, ItemSerializer.EFunction.wrap((xva$0) -> {
                        return con.newInstance(xva$0);
                    }));
                    found = true;
                } catch (NoSuchMethodException var6) {
                }

                if (!found) {
                    throw new IllegalStateException("No suitable deserialization method found for " + clazz);
                }
            }
        }

        return ((Function)deserializers.get(clazz)).apply(data);
    }

    private static Object deserializeObject(JSONMap map) {
        try {
            Class<?> clazz = Class.forName(map.getString("==").replace("%version%", NMSHelper.getNMSVersion()));
            return invokeDeserialize(clazz, map);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }


    public static Object recursiveDeserialize(Object obj) {
        if (obj instanceof JSONMap) {
            JSONMap map = (JSONMap)obj;
            map.keySet().forEach((k) -> {
                map.put(k, recursiveDeserialize(map.get(k)));
            });
            if (map.containsKey("==")) {
                return deserializeObject(map);
            }
        }

        if (obj instanceof JSONList) {
            JSONList list = (JSONList)obj;

            for(int i = 0; i < list.size(); ++i) {
                list.set(i, recursiveDeserialize(list.get(i)));
            }
        }

        return obj;
    }

    public static JSONMap toJSON(ConfigurationSerializable s, Class<?> clazz) {
        Map<String, Object> map = s.serialize();
        JSONMap json = new JSONMap();
        json.put("==", clazz.getName().replace(NMSHelper.getNMSVersion(), "%version%"));
        map.forEach((k, v) -> {
            json.put(k, serialize(v));
        });
        return json;
    }

    public static Object serialize(Object o) {
        if (o instanceof ConfigurationSerializable) {
            Class<?> clazz = o.getClass();
            return toJSON((ConfigurationSerializable)o, clazz);
        } else if (o instanceof Map) {
            Map map = (Map)o;
            JSONMap json = new JSONMap();
            map.forEach((k, v) -> {
                json.put(k.toString(), serialize(v));
            });
            return json;
        } else if (o instanceof List) {
            List list = (List)o;
            JSONList json = new JSONList();
            Stream var10000 = list.stream().map(ItemSerializer::serialize);
            Objects.requireNonNull(json);
            var10000.forEach(json::add);
            return json;
        } else {
            return o;
        }
    }

    private interface EFunction<A, B> {
        static <A, B> Function<A, B> wrap(EFunction<A, B> func) {
            return (a) -> {
                try {
                    return func.apply(a);
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return null;
                }
            };
        }

        B apply(A var1) throws Exception;
    }
}
