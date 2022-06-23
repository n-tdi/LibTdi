package world.ntdi.libtdi.Json;

interface JSONStorage {
    JSONStorage getParent();

    void setParent(JSONStorage var1);

    void add(String var1, Object var2);

    String getTempKey();

    void setTempKey(String var1);
}
