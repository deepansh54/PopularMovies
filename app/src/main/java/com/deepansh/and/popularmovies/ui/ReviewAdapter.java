package com.deepansh.and.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepansh.and.popularmovies.R;
import com.deepansh.and.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    private List<Review> reviews = new ArrayList<>();

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.update(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    public static class ReviewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.review_title_tv)
        TextView title;
        @Bind(R.id.review_tv)
        TextView reviewText;
        private Review review;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openReview(review);
                }
            });
        }

        private void openReview(Review review) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            itemView.getContext().getApplicationContext().startActivity(intent);
        }

        public void update(Review review) {
            this.review = review;
            reviewText.setText(review.getContent());
            title.setText(review.getAuthor());
        }
    }
}
