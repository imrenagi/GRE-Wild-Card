package com.imrenagi.greflashcard;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
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
import java.util.Collections;
import java.util.Random;


public class WordsActivity extends AppCompatActivity implements FlashCardButtonListener {

    private Toolbar toolbar;
    private ButtonFloat button;
    private SwipeFlingAdapterView flingContainer;
    private FlashcardAdapter adapter;
    private TextView title;
    private TextView counter;
    private IconRoundCornerProgressBar progressBar;
    private boolean updateCounter = true;

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

        counter = (TextView) findViewById(R.id.textCounter);
        progressBar = (IconRoundCornerProgressBar)findViewById(R.id.correct_progress);


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
                if (isNewUpdate) {
                    isNewUpdate = false;
                    adapter.notifyDataSetChanged();
                    return;
                }
                wordList.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                wordList.add((Word)dataObject);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                if (updateCounter) {
                    int progress = (int)progressBar.getProgress();
                    progress++;
                    progressBar.setProgress((float) progress);
                }else {
                    updateCounter = true;
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
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
            case R.id.action_part_4:
                title.setText(getString(R.string.part_four));
                create("dic-4.txt");
                break;
            case R.id.action_part_5:
                title.setText(getString(R.string.part_five));
                create("dic-5.txt");
                break;
            case R.id.action_part_6:
                title.setText(getString(R.string.part_six));
                create("dic-6.txt");
                break;
            case R.id.action_my_card:
                title.setText(getString(R.string.my_card));
                readDatabase();
                updateCounter = false;
                flingContainer.getTopCardListener().selectRight();
            default:
                return true;
        }
        updateCounter = false;
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

        long seed = System.nanoTime();
        Collections.shuffle(wordList, new Random(seed));
        updateProgressBar(0, wordList.size());
    }


    private void readDatabase() {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
        wordList.clear();
        if (adapter != null) {
            adapter.clearAdapter();
        }
        wordList.addAll(dbHelper.getWords());
        updateProgressBar(0, wordList.size());
    }

    private void updateProgressBar(int progress, int max) {
        progressBar.setProgress((float) progress);
        progressBar.setMax((float)max);
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
