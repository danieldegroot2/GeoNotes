package de.hauke_stieler.geonotes.note_list;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hauke_stieler.geonotes.R;
import de.hauke_stieler.geonotes.notes.Note;
import de.hauke_stieler.geonotes.notes.NoteIconProvider;

public class NoteListAdapter extends BaseAdapter {
    public interface NoteListClickListener {
        void onClick(long id);
    }

    private final Context context;
    private final NoteIconProvider noteIconProvider;
    private final List<Note> notes;
    private final List<Note> notesWithPhoto;
    private final NoteListClickListener clickListener;
    private final LayoutInflater inflater;

    public NoteListAdapter(Context context, NoteIconProvider noteIconProvider, List<Note> notes, List<Note> notesWithPhoto, NoteListClickListener clickListener) {
        this.context = context;
        this.noteIconProvider = noteIconProvider;
        this.notes = notes;
        this.notesWithPhoto = notesWithPhoto;
        this.clickListener = clickListener;

        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int index) {
        return notes.get(index);
    }

    @Override
    public long getItemId(int index) {
        return notes.get(index).getId();
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.note_list_row, null);

        Note note = getItem(index);
        boolean noteHasPhotos = notesWithPhoto.contains(note);
        view.setOnClickListener(v -> this.clickListener.onClick(note.getId()));

        ImageView icon = view.findViewById(R.id.note_list_row_icon);
        fillIconView(noteHasPhotos, note.getCategory().getId(), icon);

        TextView text = view.findViewById(R.id.note_list_row_text_view);
        fillTextView(note, noteHasPhotos, text);

        return view;
    }

    void fillTextView(Note note, boolean noteHasPhotos, TextView text) {
        if (noteHasPhotos && note.getDescription().trim().isEmpty()) {
            text.setText("(only photo)");
            text.setTypeface(null, Typeface.ITALIC);
            text.setTextColor(context.getResources().getColor(R.color.grey));
        } else {
            text.setText(note.getDescription());
        }
    }

    void fillIconView(boolean noteHasPhotos, long categoryId, ImageView icon) {
        icon.setImageDrawable(noteIconProvider.getIcon(categoryId, false, noteHasPhotos));
    }
}
