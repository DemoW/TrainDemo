package lishui.study.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lishui.lin on 18-7-31
 */
public class WanResult<T> {
    @SerializedName("data")
	private T data;
	private int errorCode;
	private String errorMsg;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "WanResult{" +
				"data=" + data +
				", errorCode=" + errorCode +
				", errorMsg='" + errorMsg + '\'' +
				'}';
	}
}
