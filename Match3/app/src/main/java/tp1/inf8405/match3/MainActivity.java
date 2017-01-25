package tp1.inf8405.match3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onQuit(View view){
        finish();
        System.exit(1);
    }

    public void clickRules(View view){
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }

    public void clickPlay(View view){
        Intent intent = new Intent(this, ChooseLevelActivity.class);
        startActivity(intent);
    }
}
