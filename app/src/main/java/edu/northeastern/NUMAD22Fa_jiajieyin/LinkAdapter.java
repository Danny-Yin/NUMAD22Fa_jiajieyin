package edu.northeastern.NUMAD22Fa_jiajieyin;

import android.app.LauncherActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import java.util.List;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class LinkAdapter extends RecyclerView.Adapter<LinkViewHolder>{
    private final List<LinkItem> links;
    private  LinkItemClickListener listener;

    public LinkAdapter(List<LinkItem> links){
        this.links = links;
    }

    public void setOnItemClickListener(LinkItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_item, parent, false);
        return new LinkViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
        holder.bindThisData(links.get(position));

    }

    @Override
    public int getItemCount() {
        // Returns the size of the recyclerview that is the list of the arraylist.
        return links.size();
    }
}
