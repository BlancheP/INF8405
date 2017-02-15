package tp1.inf8405.match3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Blanche on 2/6/2017.
 */

public class PlayActivity extends AppCompatActivity {

    public static final int NUMBER_OF_LEVELS = 4;

    int levelNumber;

    int nbRows;
    int nbCols;
    int objective;
    int nbRemainingShots;

    int score = 0;

    int[] colorTable = null;

    TextView objectiveView;
    TextView nbRemainingShotsView;
    TextView scoreView;

    Button nextLevelButton;

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

    public int getLevelNumber() {
        return levelNumber;
    }

    public void decrementNbRemainingShots() {
        nbRemainingShots--;
    }


    //computes the quantity of points made from a match using the number of circles that are matched together
    //and the number of the combo (if its the first move the number is 0, if its the first combo
    //the number is 1, second combo: 2, and so on)
    public void computePoints(int numCircles, int numCombo) {
        int addedScore = 0;
        if (numCircles == 3) {
            addedScore += 100;
            addedScore += 1000;
        } else if (numCircles == 4) {
            addedScore += 200;
        } else if (numCircles >= 5) {
            addedScore += 300;
        }

        score += (addedScore * (numCombo + 1)); //the 1 added to numCombo is necessary so that the added score
        //is mutiplied by 2 if it's the first combo (numCombo = 1), by 3 if it's the second combo (numCombo =2)
    }


    public void displayUpdatedStats() {
        nbRemainingShotsView = (TextView) findViewById(R.id.remainingShotsView);
        nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

        scoreView = (TextView) findViewById(R.id.scoreView);
        scoreView.setText("Score: " + score);
    }

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
        levelNumber++;

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
                            //Intent intent = new Intent(getApplicationContext(), ChooseLevelActivity.class);
                            finish();
                            //startActivity(intent);
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
                        finish();
                        System.exit(0);
                    }
                });

        quitDialog.setNegativeButton("Non", null);

        AlertDialog alertDialog = quitDialog.create();
        alertDialog.show();
    }
}