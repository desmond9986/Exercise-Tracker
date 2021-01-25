package com.example.ass3_3011712;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SpeedGraph extends View {
    private Paint mPaint;
    private Boolean mIsInit = false;
    private Path mPath;

    private float mOriginX; // x origin in graph
    private float mOriginY; // y origin in graph
    private int mWidth;
    private int mHeight;
    private int padding;

    private ArrayList<Float> kmPerHour;
    private ArrayList<Integer> seconds;

    private Paint mBlackPaint;


    public SpeedGraph(Context context) {
        super(context);
    }

    public SpeedGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpeedGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init() {
        // initial data
        mPaint = new Paint();
        mPath = new Path();
        mWidth = getWidth();
        mHeight = getHeight();
        padding = mWidth / 12;
        mOriginX = padding;
        mOriginY = mHeight - padding;
        mBlackPaint = new Paint();
        mIsInit = true;
    }

    // a method to draw the ui
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!mIsInit) {
            init();
        }
        mBlackPaint.setColor(Color.BLACK);
        mBlackPaint.setStyle(Paint.Style.STROKE);
        mBlackPaint.setStrokeWidth(10);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.BLUE);

        setBackgroundColor(Color.WHITE); // set background to white color
        drawAxis(canvas, mBlackPaint); // draw the axis lines
        if(kmPerHour != null)
            drawGraphPlotLines(canvas, mPath, mPaint); // plot the graph

        drawTextOnXaxis(canvas); // draw x axis text
        drawTextOnYaxis(canvas); // draw y axis text

    }

    // draw y axis text
    private void drawTextOnYaxis(Canvas canvas){
        TextPaint textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        // save the canvas origin onto the stack.
        canvas.save();

        // draw rows.
        for(int i = 1; i <= 11; i++){
            // save the current origin.
            canvas.save();

            // move to origin of this row
            canvas.translate( padding/4, mHeight - padding - (i * padding ));
            if(i==11)
                canvas.drawText("km/s", 0, 30, textPaint);
            else
                canvas.drawText("" + i, 0, 30, textPaint);

            // restore to the starting origin.
            canvas.restore();

        }

    }

    // draw x axis text
    private void drawTextOnXaxis(Canvas canvas){
        TextPaint textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        // save the canvas origin onto the stack.
        canvas.save();
        // to make the text in center
        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = (textHeight / 2) - textPaint.descent();
        RectF textBounds = new RectF(0, 0, mWidth-padding*3, padding);

        canvas.translate( 0, mHeight - padding);

        canvas.drawText("Time(s)", textBounds.centerX(), textBounds.centerY() + textOffset, textPaint);

        // restore to the starting origin.
        canvas.restore();
    }

    // a method to draw x and y axis
    private void drawAxis(Canvas canvas, Paint paint) {
        canvas.drawLine(padding, padding, padding, mHeight - 10, paint); // y axis
        canvas.drawLine(10, mHeight - padding,
                mWidth - padding, mHeight - padding, paint); //x axis
    }

    // a method to plot on the graph
    private void drawGraphPlotLines(Canvas canvas, Path path, Paint paint) {
        canvas.save();
        float xPoint;
        // set the style of paint
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);

        mPath.moveTo(mOriginX, mOriginY); // shift origin to graph's origin

        for (int i = 0; i < kmPerHour.size(); i++) {
            // x point is depend on the time and y is speed
            xPoint = (float)(mWidth-padding*2)/seconds.get(seconds.size()-1) * seconds.get(i);
            mPath.lineTo(mOriginX + xPoint, mOriginY - (((mOriginY-padding)/10)* kmPerHour.get(i)));

        }
        canvas.drawPath(mPath, paint);
        canvas.restore();
    }

    // call this method to set data of points
    public void setData(ArrayList<Float> kmPerHour, ArrayList<Integer> seconds) {
        this.kmPerHour = kmPerHour;
        this.seconds = seconds;
    }
}
