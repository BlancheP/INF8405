package tp1.inf8405.match3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

        Bundle extras = getIntent().getExtras();
        int levelToUnlock = extras.getInt("level_to_unlock");
        unlockToLatestLevel(levelToUnlock);
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
        finish();
    }

    public void unlockToLatestLevel(int latestLevel){

        Button levelButton;

        switch(latestLevel) {
            case 1:
                //Toast.makeText(this.getApplicationContext(), "Niveau 1 unlocked", Toast.LENGTH_SHORT).show();
                break;

            case 2:
                //Toast.makeText(this.getApplicationContext(), "Niveau 2 unlocked", Toast.LENGTH_SHORT).show();
                levelButton = (Button) findViewById(R.id.level2_button);
                levelButton.setEnabled(true);
                break;

            case 3:
                //Toast.makeText(this.getApplicationContext(), "Niveau 3 unlocked", Toast.LENGTH_SHORT).show();
                levelButton = (Button) findViewById(R.id.level2_button);
                levelButton.setEnabled(true);
                levelButton = (Button) findViewById(R.id.level3_button);
                levelButton.setEnabled(true);
                break;

            case 4:
                //Toast.makeText(this.getApplicationContext(), "Niveau 4 unlocked", Toast.LENGTH_SHORT).show();
                levelButton = (Button) findViewById(R.id.level2_button);
                levelButton.setEnabled(true);
                levelButton = (Button) findViewById(R.id.level3_button);
                levelButton.setEnabled(true);
                levelButton = (Button) findViewById(R.id.level4_button);
                levelButton.setEnabled(true);
                break;
        }

    }

    public void clickQuit(View view){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);

        quitDialog.setMessage("Voulez-vous vraiment quitter l'application?");

        quitDialog.setPositiveButton("Oui",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        System.exit(0);
                    }
                });

        quitDialog.setNegativeButton("Non", null);

        AlertDialog alertDialog = quitDialog.create();
        alertDialog.show();
    }
}
