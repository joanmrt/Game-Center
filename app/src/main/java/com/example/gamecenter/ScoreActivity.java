package com.example.gamecenter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    User currentUser;
    UserOpenHelper userOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        currentUser = (User) getIntent().getSerializableExtra("user");
        userOpenHelper = new UserOpenHelper(this);

        conseguirRanking();
    }

    private void conseguirRanking(){
        ArrayList<Score> arrayList2048 = new ArrayList<Score>();
        ArrayList<Score> arrayListNonogram = new ArrayList<Score>();
        LinearLayout linearLayout2048 = findViewById(R.id.ranking_2048);
        LinearLayout linearLayoutNonogram = findViewById(R.id.ranking_nonogram);

        arrayList2048 = userOpenHelper.getScores2048();
        arrayListNonogram = userOpenHelper.getScoresNonogram();

        for (int i = 0; i < arrayList2048.size(); i++){
            //Crear textView y añadirlo al linear layout
            TextView textView = createScoreTextView(this, i + 1, arrayList2048.get(i));
            linearLayout2048.addView(textView);
        }

        for (int i = 0; i < arrayListNonogram.size(); i++){
            //Crear textView y añadirlo al linear layout
            TextView textView = createScoreTextView(this, i + 1, arrayListNonogram.get(i));
            linearLayoutNonogram.addView(textView);
        }

    }
    private TextView createScoreTextView(Context context, int rank, Score score) {
        TextView textView = new TextView(context);
        textView.setText(rank + ". " + score.getName() + " - " + score.getPoints());
        textView.setTextSize(18);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(10, 5, 10, 5);
        Typeface customFont = ResourcesCompat.getFont(this, R.font.allerta);
        textView.setTypeface(customFont);
        return textView;
    }
}