package com.imrenagi.greflashcard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;


public class EditorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText nameEditText;
    private Spinner typeSpinner;
    private EditText meaningEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        nameEditText = (EditText) findViewById(R.id.name);
        typeSpinner = (Spinner) findViewById(R.id.type);
        meaningEditText = (EditText) findViewById(R.id.meaning);

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_finish) {

            if (!nameEditText.getText().toString().isEmpty()
                    && !meaningEditText.getText().toString().isEmpty()) {
                //todo save db here
                showSuccessDialog();
            } else {
                showFailedDialog();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSuccessDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.card_saved));
        alertDialogBuilder
                .setMessage(getString(R.string.add_card_success_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        nameEditText.setText("");
                        meaningEditText.setText("");
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showFailedDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.failed_title));
        alertDialogBuilder
                .setMessage(getString(R.string.add_card_failed_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.oke), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
