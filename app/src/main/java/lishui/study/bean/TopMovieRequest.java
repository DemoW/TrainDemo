package lishui.study.bean;

/**
 * Created by lishui.lin on 18-8-1 19:37
 */
public class TopMovieRequest {
	private int start;
	private int count;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "TopMovieRequest{" +
				"startAnim=" + start +
				", count=" + count +
				'}';
	}
}
