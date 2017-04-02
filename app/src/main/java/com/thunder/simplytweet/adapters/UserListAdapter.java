package com.thunder.simplytweet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thunder.simplytweet.R;
import com.thunder.simplytweet.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anlinsquall on 2/4/17.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.user_list_profile)
        ImageView profile;
        @BindView(R.id.user_list_name)
        TextView name;
        @BindView(R.id.user_list_screen_name)
        TextView screenName;
        @BindView(R.id.user_list_tagline)
        TextView tagLine;
        @BindView(R.id.user_list_add)
        ImageButton add;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    ArrayList<User> users;
    private Context context;

    private Context getContext() {
        return this.context;
    }

    public UserListAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View userView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(userView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        Glide.with(context).load(user.getProfileImageUrl()).into(holder.profile);
        holder.name.setText(user.getName());
        holder.screenName.setText("@"+user.getScreenName());
        holder.tagLine.setText(user.getTagline());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "click");
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addAll(List<User> users){
        this.users.addAll(users);
        notifyDataSetChanged();
    }
}
