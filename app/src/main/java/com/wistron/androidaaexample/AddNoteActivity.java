package com.wistron.androidaaexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {
    public static final String  EXTRA_TITLE =
            "com.wistron.androidaaexample.EXTRA_TITLE";
    public static final String EXTRA_DESC =
            "com.wistron.androidaaexample.EXTRA_DESC";
    public static final String EXTRA_PRIORITY =
            "com.wistron.androidaaexample.EXTRA_PRIORITY";

    private EditText et_title;
    private EditText et_desc;
    private NumberPicker np_priority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        et_title = findViewById(R.id.et_title);
        et_desc = findViewById(R.id.et_desc);
        np_priority = findViewById(R.id.np_priority);

        np_priority.setMinValue(1);
        np_priority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    private void saveNote(){
        String title = et_title.getText().toString();
        String desc = et_desc.getText().toString();
        int priority = np_priority.getValue();

        if(title.trim().isEmpty() || desc.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESC, desc);
        intent.putExtra(EXTRA_PRIORITY, priority);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
