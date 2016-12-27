package com.mshvdvskgmail.fordearbnet.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mshvdvskgmail.fordearbnet.R;
import com.mshvdvskgmail.fordearbnet.models.Note;
import java.util.List;

/**
 * Adapter for the NoteListFragment, for determine how to display notes in the list.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>{

    private List<Note> mNotes;


    public NoteListAdapter(List<Note> notes){
        mNotes = notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.noteDate.setText(mNotes.get(position).getReadableDate());
        holder.noteContent.setText(mNotes.get(position).getTwoHundredSignsContent());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView noteContent, noteDate;



        public ViewHolder(View itemView) {
            super(itemView);
            noteDate = (TextView) itemView.findViewById(R.id.text_view_note_date_created_or_created_and_modified);
            noteContent = (TextView) itemView.findViewById(R.id.text_view_note_content);
        }

    }

    public void refreshData (List<Note> notes){
        mNotes.clear();
        mNotes.addAll(notes);
        notifyDataSetChanged();
    }



}
