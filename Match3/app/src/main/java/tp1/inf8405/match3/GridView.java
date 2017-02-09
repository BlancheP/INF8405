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
    float currentX = 0;
    float currentY = 0;
    List<Integer> colors = null;


    //VARIABLES FOR SWIPE MANAGEMENT
    float MIN_DELTA; //minimum distance in order for a swipe to take effect

    List<Integer> beginCoords = null;
    List<Integer> endCoords = null;

    List<List<Integer>> matchCirclesVertical = new ArrayList<>();
    List<List<Integer>> matchCirclesHorizontal = new ArrayList<>();

    public GridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint = new Paint();
        colors = new ArrayList<>(Arrays.asList(
                Color.RED,
                Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.rgb(255,165,0),
                Color.rgb(148,0,211)
        ));
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float radius = circleRadius(8);
        initGrid(5,8,getWidth());
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        for (int i = 0; i < 5; i ++)
        {
            for (int j = 0; j < 8; j++)
            {
                ArrayList<Float> coords = getCoords(i,j);
                paint.setColor(Math.round(coords.get(2)));
                canvas.drawCircle(coords.get(0), coords.get(1), radius, paint);
            }
        }
    }

    protected float circleRadius(int nbCols)
    {
        return getWidth()/nbCols/2;
    }

    protected void initGrid(int nbRows, int nbCols, int w)
    {
        grid = new ArrayList[nbRows][nbCols];
        width = w/(float)nbCols;
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
                grid[i][j].add((float)getRandomColor());
                currentX += width;
            }
            currentX = width / 2;
            currentY += width;
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
        float initialPositionX = 0, initialPositionY = 0, finalPositionX, finalPositionY;

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
                endCoords = findCircle(finalPositionX, finalPositionY);

                //Toast.makeText(this.getContext(), "Action was UP at : " + finalPositionX, Toast.LENGTH_SHORT).show ();

                if(finalPositionX - initialPositionX >= MIN_DELTA)
                {
                    Toast.makeText(this.getContext(), "Swipped RIGHT", Toast.LENGTH_SHORT).show ();
                }

                else if (Math.abs(initialPositionX - finalPositionX) >= MIN_DELTA)
                {
                    Toast.makeText(this.getContext(), "Swipped LEFT", Toast.LENGTH_SHORT).show ();
                }

                else if (finalPositionY - initialPositionY >= MIN_DELTA)
                {
                    Toast.makeText(this.getContext(), "Swipped DOWN", Toast.LENGTH_SHORT).show ();
                }

                else if (Math.abs(initialPositionY - finalPositionY) >= MIN_DELTA)
                {
                    Toast.makeText(this.getContext(), "Swipped UP", Toast.LENGTH_SHORT).show ();
                }

                float oldColor1 = (float)grid[beginCoords.get(0)][beginCoords.get(1)].get(2), oldColor2 = (float)grid[endCoords.get(0)][endCoords.get(1)].get(2);
                grid[beginCoords.get(0)][beginCoords.get(1)].set(2, oldColor2);
                grid[endCoords.get(0)][endCoords.get(1)].set(2, oldColor1);

                match(beginCoords);
                match(endCoords);

                return true;

            default :
                return super.onTouchEvent(event);
        }

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

    protected int match(List<Integer> circle)
    {
        matchCirclesHorizontal.clear();
        matchCirclesVertical.clear();
        matchCirclesHorizontal.add(circle);
        matchCirclesVertical.add(circle);

        //Vertical
        findMatch(circle, 0);
        findMatch(circle, 1);

        //Horizontal
        findMatch(circle, 2);
        findMatch(circle, 3);

        if (matchCirclesHorizontal.size() > 2 || matchCirclesVertical.size() > 2) {
            Toast.makeText(this.getContext(), "Horizontal: " + matchCirclesHorizontal.size(), Toast.LENGTH_SHORT).show ();
            Toast.makeText(this.getContext(), "Vertical: " + matchCirclesVertical.size(), Toast.LENGTH_SHORT).show ();
            return matchCirclesHorizontal.size() > matchCirclesVertical.size() ? 1 : 0;
        }
        else
            return -1;

    }

    protected void findMatch(List<Integer> circle, int direction)
    {
        List<Integer> neighbor = findNeighbor(circle, direction);
        if (neighbor.size() != 0 && isThereAMatch(circle, neighbor))
        {
            if (direction == 0 || direction == 1)
                matchCirclesVertical.add(neighbor);
            else
                matchCirclesHorizontal.add(neighbor);
            findMatch(neighbor, direction);
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
