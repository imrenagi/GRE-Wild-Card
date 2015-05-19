package com.imrenagi.greflashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.imrenagi.greflashcard.adapter.FlashcardAdapter;
import com.imrenagi.greflashcard.model.Word;
import com.imrenagi.greflashcard.model.Words;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.rey.material.widget.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class WordsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton button;
    private SwipeFlingAdapterView flingContainer;
    private FlashcardAdapter adapter;

    private Gson gson = new Gson();
    private Words words;
    private String json = null;
    private List<Word> wordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        button = (FloatingActionButton) findViewById(R.id.edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WordsActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
//        Word in = new Word();
//        in.name = "hello";
//        in.meaning = "pleasure";
//        dbHelper.addWords(in);
//        List<Word> list = dbHelper.getWords();

        create("dic-1.txt");

        Log.d("", "");

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        adapter = new FlashcardAdapter(this, R.layout.flashcard, wordList);

        flingContainer.setAdapter(adapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                if (wordList.size() != 0) {
                    wordList.remove(0);
//                    adapter.updateList(wordList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Toast.makeText(WordsActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(WordsActivity.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
//                Toast.makeText(WordsActivity.this, "Empty!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScroll(float v) {

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(WordsActivity.this, "Click!", Toast.LENGTH_SHORT).show();
            }
        });

//        Button buttonRight = (Button) findViewById(R.id.right_button);
//        buttonRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                flingContainer.getTopCardListener().selectRight();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_words, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        flingContainer.getTopCardListener().selectRight();

        switch (id) {
            case R.id.action_part_1:
                create("dic-1.txt");
                break;
            case R.id.action_part_2:
                create("dic-2.txt");
                break;
            case R.id.action_part_3:
                break;
            case R.id.action_part_4:
                break;
            case R.id.action_part_5:
                break;
            case R.id.action_part_6:
                break;
        }

        adapter.updateList(wordList);

        return super.onOptionsItemSelected(item);
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
}
