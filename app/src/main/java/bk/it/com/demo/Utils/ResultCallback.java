package bk.it.com.demo.Utils;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/5/3.
 */

public abstract class ResultCallback<T> {

    //这是请求数据的返回类型 包含常见的 Bean List等
    Type mType;


    public ResultCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    /**
     * 通过反射想要的返回类型
     *
     * @param subclass
     * @return
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    /**
     * 在请求之前的方法，一般用于加载框显示
     */
    public void oBefore(Request request) {

    }

    /**
     * 在请求之后的方法，一般用于加载框隐藏
     */
    public void onAften() {

    }


    /**
     * 请求失败的时候
     */
    public abstract void onError(Request request, Exception e);


    public abstract void onResponse(T response);

}
