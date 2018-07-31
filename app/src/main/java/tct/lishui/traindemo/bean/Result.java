package tct.lishui.traindemo.bean;

/**
 * Created by lishui.lin on 18-7-31 09:20
 */
public class Result<T> {
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
		return "Result{" +
				"data=" + data +
				", errorCode=" + errorCode +
				", errorMsg='" + errorMsg + '\'' +
				'}';
	}
}
