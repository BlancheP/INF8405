package tp1.inf8405.match3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Blanche on 2/6/2017.
 */

public class GridView extends View
{
    //VARIABLES FOR GRID INITIATION
    Paint paint = null;
    ArrayList[][] grid = null;
    float width = 0;
    float height = 0;
    float currentX = 0;
    float currentY = 0;
    List<Integer> colors = null;


    //VARIABLES FOR SWIPE MANAGEMENT
    float MIN_DELTA; //minimum distance in order for a swipe to take effect
    float initialPositionX = 0, initialPositionY = 0, finalPositionX, finalPositionY;

    List<Integer> beginCoords = null;
    List<Integer> endCoords = null;
    BitSet statusBegin = null;
    BitSet statusEnd = null;

    List<List<List<Integer>>> matchCirclesVertical = new ArrayList<>();
    List<List<List<Integer>>> matchCirclesHorizontal = new ArrayList<>();

    List<List<Integer>> newCircles = new ArrayList<>();

    //Variable for combo management
    int numCombo = 0;

    public GridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint = new Paint();
        colors = new ArrayList<>(Arrays.asList(
                Color.rgb(255, 0, 0), //red
                Color.rgb(255, 255, 0), //yellow
                Color.rgb(0, 255, 0), //green
                Color.rgb(85, 26, 139), //purple
                Color.rgb(255, 127, 0), //orange
                Color.rgb(2, 102, 200)
        ));
        matchCirclesHorizontal.add(new ArrayList<List<Integer>>());
        matchCirclesHorizontal.add(new ArrayList<List<Integer>>());
        matchCirclesVertical.add(new ArrayList<List<Integer>>());
        matchCirclesVertical.add(new ArrayList<List<Integer>>());
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float radius = circleRadius(((PlayActivity) getContext()).getNbCols(),
                                    ((PlayActivity) getContext()).getNbRows());

        int[] colorTable = ((PlayActivity) getContext()).getColorTable();

        int nbRows = ((PlayActivity) getContext()).getNbRows();
        int nbCols = ((PlayActivity) getContext()).getNbCols();

        if (grid == null)
        {
            initGrid(nbRows, nbCols, getWidth(), getHeight(), colorTable);
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        for (int i = 0; i < nbRows; i ++)
        {
            for (int j = 0; j < nbCols; j++)
            {
                ArrayList<Float> coords = getCoords(i,j);
                /*if (coords.size() < 3)
                {
                    grid[i][j].add((float)getRandomColor());
                    coords = getCoords(i,j);
                }*/
                paint.setColor(Math.round(coords.get(2)));
                canvas.drawCircle(coords.get(0), coords.get(1), radius, paint);
            }
        }
    }

    protected void match()
    {
        while (newCircles.size() > 0)
        {
            statusBegin = new BitSet(2);
            statusBegin = findMatch(newCircles.get(0), 0);
            if (!statusBegin.equals(new BitSet(2))) // There is a match
            {
                numCombo++;
                doMatch(statusBegin, 0);
                updateGrid();
                invalidate();
            }
            newCircles.remove(0);
        }

        /*
        if(((PlayActivity) getContext()).getNbRemainingShots() == 0 &&
                (((PlayActivity) getContext()).getScore() < ((PlayActivity) getContext()).getObjective()))
        {
            ((PlayActivity)getContext()).gameOver();
        }

        else if((((PlayActivity) getContext()).getScore() >= ((PlayActivity) getContext()).getObjective()))
        {
            ((PlayActivity) getContext()).victory();
        }
        */
    }

