package com.imrenagi.greflashcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.imrenagi.greflashcard.R;
import com.imrenagi.greflashcard.model.Word;

import java.util.List;

/**
 * Created by imrenagi on 5/19/15.
 */
public class FlashcardAdapter extends ArrayAdapter<Word> {

    private LayoutInflater mInflater;

    private Context context;

    private int layoutResourceId;

    private List<Word> words = null;

    public FlashcardAdapter(Context context, int layoutResourceId, List<Word> objects) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.words = objects;
        mInflater = (LayoutInflater) (this.context)
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateList(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        this.words.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Word getItem(int position) {
        return words.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        EventHolder holder;

        if (row == null) {
            row = mInflater.inflate(layoutResourceId, parent, false);
            holder = new EventHolder(row);
            row.setTag(holder);
        } else {
            holder = (EventHolder) row.getTag();
        }

        Word word = words.get(position);

        holder.word.setText(word.name);
        holder.meaning.setText(word.meaning);

        return row;
    }

    static class EventHolder {

        TextView word;
        TextView meaning;

        public EventHolder(View view) {
            word = (TextView) view.findViewById(R.id.word);
            meaning = (TextView) view.findViewById(R.id.meaning);
        }
    }

}
