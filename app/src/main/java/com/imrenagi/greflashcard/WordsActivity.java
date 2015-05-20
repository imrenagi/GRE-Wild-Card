package com.imrenagi.greflashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.google.gson.Gson;
import com.imrenagi.greflashcard.adapter.FlashcardAdapter;
import com.imrenagi.greflashcard.adapter.FlashcardAdapter.FlashCardButtonListener;
import com.imrenagi.greflashcard.db.FeedReaderDbHelper;
import com.imrenagi.greflashcard.model.Word;
import com.imrenagi.greflashcard.model.Words;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class WordsActivity extends AppCompatActivity implements FlashCardButtonListener {

    private Toolbar toolbar;
    private ButtonFloat button;
    private SwipeFlingAdapterView flingContainer;
    private FlashcardAdapter adapter;
    private TextView title;

    private Gson gson = new Gson();
    private Words words;
    private String json = null;
    private ArrayList<Word> wordList = new ArrayList<>();

    private boolean isNewUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        title = (TextView) findViewById(R.id.category_title);
        title.setText(getString(R.string.part_one));
        button = (ButtonFloat) findViewById(R.id.buttonFloat);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WordsActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        create("dic-1.txt");

        Log.d("", "");

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        adapter = new FlashcardAdapter(this, R.layout.flashcard, wordList, this);

        flingContainer.setAdapter(adapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");

                if (isNewUpdate) {
                    Log.d("UPDATE", "TRUE");
                    isNewUpdate = false;
                    adapter.notifyDataSetChanged();
                    return;
                }

                wordList.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Log.d("LIST", "Remove To Left");
                wordList.add((Word)dataObject);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d("LIST", "Remove To Right");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Log.d("LIST", "Adapter About To Empty");
            }

            @Override
            public void onScroll(float v) {

            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_words, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        isNewUpdate = true;

        switch (id) {
            case R.id.action_part_1:
                title.setText(getString(R.string.part_one));
                create("dic-1.txt");
                break;
            case R.id.action_part_2:
                title.setText(getString(R.string.part_two));
                create("dic-2.txt");
                break;
            case R.id.action_part_3:
                title.setText(getString(R.string.part_three));
                create("dic-3.txt");
                break;
            case R.id.action_my_card:
                title.setText(getString(R.string.my_card));
                readDatabase();
                flingContainer.getTopCardListener().selectRight();
            default:
                return true;
        }
        flingContainer.getTopCardListener().selectRight();
        adapter.updateList(wordList);

        return true;
    }

    private String readJson(String fileName) throws IOException {
        InputStream stream = getAssets().open(fileName);
        int size = stream.available();
        byte[] buffer = new byte[size];
        stream.read(buffer);
        stream.close();
        String text = new String(buffer);
        return text;
    }

    private void create(String fileName) {

        try {
            json = readJson(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        words = gson.fromJson(json, Words.class);

        wordList.clear();
        if (adapter != null) {
            adapter.clearAdapter();
        }
        for (Word word : words.words) {
            wordList.add(word);
        }
    }

    private void readDatabase() {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
        wordList.clear();
        if (adapter != null) {
            adapter.clearAdapter();
        }
        wordList.addAll(dbHelper.getWords());
        Log.d("","");
    }

    @Override
    public void onRightButtonPressed() {
        flingContainer.getTopCardListener().selectRight();
    }

    @Override
    public void onLeftButtonPressed() {
        flingContainer.getTopCardListener().selectLeft();
    }
}
