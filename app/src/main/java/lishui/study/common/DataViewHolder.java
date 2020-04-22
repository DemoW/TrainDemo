package lishui.study.common;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by lishui.lin on 20-4-22
 */
public class DataViewHolder extends RecyclerView.ViewHolder {

    public ViewDataBinding binding;
    public DataViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
