package tct.lishui.traindemo.bean;

import java.util.List;

/**
 * Created by lishui.lin on 18-8-1 19:39
 */
public class TopMovieSubject {
	private String title;
	private String original_title;
	private String year;
	private MovieRate rating;
	private MovieImages images;
	private List<String> genres;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginal_title() {
		return original_title;
	}

	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public MovieRate getRating() {
		return rating;
	}

	public void setRating(MovieRate rating) {
		this.rating = rating;
	}

	public MovieImages getImages() {
		return images;
	}

	public void setImages(MovieImages images) {
		this.images = images;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	@Override
	public String toString() {
		return "TopMovieSubject{" +
				"title='" + title + '\'' +
				", original_title='" + original_title + '\'' +
				", year='" + year + '\'' +
				", rating=" + rating +
				", images=" + images +
				", genres=" + genres +
				'}';
	}
}
