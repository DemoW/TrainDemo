package lishui.study.bean;

/**
 * Created by lishui.lin on 18-8-1 20:30
 */
public class MovieRate {
	private int min;
	private int max;
	private float average;

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public float getAverage() {
		return average;
	}

	public void setAverage(float average) {
		this.average = average;
	}

	@Override
	public String toString() {
		return "MovieRate{" +
				"min=" + min +
				", max=" + max +
				", average=" + average +
				'}';
	}
}
