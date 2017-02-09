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
    int[] colorTable = null;


    //VARIABLES FOR SWIPE MANAGEMENT
    float MIN_DELTA; //minimum distance in order for a swipe to take effect

    float initialPositionX;
    float initialPositionY;

    float finalPositionX;
    float finalPositionY;

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
        colorTable = new int[nbRows*nbCols];
        width = w/(float)nbCols;
        MIN_DELTA = width / 2;
        currentX = currentY = width / 2;

        colorTable = getResources().getIntArray(R.array.colorGridL1);
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
                System.out.println(grid[i][j]);
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

    protected List<Integer> findCircle(float x, float y)
    {
        List<Integer> coords = new ArrayList<>();

        int i;

        for (i = 0; i < grid.length; i++)
        {
            int max = Float.compare(y, ((float)grid[i][0].get(1) + width/2));
            int min = Float.compare(y, ((float)grid[i][0].get(1) - width/2));
            if (max == 0 || min == 0 || (max < 0 && min > 0) ) {
                coords.add(i);
                break;
            }

        }

        for (int j = 0; j < grid[i].length; j++)
        {
            int max = Float.compare(x, ((float)grid[i][j].get(0) + width/2));
            int min = Float.compare(x, ((float)grid[i][j].get(0) - width/2));
            if (max == 0 || min == 0 || (max < 0 && min > 0) ) {
                coords.add(j);
                break;
            }
        }
        return coords;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action)
        {
            case (MotionEvent.ACTION_DOWN) :

                initialPositionX = event.getX();
                initialPositionY = event.getY();
                List<Integer> circleCoords = findCircle(initialPositionX, initialPositionY);

                //Toast.makeText(this.getContext(), "Action was DOWN at: " + initialPositionX, Toast.LENGTH_SHORT).show ();
                Toast.makeText(this.getContext(), "Il sagit du cercle " + circleCoords.get(0) + circleCoords.get(1), Toast.LENGTH_SHORT).show ();

                return true;

            case (MotionEvent.ACTION_UP) :

                finalPositionX = event.getX();
                finalPositionY = event.getY();

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

                return true;

            default :
                return super.onTouchEvent(event);
        }
    }
}
