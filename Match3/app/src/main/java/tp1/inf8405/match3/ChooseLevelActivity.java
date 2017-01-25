package tp1.inf8405.match3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
    }

    public void clickLevel(View view){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }
}
