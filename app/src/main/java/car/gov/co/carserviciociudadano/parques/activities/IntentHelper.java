package car.gov.co.carserviciociudadano.parques.activities;

import java.util.Hashtable;

/**
 * Created by Olger on 17/12/2016.
 */

public final class IntentHelper {
    private static IntentHelper instance = null;

    private Hashtable<String, Object> mHash;

    private IntentHelper() {
        mHash = new Hashtable<>();
    }
    public static synchronized IntentHelper getInstance(){
        if (instance == null)
            instance = new IntentHelper();

        return instance;
    }
    public static void addObjectForKey(Object object, String key) {
        getInstance().mHash.put(key, object);
    }
    public static Object getObjectForKey(String key) {
        IntentHelper helper = getInstance();
        Object data = helper.mHash.get(key);
        helper.mHash.remove(key);
        helper = null;
        return data;
    }
}
