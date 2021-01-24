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

    private float mOriginX;
    private float mOriginY;
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
        mPaint = new Paint();
        mPath = new Path();
        mWidth = getWidth();
        mHeight = getHeight();
        padding = mWidth / 12;
        mOriginX = padding;
        mOriginY = mHeight - padding;
        mBlackPaint = new Paint();
        mIsInit = true;
//        kmPerHour = new ArrayList<Float>();
//        seconds = new ArrayList<Integer>();
    }

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

        setBackgroundColor(Color.WHITE);
        drawAxis(canvas, mBlackPaint);

        drawGraphPlotLines(canvas, mPath, mPaint);
        drawTextOnXaxis(canvas);
        drawTextOnYaxis(canvas);

    }

    private void drawTextOnYaxis(Canvas canvas){
        TextPaint textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        //Save the canvas origin onto the stack.
        canvas.save();

        //Draw rows.
        for(int i=1; i<=11; i++){
            //Save the current origin.
            canvas.save();

            //Move to origin of this column.
            canvas.translate( padding/4, mHeight - padding - (i * padding ));
            if(i==11)
                canvas.drawText("km/s", 0, 30, textPaint);
            else
                canvas.drawText("" + i, 0, 30, textPaint);

            //Restore to the starting origin.
            canvas.restore();

        }//for j inner loop.

    }

    private void drawTextOnXaxis(Canvas canvas){
        TextPaint textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        //Save the canvas origin onto the stack.
        canvas.save();
        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = (textHeight / 2) - textPaint.descent();
        RectF textBounds = new RectF(0, 0, mWidth-padding*3, padding);

        //Move to origin of this column.
        canvas.translate( 0, mHeight - padding);

        canvas.drawText("Time(s)", textBounds.centerX(), textBounds.centerY() + textOffset, textPaint);

        //Restore to the starting origin.
        canvas.restore();
    }

    private void drawAxis(Canvas canvas, Paint paint) {


        canvas.drawLine(padding, padding, padding, mHeight - 10, paint);//y-axis
        canvas.drawLine(10, mHeight - padding,
                mWidth - padding, mHeight - padding, paint);//x-axis
    }

    private void drawGraphPlotLines(Canvas canvas, Path path, Paint paint) {
        float xPoint;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);

        mPath.moveTo(mOriginX, mOriginY);//shift origin to graph's origin

        for (int i = 0; i < kmPerHour.size(); i++) {
            xPoint = (float)(mWidth-padding*2)/seconds.get(seconds.size()-1) * seconds.get(i);
            mPath.lineTo(mOriginX + xPoint, mOriginY - (((mOriginY-padding)/10)* kmPerHour.get(i)));

        }//end for
        canvas.drawPath(mPath, paint);
    }


    public void setData(ArrayList<Float> kmPerHour, ArrayList<Integer> seconds) {
        this.kmPerHour = kmPerHour;
        this.seconds = seconds;
    }
}
