package tp1.inf8405.match3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class PlayActivity extends AppCompatActivity {

    private static final int NUMBER_OF_LEVELS = 4;

    public int levelNumber = 1;
    public static int levelToUnlock = 1; //this variable will be used to determine what is the latest unlocked level

    int nbRows;
    int nbCols;
    int objective;
    int nbRemainingShots;

    int score = 0;

    int[] colorTable = null;

    TextView objectiveView;
    TextView nbRemainingShotsView;
    TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Bundle extras = getIntent().getExtras();
        levelNumber = extras.getInt("level_number");
        initLevelPresets(levelNumber);

        Toast.makeText(this.getApplicationContext(), "Niveau " + levelNumber, Toast.LENGTH_SHORT).show();

    }

    public int getNbRows() {
        return nbRows;
    }

    public int getNbCols() {
        return nbCols;
    }

    public int[] getColorTable() {
        return colorTable;
    }

    public int getScore() {
        return score;
    }

    public int getNbRemainingShots() {
        return nbRemainingShots;
    }

    public int getObjective() {
        return objective;
    }

    public void decrementNbRemainingShots() {
        nbRemainingShots--;
    }


    //calcule la quantité de points acquis à partir du "match" en utilisant le nombre de cercle dans cet ensemble
    //et le nombre du combo (si c'est un mouvement, donc pas de combo, le premier combo, le deuxième, le troisième, etc.)
    public void computePoints(int numCircles, int numCombo) {
        int addedScore = 0;
        if (numCircles == 3) {
            addedScore += 100;
            //addedScore += 1000;
        } else if (numCircles == 4) {
            addedScore += 200;
        } else if (numCircles >= 5) {
            addedScore += 300;
        }

        score += (addedScore * (numCombo + 1)); //le 1 ajouté est pour faire en sorte que si ce n'est pas un combo (numCombo = 0)
                                                //le score ajouté ne sera pas 0 et ne sera pas modifié par la variable numCombo.
                                                //Si c'est un combo, le facteur sera ainsi à la valeur souhaitée pour la multiplication.
    }


    public void displayUpdatedStats() {
        nbRemainingShotsView = (TextView) findViewById(R.id.remainingShotsView);
        nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

        scoreView = (TextView) findViewById(R.id.scoreView);
        scoreView.setText("Score: " + score);
    }

    //chargement des presets du niveau actuel
    protected void initLevelPresets(int levelNumber) {
        switch (levelNumber) {
            case 1:
                nbRows = getResources().getInteger(R.integer.numRowsL1);
                nbCols = getResources().getInteger(R.integer.numColsL1);
                objective = getResources().getInteger(R.integer.objectiveL1);
                nbRemainingShots = getResources().getInteger(R.integer.nbMovesL1);
                score = 0;

                colorTable = new int[nbRows * nbCols];
                colorTable = getResources().getIntArray(R.array.colorGridL1);

                objectiveView = (TextView) findViewById(R.id.objectifView);
                objectiveView.setText("Objectif: " + objective);

                nbRemainingShotsView = (TextView) findViewById(R.id.remainingShotsView);
                nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

                scoreView = (TextView) findViewById(R.id.scoreView);
                scoreView.setText("Score: " + score);

                break;

            case 2:
                nbRows = getResources().getInteger(R.integer.numRowsL2);
                nbCols = getResources().getInteger(R.integer.numColsL2);
                objective = getResources().getInteger(R.integer.objectiveL2);
                nbRemainingShots = getResources().getInteger(R.integer.nbMovesL2);
                score = 0;

                colorTable = new int[nbRows * nbCols];
                colorTable = getResources().getIntArray(R.array.colorGridL2);

                objectiveView = (TextView) findViewById(R.id.objectifView);
                objectiveView.setText("Objectif: " + objective);

                nbRemainingShotsView = (TextView) findViewById(R.id.remainingShotsView);
                nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

                scoreView = (TextView) findViewById(R.id.scoreView);
                scoreView.setText("Score: " + score);

                break;

            case 3:
                nbRows = getResources().getInteger(R.integer.numRowsL3);
                nbCols = getResources().getInteger(R.integer.numColsL3);
                objective = getResources().getInteger(R.integer.objectiveL3);
                nbRemainingShots = getResources().getInteger(R.integer.nbMovesL3);
                score = 0;

                colorTable = new int[nbRows * nbCols];
                colorTable = getResources().getIntArray(R.array.colorGridL3);

                objectiveView = (TextView) findViewById(R.id.objectifView);
                objectiveView.setText("Objectif: " + objective);

                nbRemainingShotsView = (TextView) findViewById(R.id.remainingShotsView);
                nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

                scoreView = (TextView) findViewById(R.id.scoreView);
                scoreView.setText("Score: " + score);

                break;

            case 4:
                nbRows = getResources().getInteger(R.integer.numRowsL4);
                nbCols = getResources().getInteger(R.integer.numColsL4);
                objective = getResources().getInteger(R.integer.objectiveL4);
                nbRemainingShots = getResources().getInteger(R.integer.nbMovesL4);
                score = 0;

                colorTable = new int[nbRows * nbCols];
                colorTable = getResources().getIntArray(R.array.colorGridL4);

                objectiveView = (TextView) findViewById(R.id.objectifView);
                objectiveView.setText("Objectif: " + objective);

                nbRemainingShotsView = (TextView) findViewById(R.id.remainingShotsView);
                nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

                scoreView = (TextView) findViewById(R.id.scoreView);
                scoreView.setText("Score: " + score);

                break;
        }
    }

    protected void gameOver() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Désolé, vous avez perdu... Voulez-vous réessayer?");

        alertDialogBuilder.setPositiveButton("Réessayer",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = getIntent();
                    intent.putExtra("level_number", levelNumber);
                    finish();
                    startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Non",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Intent intent = new Intent(getApplicationContext(), ChooseLevelActivity.class);
                        finish();
                        //startActivity(intent);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    protected void victory() {

        //si le niveau victorieux actuel est un niveau en-dessous du prochain niveau a debloquer,
        //ce dernier sera debloqué
        if((levelToUnlock + 1) - levelNumber == 1){
            levelToUnlock++;
        }

        //incrementer pour passer au prochain niveau si l'utilisateur le desire
        levelNumber++;

        //verification si le niveau actuel est le dernier niveau du jeu
        if (levelNumber <= NUMBER_OF_LEVELS) {
            AlertDialog.Builder winLevelDialog = new AlertDialog.Builder(this);
            winLevelDialog.setMessage("Vous avez gagné! Voulez-vous passer au niveau suivant?");

            winLevelDialog.setPositiveButton("Prochain Niveau",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = getIntent();
                            intent.putExtra("level_number", levelNumber);
                            finish();
                            startActivity(intent);
                        }
                    });

            winLevelDialog.setNegativeButton("Non",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Intent intent = new Intent(getApplicationContext(), ChooseLevelActivity.class);
                            finish();
                            //startActivity(intent);
                        }
                    });

            AlertDialog alertDialog = winLevelDialog.create();
            alertDialog.show();
        } else {
            AlertDialog.Builder winGameDialog = new AlertDialog.Builder(this);
            winGameDialog.setMessage("Félicitations, vous avez complété le jeu!");

            winGameDialog.setPositiveButton("Je sais, je suis un Dieu",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            levelToUnlock = NUMBER_OF_LEVELS;
                            finish();
                        }
                    });

            AlertDialog alert = winGameDialog.create();
            alert.show();
        }
    }

    public void clickResetLevel(View view)
    {
        Intent intent = getIntent();
        intent.putExtra("level_number", levelNumber);
        finish();
        startActivity(intent);
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
}