package buwai.android.demo.ui.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import buwai.android.demo.R;

public class MapNavActivity extends AppCompatActivity {

    private Button btnMapNavMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_nav);

        this.btnMapNavMarker = findViewById(R.id.btnMapNavMarker);

        btnMapNavMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapMarkerActivity.class);
                startActivity(intent);
            }
        });
    }
}
