package com.example.webstiteblocker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UrlListAdapter extends RecyclerView.Adapter<UrlListViewHolder> {
    public List<String> list;
    public UrlListAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UrlListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_items, parent, false);
        return new UrlListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UrlListViewHolder holder, int position) {
        String url = list.get(position);
        holder.tv_name.setText(url);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class UrlListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tv_name;

    public UrlListViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