    protected void updateGrid()
    {
        for (int i = 0; i < grid.length; i ++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                ArrayList<Float> coords = getCoords(i,j);
                if (coords.size() < 3)
                {
                    grid[i][j].add((float)getRandomColor());
                }
            }
        }
    }

    protected float circleRadius(int nbCols, int nbRows)
    {
        if(nbCols >= nbRows)
        {
            return getWidth()/nbCols/2;
        }
        else
        {
            return getHeight()/nbRows/2;
        }
    }

    protected void initGrid(int nbRows, int nbCols, int w, int h, int[] colorTable)
    {
        grid = new ArrayList[nbRows][nbCols];

        width = w/(float)nbCols;
        height = h/(float)nbRows;

        MIN_DELTA = width / 2;
        currentX = currentY = width / 2;

        for (int i = 0; i < nbRows; i++)
        {
            for (int j = 0; j < nbCols; j++)
            {
                grid[i][j] = new ArrayList<>();
            }
        }

        for (int i = 0; i < nbRows; i++)
        {
            for (int j = 0; j < nbCols; j++)
            {
                grid[i][j].add(currentX);
                grid[i][j].add(currentY);
                grid[i][j].add((float)colorTable[j + i * nbCols]);
                //grid[i][j].add((float)getRandomColor());
                currentX += width;
            }
            currentX = width / 2;

            if(nbRows > 6)
            {
                currentY += height;
            }
            else
            {
                currentY += width;
            }
        }
    }

    protected ArrayList<Float> getCoords(int i, int j)
    {
        return grid[i][j];
    }

    protected int getRandomColor()
    {
        return colors.get((int)(Math.random() * colors.size()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action)
        {
            case (MotionEvent.ACTION_DOWN) :

                initialPositionX = event.getX();
                initialPositionY = event.getY();
                beginCoords = findCircle(initialPositionX, initialPositionY);

                //Toast.makeText(this.getContext(), "Action was DOWN at: " + initialPositionX, Toast.LENGTH_SHORT).show ();

                return true;

            case (MotionEvent.ACTION_UP) :

                finalPositionX = event.getX();
                finalPositionY = event.getY();

                //Toast.makeText(this.getContext(), "Action was UP at : " + finalPositionX, Toast.LENGTH_SHORT).show ();

                if (beginCoords.size() < 2)
                    return true;
                
                if(finalPositionX - initialPositionX >= MIN_DELTA)
                {
                    //Toast.makeText(this.getContext(), "Swipped RIGHT", Toast.LENGTH_SHORT).show ();
                    endCoords = findNeighbor(beginCoords, 2);
                }

                else if (Math.abs(initialPositionX - finalPositionX) >= MIN_DELTA)
                {
                    //Toast.makeText(this.getContext(), "Swipped LEFT", Toast.LENGTH_SHORT).show ();
                    endCoords = findNeighbor(beginCoords, 3);
                }

                else if (finalPositionY - initialPositionY >= MIN_DELTA)
                {
                    //Toast.makeText(this.getContext(), "Swipped DOWN", Toast.LENGTH_SHORT).show ();
                    endCoords = findNeighbor(beginCoords, 1);
                }

                else if (Math.abs(initialPositionY - finalPositionY) >= MIN_DELTA)
                {
                    //Toast.makeText(this.getContext(), "Swipped UP", Toast.LENGTH_SHORT).show ();
                    endCoords = findNeighbor(beginCoords, 0);
                }
                else
                {
                    endCoords = beginCoords;
                }

                float oldColor1 = (float)grid[beginCoords.get(0)][beginCoords.get(1)].get(2), oldColor2 = (float)grid[endCoords.get(0)][endCoords.get(1)].get(2);
                grid[beginCoords.get(0)][beginCoords.get(1)].set(2, oldColor2);
                grid[endCoords.get(0)][endCoords.get(1)].set(2, oldColor1);

                statusBegin = new BitSet(2);
                statusEnd = new BitSet(2);

                statusBegin = findMatch(beginCoords, 0);
                statusEnd = findMatch(endCoords, 1);

                if (statusBegin.equals(new BitSet(2)) && statusEnd.equals(new BitSet(2)))
                {
                    grid[beginCoords.get(0)][beginCoords.get(1)].set(2, oldColor1);
                    grid[endCoords.get(0)][endCoords.get(1)].set(2, oldColor2);
                    Toast.makeText(this.getContext(), "Ceci est une action interdite!!", Toast.LENGTH_SHORT).show();

                    // Toast.makeText(this.getContext(), "Shots Remaining: " + ((PlayActivity) getContext()).getNbRemainingShots(), Toast.LENGTH_SHORT).show ();
                }
                else
                {
                    //numCombo is set to 0 because there was a move at that moment
                    numCombo = 0;
                    doMatch(statusBegin, 0);
                    doMatch(statusEnd, 1);

                    ((PlayActivity) getContext()).decrementNbRemainingShots();
                    ((PlayActivity) getContext()).displayUpdatedStats();
                    //Toast.makeText(this.getContext(), "Shots Remaining: " + ((PlayActivity) getContext()).getNbRemainingShots(), Toast.LENGTH_SHORT).show();

                }

                updateGrid();
                invalidate();
                match();

                if(((PlayActivity) getContext()).getNbRemainingShots() == 0 &&
                        (((PlayActivity) getContext()).getScore() < ((PlayActivity) getContext()).getObjective()))
                {
                    ((PlayActivity)getContext()).gameOver();
                }

                else if((((PlayActivity) getContext()).getScore() >= ((PlayActivity) getContext()).getObjective()))
                {
                    ((PlayActivity) getContext()).victory();
                }

                return true;

            default :
                return super.onTouchEvent(event);
        }

    }

    protected void doMatch(BitSet status, int index)
    {
        if (status.get(0) == true) //Vertical match
        {
            Collections.sort(matchCirclesVertical.get(index), new Comparator<List<Integer>>() {
                @Override
                public int compare(List<Integer> circle1, List<Integer> circle2) {
                    return circle2.get(0) > circle1.get(0) ? -1 : (circle2.get(0) < circle1.get(0)) ? 1 : 0;
                }
            });
            int col = matchCirclesVertical.get(index).get(0).get(1);
            for (int i = matchCirclesVertical.get(index).get(matchCirclesVertical.get(index).size() - 1).get(0); i >= 0; i--)
            {
                newCircles.add(Arrays.asList(i, col));
            }
            for (int i = 0; i < matchCirclesVertical.get(index).size(); i++)
            {
                bringCirclesDown(matchCirclesVertical.get(index).get(i));

                //((PlayActivity) getContext()).decrementNbRemainingShots();
                //Toast.makeText(this.getContext(), "Shots Remaining: " + ((PlayActivity) getContext()).getNbRemainingShots(), Toast.LENGTH_SHORT).show ();
            }
            ((PlayActivity) getContext()).computePoints(matchCirclesVertical.get(index).size(), numCombo);
        }
        if (status.get(1) == true) //Horizontal match
        {
            Collections.sort(matchCirclesHorizontal.get(index), new Comparator<List<Integer>>() {
                @Override
                public int compare(List<Integer> circle1, List<Integer> circle2) {
                    return circle2.get(1) > circle1.get(1) ? -1 : (circle2.get(1) < circle1.get(1)) ? 1 : 0;
                }
            });
            int row = matchCirclesHorizontal.get(index).get(0).get(0);
            int minCol = matchCirclesHorizontal.get(index).get(0).get(1);
            int maxCol = matchCirclesHorizontal.get(index).get(matchCirclesHorizontal.get(index).size() - 1).get(1);
            for (int i = 0; i <= row; i++)
            {
                for (int j = minCol; j <= maxCol; j++)
                {
                    newCircles.add(Arrays.asList(i, j));
                }
            }
            for (int i = 0; i < matchCirclesHorizontal.get(index).size(); i++)
            {
                bringCirclesDown(matchCirclesHorizontal.get(index).get(i));

                //((PlayActivity) getContext()).decrementNbRemainingShots();
                //Toast.makeText(this.getContext(), "Shots Remaining: " + ((PlayActivity) getContext()).getNbRemainingShots(), Toast.LENGTH_SHORT).show ();
            }
            ((PlayActivity) getContext()).computePoints(matchCirclesHorizontal.get(index).size(), numCombo);
        }

        ((PlayActivity) getContext()).displayUpdatedStats();
    }

    protected void bringCirclesDown(List<Integer> circle)
    {
        int col = circle.get(1);
        for (int i = circle.get(0); i > 0; i--)
        {
            if (grid[i-1][col].size() > 2)
                grid[i][col].set(2, grid[i - 1][col].get(2));
            else if (grid[i][col].size() > 2)
            {
                grid[i][col].remove(2);
            }

        }
        if (grid[0][col].size() > 2)
            grid[0][col].remove(2);
    }

    protected List<Integer> findCircle(float x, float y)
    {
        List<Integer> coords = new ArrayList<>();

        for (int i = 0; i < grid.length; i++)
        {
            int max = Float.compare(y, ((float)grid[i][0].get(1) + width/2));
            int min = Float.compare(y, ((float)grid[i][0].get(1) - width/2));
            if (max == 0 || min == 0 || (max < 0 && min > 0) ) {
                coords.add(i);
                break;
            }

        }

        for (int j = 0; j < grid[0].length; j++)
        {
            int max = Float.compare(x, ((float)grid[0][j].get(0) + width/2));
            int min = Float.compare(x, ((float)grid[0][j].get(0) - width/2));
            if (max == 0 || min == 0 || (max < 0 && min > 0) ) {
                coords.add(j);
                break;
            }
        }

        return coords;
    }

    protected BitSet findMatch(List<Integer> circle, int index)
    {
        BitSet status = new BitSet(2);
        matchCirclesHorizontal.get(index).clear();
        matchCirclesVertical.get(index).clear();
        matchCirclesHorizontal.get(index).add(circle);
        matchCirclesVertical.get(index).add(circle);

        //Vertical
        constructMatch(circle, 0, index);
        constructMatch(circle, 1, index);

        //Horizontal
        constructMatch(circle, 2, index);
        constructMatch(circle, 3, index);

        if (matchCirclesHorizontal.get(index).size() > 2)
        {
            //Toast.makeText(this.getContext(), "Horizontal : " + matchCirclesHorizontal.size(), Toast.LENGTH_SHORT).show();
            status.set(1);
        }
        if (matchCirclesVertical.get(index).size() > 2)
        {
            //Toast.makeText(this.getContext(), "Vertical : " + matchCirclesVertical.size(), Toast.LENGTH_SHORT).show();
            status.set(0);
        }
        return status;
    }

    //Checks the neighboring circle in the specified direction to see if it matches the color of the current one
    protected void constructMatch(List<Integer> circle, int direction, int index)
    {
        List<Integer> neighbor = findNeighbor(circle, direction);
        if (neighbor.size() != 0 && isThereAMatch(circle, neighbor))
        {
            if (direction == 0 || direction == 1)
                matchCirclesVertical.get(index).add(neighbor);
            else
                matchCirclesHorizontal.get(index).add(neighbor);
            constructMatch(neighbor, direction, index);
        }
    }

    protected List<Integer> findNeighbor(List<Integer> circle, int direction)
    {
        List<Integer> coords = new ArrayList<>();
        switch (direction){
            case 0 /*up*/: if (circle.get(0) == 0) break;
                coords.add(circle.get(0) - 1);
                coords.add(circle.get(1));
                break;
            case 1 /*down*/: if (circle.get(0) == grid.length - 1) break;
                coords.add(circle.get(0) + 1);
                coords.add(circle.get(1));
                break;
            case 2 /*right*/: if (circle.get(1) == grid[0].length - 1) break;
                coords.add(circle.get(0));
                coords.add(circle.get(1) + 1);
                break;
            case 3 /*left*/: if (circle.get(1) == 0) break;
                coords.add(circle.get(0));
                coords.add(circle.get(1) - 1);
                break;
            default:
                break;
        }
        return coords;
    }

    protected Boolean isThereAMatch(List<Integer> circle, List<Integer> neighbor)
    {
        return Math.abs((float)grid[circle.get(0)][circle.get(1)].get(2) - (float)grid[neighbor.get(0)][neighbor.get(1)].get(2)) < 0.000001;
    }

}
