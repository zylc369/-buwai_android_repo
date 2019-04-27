package buwai.android.demo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import buwai.android.demo.R;
import buwai.android.demo.ui.map.MapNavActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnNavMap;
    private Button btnNavDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnNavMap = findViewById(R.id.btnNavMap);
        this.btnNavDialog = findViewById(R.id.btnNavDialog);

        btnNavMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapNavActivity.class);
                startActivity(intent);
            }
        });
        btnNavDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
