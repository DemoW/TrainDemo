package tct.lishui.traindemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tct.lishui.traindemo.R;
import tct.lishui.traindemo.bean.TopMovieSubject;

/**
 * Created by lishui.lin on 18-8-1 16:59
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

	private Context mContext;
	private List<TopMovieSubject> datas;
	public MovieAdapter(Context mContext,List<TopMovieSubject> datas) {
		this.mContext = mContext;
		this.datas = datas;
	}

	public void clearMovies(){
		datas.clear();
	}
	public void setMovies(List<TopMovieSubject> newData){
		this.datas = newData;
	}
	@NonNull
	@Override
	public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_movie_view, viewGroup, false);
		return new MovieViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
		TopMovieSubject topMovieSubject = datas.get(i);
		String url = topMovieSubject.getImages().getMedium();
		if (url != null && !url.isEmpty()) {
			Glide.with(mContext).load(topMovieSubject.getImages().getMedium()).into(movieViewHolder.image_iv);
		}
		movieViewHolder.title_tv.setText("电影名称： " + topMovieSubject.getTitle());
		movieViewHolder.original_title_tv.setText("本地名称： " + topMovieSubject.getOriginal_title());
		movieViewHolder.type_tv.setText("电影类型： "+topMovieSubject.getGenres().toString());
		movieViewHolder.average_tv.setText("电影评分： " + topMovieSubject.getRating().getAverage());
		movieViewHolder.year_tv.setText("上映年份： " + topMovieSubject.getYear());
	}

	@Override
	public int getItemCount() {
		return datas.size();
	}

	static class MovieViewHolder extends RecyclerView.ViewHolder{
		ImageView image_iv;
		TextView title_tv;
		TextView original_title_tv;
		TextView type_tv;
		TextView average_tv;
		TextView year_tv;

		public MovieViewHolder(@NonNull View itemView) {
			super(itemView);
			image_iv = itemView.findViewById(R.id.movie_image);
			title_tv = itemView.findViewById(R.id.movie_title);
			original_title_tv = itemView.findViewById(R.id.movie_original_name);
			type_tv = itemView.findViewById(R.id.movie_type);
			average_tv = itemView.findViewById(R.id.movie_average);
			year_tv = itemView.findViewById(R.id.movie_year);
		}
	}
}
