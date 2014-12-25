package util;

import com.google.gson.Gson;

/**
 * Created by infinitu on 14. 12. 25..
 */
public abstract class JsonDataSerializable {
    private static ThreadLocal<Gson> gson = new ThreadLocal<Gson>(){
        @Override
        protected Gson initialValue() {
            return new Gson();
        }
    };

    public String serialize(){
        return gson.get().toJson(this);
    }

    public static <T> T deserialize(Class<T> cls, String data){
        return gson.get().fromJson(data,cls);
    }
}
