package lishui.study.http;

import lishui.study.bean.WanResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lishui.lin on 19-11-13
 */
public abstract class WanResultCallback<T> implements Callback<WanResult<T>> {

    @Override
    public void onResponse(Call<WanResult<T>> call, Response<WanResult<T>> response) {
        if (response.isSuccessful() && response.body() != null) {
            onResponse(response.body().getData());
        } else {
            onResponse(null);
        }
    }

    @Override
    public void onFailure(Call<WanResult<T>> call, Throwable t) {
        onFailure(t);
    }

    public abstract void onResponse(T t);

    public abstract void onFailure(Throwable throwable);
}
