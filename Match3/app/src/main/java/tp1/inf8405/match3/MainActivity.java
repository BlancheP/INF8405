package tp1.inf8405.match3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickQuit(View view){
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);

        quitDialog.setMessage("Voulez-vous vraiment quitter l'application?");

        quitDialog.setPositiveButton("Oui",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finishAffinity();
                    }
                });

        quitDialog.setNegativeButton("Non", null);

        AlertDialog alertDialog = quitDialog.create();
        alertDialog.show();
    }

    public void clickRules(View view){
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }

    public void clickPlay(View view){
        Intent intent = new Intent(this, ChooseLevelActivity.class);
        intent.putExtra("level_to_unlock", PlayActivity.levelToUnlock);
        startActivity(intent);
    }
}
