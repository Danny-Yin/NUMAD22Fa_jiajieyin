package edu.northeastern.NUMAD22Fa_jiajieyin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinkViewHolder extends RecyclerView.ViewHolder {

    public TextView linkName;
    public TextView linkURL;

    public LinkViewHolder(@NonNull View itemView, final LinkItemClickListener listener){
        super(itemView);
        this.linkName = itemView.findViewById(R.id.linkName);
        this.linkURL = itemView.findViewById(R.id.linkURL);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onLinkItemClick(position);
                    }
                }
            }
        });
    }

    public void bindThisData(LinkItem linkToBind) {
        // sets the name of the link to the linkURL textview of the viewholder.
        linkName.setText(linkToBind.getLinkName());
        // sets the URL of the link to the linkURL textview of the viewholder.
        linkURL.setText(linkToBind.getLinkURL());
    }
}
