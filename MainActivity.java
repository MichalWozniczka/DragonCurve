package com.dcurve.michal.dragoncurve;

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Point;
        import android.os.Bundle;
        import android.view.Display;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.Random;


public class MainActivity extends Activity {
    ArrayList<Float> xCoords = new ArrayList<>();
    ArrayList<Float> yCoords = new ArrayList<>();
    Paint paint = new Paint();
    ImageView drawingImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        display.getSize(size);
        int maxX = size.x;
        int maxY = size.y;

        //sets endpoints for initial line
        xCoords.add((float)5*(maxX/8));
        xCoords.add((float)5*(maxX/8));
        yCoords.add((float)2*(maxY/8));
        yCoords.add((float) 6 * (maxY / 8));

        draw();
    }

    public void screenTapped(View view) {
        //limits number of iterations to prevent extreme slowdown
        if(xCoords.size() < 34000) {
                createNewLines();
                draw();
        }
    }

    //puts in intermediate points to create new lines
    public void createNewLines() {
        //determines if current line segment will buckle to the left or to the right
        int switchLR = 1;

        //places point so that line is split into two lines at right angle
        for (int i = 0; i < xCoords.size() - 1; i++) {
            float midX = (xCoords.get(i) + xCoords.get(i + 1)) / 2;
            float midY = (yCoords.get(i) + yCoords.get(i + 1)) / 2;
            float x = midX - xCoords.get(i);
            float y = midY - yCoords.get(i);
            float angle = (float) 1;

            if (switchLR == 1) {
                xCoords.add(i + 1, midX - (y * angle));
                yCoords.add(i + 1, midY + (x * angle));
            }

            if (switchLR == -1) {
                xCoords.add(i + 1, midX + (y * angle));
                yCoords.add(i + 1, midY - (x * angle));
            }

            i++;
            switchLR *= -1;
        }
    }

    public void draw() {
        setContentView(R.layout.activity_main);

        drawingImageView = (ImageView) this.findViewById(R.id.DrawingImageView);
        Bitmap bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawingImageView.setImageBitmap(bitmap);
        Paint paint = new Paint();

        //draws line between each point in coordinate list
        paint.setStrokeWidth(5);
        for(int i = 0; i < xCoords.size()-1; i++) {
            canvas.drawLine(xCoords.get(i), yCoords.get(i), xCoords.get(i + 1), yCoords.get(i + 1), paint);
        }
    }

    //clears the screen when clear is pressed
    public void clear(View view) {
        xCoords.clear();
        yCoords.clear();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int maxX = size.x;
        int maxY = size.y;

        //adds in back coordinates of initial line segment
        xCoords.add((float)5*(maxX/8));
        xCoords.add((float)5*(maxX/8));
        yCoords.add((float)2*(maxY/8));
        yCoords.add((float) 6 * (maxY / 8));

        draw();
    }
}