package com.deepansh.and.popularmovies.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepansh.and.popularmovies.R;
import com.deepansh.and.popularmovies.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    public void setVideos(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    private List<Video> videos = new ArrayList<>();

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        holder.update(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }

    public static class TrailerHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.trailer_cover_iv)
        ImageView cover;
        @Bind(R.id.trailer_title_tv)
        TextView title;
        private Video video;

        public TrailerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openVideo(video);
                }
            });
        }

        private void openVideo(Video video) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getKey()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itemView.getContext().getApplicationContext().startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itemView.getContext().getApplicationContext().startActivity(intent);
            }
        }

        public void update(Video video) {
            this.video = video;
            Picasso.with(itemView.getContext()).load("http://img.youtube.com/vi/" + video.getKey() + "/mqdefault.jpg").fit().centerCrop().into(cover);
            title.setText(video.getName());
        }
    }
}
