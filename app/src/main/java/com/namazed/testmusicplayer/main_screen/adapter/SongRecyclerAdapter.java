package com.namazed.testmusicplayer.main_screen.adapter;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.namazed.testmusicplayer.R;
import com.namazed.testmusicplayer.api.models.Song;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongRecyclerAdapter extends RecyclerView.Adapter<SongRecyclerAdapter.SongViewHolder> {

    private final Context context;
    private final SongAdapterListener songAdapterListener;
    private final Picasso picasso;
    private List<Song> listSongs;
    private boolean isGridView;

    public interface SongAdapterListener {
        void songClickListener(View view, Song song);
    }

    public SongRecyclerAdapter(Context context, List<Song> listSongs, SongAdapterListener songAdapterListener) {
        this.listSongs = listSongs;
        this.context = context;
        picasso = Picasso.with(context);
        this.songAdapterListener = songAdapterListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_song, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_song_grid_view, parent, false);
        }

        return new SongViewHolder((CardView) itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = listSongs.get(position);

        holder.nameArtistTextView.setText(song.getArtistName());
        holder.nameSongTextView.setText(song.getSongName());

        Uri uriImage = Uri.parse(song.getArtworkUrl100());
        picasso.load(uriImage)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.backgroundSongImageView);

        holder.itemView.setOnClickListener(view -> songAdapterListener.songClickListener(view, song));
    }

    @Override
    public int getItemCount() {
        return listSongs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (isGridView ? 1 : 0);
    }

    public void setGridViewType(boolean isGridView) {
        this.isGridView = isGridView;
        notifyDataSetChanged();
    }

    public void setData(List<Song> listSongs) {
        if (listSongs == null) {
            this.listSongs.clear();
            notifyDataSetChanged();
        } else {
            this.listSongs.clear();
            this.listSongs.addAll(listSongs);
            notifyDataSetChanged();
        }
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        ImageView backgroundSongImageView;
        TextView nameArtistTextView;
        TextView nameSongTextView;

        public SongViewHolder(CardView itemView) {
            super(itemView);
            backgroundSongImageView = (ImageView) itemView.findViewById(R.id.image_wrapper_album);
            nameArtistTextView = (TextView) itemView.findViewById(R.id.text_name_artist);
            nameSongTextView = (TextView) itemView.findViewById(R.id.text_name_song);
        }
    }
}
