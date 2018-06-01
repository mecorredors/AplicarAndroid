package car.gov.co.carserviciociudadano.Utils;


import android.content.Context;
import android.content.SharedPreferences;
import java.util.Set;

import car.gov.co.carserviciociudadano.AppCar;


/**
 * Created by Olger on 04/09/2016.
 */
public class PreferencesApp {
    private Context _context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private final static String DEFAULT_NAME = "PREFERENCES-APP-CIU";
    public final static String BICIAR_NAME = "PREFERENCES-BICICAR";

    public final static int READ = 0;
    public final static int WRITE = 1;

    public PreferencesApp(int type){

        this._context= AppCar.getContext();
        pref =   _context.getSharedPreferences(DEFAULT_NAME,Context.MODE_PRIVATE);
        if(type==WRITE)
            editor = pref.edit();
    }

    public PreferencesApp(int type,String name){
        this._context = AppCar.getContext();
        pref =   _context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public PreferencesApp(int type,String name,int mode){
        this._context = AppCar.getContext();
        pref =   _context.getSharedPreferences(name, mode);
        if(type==WRITE)
            editor = pref.edit();
    }

    public static PreferencesApp getDefault(int type){
        return new PreferencesApp(type);
    }

    public void commit(){
        editor.commit();
    }
    public void edit() {
        pref.edit();
    }

    public SharedPreferences.Editor putString(String key,String value){
        return editor.putString(key, value);
    }

    public  SharedPreferences.Editor putInt(String key,int value){
        return editor.putInt(key, value);
    }

    public  SharedPreferences.Editor putFloat(String key,float value){
        return editor.putFloat(key, value);
    }

    public  SharedPreferences.Editor putLong(String key,long value){
        return editor.putLong(key, value);
    }

    public SharedPreferences.Editor putBoolean(String key,boolean value){
        return   editor.putBoolean(key, value);
    }

    public  SharedPreferences.Editor putBoolean(String key,Set<String> values){
        return editor.putStringSet(key, values);
    }

    public String getString(String key,String defValue){
        return pref.getString(key, defValue);
    }
    public String getString(String key){
        return pref.getString(key,"");
    }

    public int getInt(String key,int defValue){
        return pref.getInt(key, defValue);
    }

    public float getFloat(String key,float defValue){
        return pref.getFloat(key, defValue);
    }

    public long getInt(String key,long defValue){
        return pref.getLong(key, defValue);
    }

    public boolean getBoolean(String key,boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public Long getLong(String key,Long defValue) {
        return pref.getLong(key, defValue);
    }

}
