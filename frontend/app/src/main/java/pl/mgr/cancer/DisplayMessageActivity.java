package pl.mgr.cancer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class DisplayMessageActivity extends AppCompatActivity {

    private final Map<String, String> idxToName = new HashMap<String, String>() {{
        put("nv", "znamię melanocytowe");
        put("mel", "czerniaka");
        put("bkl", "łagodną zmianę przypominającą rogowacenie");
        put("bcc", "raka podstawnokomórkowego");
        put("akiec", "rogowacenie słoneczne");
        put("vasc", "zmiany naczyniowe");
        put("df", "włokniaka");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String result = "Twoja zmiana przypomina " + idxToName.get(message);

        TextView textView = findViewById(R.id.result);
        textView.setText(result);
    }
}