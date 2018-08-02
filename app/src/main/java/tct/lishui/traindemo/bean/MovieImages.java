package tct.lishui.traindemo.bean;

/**
 * Created by lishui.lin on 18-8-1 20:33
 */
public class MovieImages {
	private String samll;
	private String large;
	private String medium;

	public String getSamll() {
		return samll;
	}

	public void setSamll(String samll) {
		this.samll = samll;
	}

	public String getLarge() {
		return large;
	}

	public void setLarge(String large) {
		this.large = large;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	@Override
	public String toString() {
		return "MovieImages{" +
				"samll='" + samll + '\'' +
				", large='" + large + '\'' +
				", medium='" + medium + '\'' +
				'}';
	}
}
