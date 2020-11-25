package com.example.conference_infinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class ArticleActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        floatingActionButton = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        /* main line for getting menu in bottom app bar */
        setSupportActionBar(bottomAppBar);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View viewPos = findViewById(R.id.coordinator_layout);
                Snackbar snackbar = Snackbar.make(
                        viewPos,
                        "FAB Clicked",
                        Snackbar.LENGTH_LONG
                ).setAction("UNDO", null);
                snackbar.setAnchorView(floatingActionButton);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.show();

            }
        });

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getClass().getSimpleName());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.article_bottom_app_bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_bar_translate:
                Toast.makeText(this, "Translate", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_bar_chat:
                Toast.makeText(this, "Comments", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_bar_time:
                Toast.makeText(this, "Time", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_bar_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}