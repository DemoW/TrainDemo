package lishui.study.common.util;

/**
 * Created by lishui.lin on 19-12-25
 */
public abstract class Singleton<T> {

    private T mInstance;

    protected abstract T create();

    public final T get() {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create();
            }
            return mInstance;
        }
    }
}