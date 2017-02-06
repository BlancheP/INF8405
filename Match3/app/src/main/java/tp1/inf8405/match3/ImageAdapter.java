package tp1.inf8405.match3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by alain.trandang on 2017-02-05.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return gameZone.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(gameZone[position]);
        return imageView;
    }

    // references to our images
    private Integer[] gameZone = {
            R.drawable.yellow, R.drawable.green, R.drawable.green, R.drawable.yellow, R.drawable.green, R.drawable.yellow, R.drawable.purple, R.drawable.purple,
            R.drawable.green, R.drawable.purple, R.drawable.orange, R.drawable.red, R.drawable.blue, R.drawable.blue, R.drawable.orange, R.drawable.blue,
            R.drawable.blue, R.drawable.red, R.drawable.orange, R.drawable.red, R.drawable.blue, R.drawable.green, R.drawable.orange, R.drawable.red,
            R.drawable.yellow, R.drawable.red, R.drawable.yellow, R.drawable.orange, R.drawable.blue, R.drawable.red, R.drawable.green, R.drawable.purple
    };
}
