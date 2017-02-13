package tp1.inf8405.match3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Blanche on 2/6/2017.
 */

public class PlayActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Bundle extras = getIntent().getExtras();
        levelNumber = extras.getInt("level_number");
        initLevelPresets(levelNumber);

        Toast.makeText(this.getApplicationContext(), "Niveau " + levelNumber, Toast.LENGTH_SHORT).show();

    }

    public int getNbRows()
    {
        return nbRows;
    }

    public int getNbCols()
    {
        return nbCols;
    }

    public int[] getColorTable()
    {
        return colorTable;
    }

    public int getScore()
    {
        return score;
    }

    public int getNbRemainingShots()
    {
        return nbRemainingShots;
    }

    public int getObjective()
    {
        return objective;
    }

    public void decrementNbRemainingShots()
    {
        nbRemainingShots--;
    }


    //computes the quantity of points made from a match using the number of circles that are matched together
    //and the number of the combo (if its the first move the number is 0, if its the first combo
    //the number is 1, second combo: 2, and so on)
    public void computePoints(int numCircles, int numCombo)
    {
        int addedScore = 0;
        if(numCircles == 3)
        {
            addedScore += 100;
        }
        else if(numCircles == 4)
        {
            addedScore += 200;
        }
        else if(numCircles == 5)
        {
            addedScore += 300;
        }

        score += (addedScore * (numCombo + 1)); //the 1 added to numCombo is necessary so that the added score
                                                //is mutiplied by 2 if it's the first combo (numCombo = 1), by 3 if it's the second combo (numCombo =2)
    }


    public void displayUpdatedStats()
    {
        nbRemainingShotsView = (TextView)findViewById(R.id.remainingShotsView);
        nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

        scoreView = (TextView)findViewById(R.id.scoreView);
        scoreView.setText("Score: " + score);
    }

    protected void initLevelPresets(int levelNumber)
    {
        switch (levelNumber)
        {
            case 1 :
                nbRows = getResources().getInteger(R.integer.numRowsL1);
                nbCols = getResources().getInteger(R.integer.numColsL1);
                objective = getResources().getInteger(R.integer.objectiveL1);
                nbRemainingShots = getResources().getInteger(R.integer.nbMovesL1);

                colorTable = new int[nbRows*nbCols];
                colorTable = getResources().getIntArray(R.array.colorGridL1);

                objectiveView = (TextView)findViewById(R.id.objectifView);
                objectiveView.setText("Objectif: " + objective);

                nbRemainingShotsView = (TextView)findViewById(R.id.remainingShotsView);
                nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

                scoreView = (TextView)findViewById(R.id.scoreView);
                scoreView.setText("Score: " + score);

                break;

            case 2 :
                nbRows = getResources().getInteger(R.integer.numRowsL2);
                nbCols = getResources().getInteger(R.integer.numColsL2);
                objective = getResources().getInteger(R.integer.objectiveL2);
                nbRemainingShots = getResources().getInteger(R.integer.nbMovesL2);

                colorTable = new int[nbRows*nbCols];
                colorTable = getResources().getIntArray(R.array.colorGridL2);

                objectiveView = (TextView)findViewById(R.id.objectifView);
                objectiveView.setText("Objectif: " + objective);

                nbRemainingShotsView = (TextView)findViewById(R.id.remainingShotsView);
                nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

                scoreView = (TextView)findViewById(R.id.scoreView);
                scoreView.setText("Score: " + score);

                break;

            case 3 :
                nbRows = getResources().getInteger(R.integer.numRowsL3);
                nbCols = getResources().getInteger(R.integer.numColsL3);
                objective = getResources().getInteger(R.integer.objectiveL3);
                nbRemainingShots = getResources().getInteger(R.integer.nbMovesL3);

                colorTable = new int[nbRows*nbCols];
                colorTable = getResources().getIntArray(R.array.colorGridL3);

                objectiveView = (TextView)findViewById(R.id.objectifView);
                objectiveView.setText("Objectif: " + objective);

                nbRemainingShotsView = (TextView)findViewById(R.id.remainingShotsView);
                nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

                scoreView = (TextView)findViewById(R.id.scoreView);
                scoreView.setText("Score: " + score);

                break;

            case 4 :
                nbRows = getResources().getInteger(R.integer.numRowsL4);
                nbCols = getResources().getInteger(R.integer.numColsL4);
                objective = getResources().getInteger(R.integer.objectiveL4);
                nbRemainingShots = getResources().getInteger(R.integer.nbMovesL4);

                colorTable = new int[nbRows*nbCols];
                colorTable = getResources().getIntArray(R.array.colorGridL4);

                objectiveView = (TextView)findViewById(R.id.objectifView);
                objectiveView.setText("Objectif: " + objective);

                nbRemainingShotsView = (TextView)findViewById(R.id.remainingShotsView);
                nbRemainingShotsView.setText("Nombre de coups restants: " + nbRemainingShots);

                scoreView = (TextView)findViewById(R.id.scoreView);
                scoreView.setText("Score: " + score);

                break;
        }
    }
}

