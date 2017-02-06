package tp1.inf8405.match3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        setContentView(new CustomView(this, displaymetrics));
        */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Toast.makeText(PlayActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        /*
        //FOR SWIPE DETECTION
        gridview.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext().getApplicationContext())
        {
            public void onSwipeTop()
            {
                Toast.makeText(getApplicationContext().getApplicationContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight()
            {
                Toast.makeText(getApplicationContext().getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft()
            {
                Toast.makeText(getApplicationContext().getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom()
            {
                Toast.makeText(getApplicationContext().getApplicationContext(), "bottom", Toast.LENGTH_SHORT).show();
            }
        });
        */

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        System.out.println("x : " + x + ", y: " + y);

        return true;
    }
}
