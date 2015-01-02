package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by infinitu on 14. 12. 25..
 */
public abstract class JsonDataSerializable {
    protected static ThreadLocal<Gson> gson = new ThreadLocal<Gson>(){
        @Override
        protected Gson initialValue() {
            GsonBuilder gb = new GsonBuilder();
            gb.excludeFieldsWithoutExposeAnnotation();
            return gb.create();
        }
    };

    public String serialize(){
        return gson.get().toJson(this);
    }

    public static <T> T deserialize(Class<T> cls, String data){
        return gson.get().fromJson(data,cls);
    }
}
