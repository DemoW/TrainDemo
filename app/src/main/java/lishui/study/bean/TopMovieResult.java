package lishui.study.bean;

/**
 * Created by lishui.lin on 18-8-1 19:33
 */
public class TopMovieResult<T> {

	private int start;
	private int count;
	private int total;
	private String title;
	private T subjects;

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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public T getSubjects() {
		return subjects;
	}

	public void setSubjects(T subjects) {
		this.subjects = subjects;
	}

	@Override
	public String toString() {
		return "TopMovieResult{" +
				"startAnim=" + start +
				", count=" + count +
				", total=" + total +
				", title='" + title + '\'' +
				", subjects=" + subjects +
				'}';
	}
}
