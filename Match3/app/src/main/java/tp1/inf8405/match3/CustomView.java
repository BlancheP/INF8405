package tp1.inf8405.match3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.Console;


//THIS VIEW REPRESENTS LEVEL 1 OF THE GAME

public class CustomView extends View {

    private int GRID_WIDTH = 8;
    private int GRID_HEIGHT = 5;
    private int CELL_WIDTH = 40;
    private int CELL_HEIGHT = 40;

    private Rect[][] gameGrid = new Rect[GRID_HEIGHT][GRID_WIDTH];

    private Rect gameZone;
    private Paint paint;


    public CustomView(Context context, DisplayMetrics displayMetrics) {
        super(context);

        //REFERENCE : http://stackoverflow.com/questions/22589322/what-does-top-left-right-and-bottom-mean-in-android-rect-object
        int left = 0;
        int top = displayMetrics.heightPixels / 10;
        int right = displayMetrics.widthPixels;
        int bottom = displayMetrics.heightPixels - top;

        // create a rectangle that we'll draw later
        gameZone = new Rect(left, top, right, bottom);

        for(int i = 0; i < GRID_HEIGHT; i++)
        {
            for(int j = 0; j < GRID_WIDTH; j++)
            {
                gameGrid[i][j] = new Rect(left + j*10, top, left+(j*10+CELL_WIDTH), bottom);
            }
        }

        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.BLUE);
        canvas.drawRect(gameZone, paint);
    }
}
