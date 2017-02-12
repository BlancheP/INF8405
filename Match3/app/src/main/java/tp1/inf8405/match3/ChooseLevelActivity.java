package tp1.inf8405.match3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
    }

    public void clickLevel(View view){
        Intent intent = new Intent(this, PlayActivity.class);

        Button b = (Button)view;
        String buttonText = b.getText().toString();

        switch(buttonText) {
            case "Niveau 1" :
                //Toast.makeText(this.getApplicationContext(), "Niveau 1", Toast.LENGTH_SHORT).show();
                intent.putExtra("level_number", 1);
                break;

            case "Niveau 2" :
                //Toast.makeText(this.getApplicationContext(), "Niveau 2", Toast.LENGTH_SHORT).show();
                intent.putExtra("level_number", 2);
                break;

            case "Niveau 3" :
                //Toast.makeText(this.getApplicationContext(), "Niveau 3", Toast.LENGTH_SHORT).show();
                intent.putExtra("level_number", 3);
                break;

            case "Niveau 4" :
                //Toast.makeText(this.getApplicationContext(), "Niveau 4", Toast.LENGTH_SHORT).show();
                intent.putExtra("level_number", 4);
                break;

            default : // Optional
                Toast.makeText(this.getApplicationContext(), "Niveau invalide", Toast.LENGTH_SHORT).show();
        }

        startActivity(intent);
    }
}
