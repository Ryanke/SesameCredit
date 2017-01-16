package ryan.sesamecredit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.icu.text.DisplayContext;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import static android.R.attr.radius;

/**
 * 芝麻信用表盘
 * Created by kerui on 2016/11/22.
 */

public class SesameCreditChart extends View {
    //中心X坐标
    private int centerX;
    //中心Y坐标
    private int centerY;
    private float px1 = 360;
    private float py1 = 540;
    private float px = 360;//外圈运动小圆的x轴坐标
    private float py = 340;//外圈运动小圆的y轴坐标
    private float radio = 200;
    private int RIGHT_LEFT_UP = 2;
    private int LEFT_RIGHT_DOWN = 3;
    private int flag = LEFT_RIGHT_DOWN;
    private Paint InnerCircleP = new Paint();
    private Paint IDrawOutLineP = new Paint();
    private Paint InnerDotP = new Paint();
    private Paint InnerLittleDotP = new Paint();
    private Paint drawOutDotP = new Paint();
    //各维度标题
    private String[] titles = {"350", "较差", "550", "中等", "600", "良好", "650", "优秀", "700", "极好", "950"};
    private Paint titlePaint = new Paint();
    //分数画笔
    private Paint scorePaint;
    private int titleSize = DensityUtils.dp2px(getContext(), 13);
    //分数大小
    private int scoreSize = DensityUtils.dp2px(getContext(), 28);
    private int tb = 20;
    private int pointCount = 2;
    private int score = 80;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mRotate;
    private Matrix mMatrix = new Matrix();
    private Shader mShader;
    private boolean mDoTiming;

    public SesameCreditChart(Context context) {
        super(context);
    }

    public SesameCreditChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#CCFFFF"));   //白色背景
        initPaint();
        DrawInnerCircle(canvas);
        DrawOutLine(canvas);
        DrawInnerDot(canvas);
        DrawInnerLittleDot(canvas);
        DrawDotValue(canvas);
        drawScore(canvas);
        drawPoint(canvas);
        drawTest(canvas);


    }

    private void initPaint() {
        //内圈
        InnerCircleP.setAntiAlias(true);                       //设置画笔为无锯齿
        InnerCircleP.setColor(Color.parseColor("#2f7f7f7f"));                    //设置画笔颜色
        InnerCircleP.setStrokeWidth(30.0f);              //线宽
        InnerCircleP.setStyle(Paint.Style.STROKE);

        //外圈
        IDrawOutLineP.setAntiAlias(true);                       //设置画笔为无锯齿
        IDrawOutLineP.setColor(Color.parseColor("#2f7f7f7f"));                    //设置画笔颜色
        IDrawOutLineP.setStrokeWidth(5.0f);              //线宽
        IDrawOutLineP.setStyle(Paint.Style.STROKE);

        //内圈dot大指针
        InnerDotP.setAntiAlias(false);
        InnerDotP.setColor(Color.parseColor("#ffffff"));                    //设置画笔颜色
        InnerDotP.setStrokeWidth(35.0f);              //线宽
        InnerDotP.setStyle(Paint.Style.STROKE);

        //内圈小指针
        InnerLittleDotP.setAntiAlias(false);
        InnerLittleDotP.setColor(Color.parseColor("#ffffff"));                    //设置画笔颜色
        InnerLittleDotP.setStrokeWidth(30.0f);              //线宽
        InnerLittleDotP.setStyle(Paint.Style.STROKE);

        //标题
        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(Color.WHITE);
        titlePaint.setStyle(Paint.Style.FILL);

        //分数
        scorePaint = new Paint();
        scorePaint.setAntiAlias(true);
        scorePaint.setTextSize(scoreSize);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setStyle(Paint.Style.FILL);


        // drawOutDotP


//        this.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//                    public boolean onPreDraw() {
//                        new threadtwo();
//                        getViewTreeObserver().removeOnPreDrawListener(this);
//                        return false;
//                    }
//                });


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //中心坐标
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 内圈
     *
     * @param canvas 画布
     */
    private void DrawInnerCircle(Canvas canvas) {
        Paint paint2 = new Paint();  //定义一个Paint
        Shader mShader = new LinearGradient(0, 0, 40, 60, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.REPEAT);
        //新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        paint2.setShader(mShader);
        RectF oval = new RectF();                       //RectF对象
        oval.left = 300;                              //左边
        oval.top = 750;                                   //上边
        oval.right = 800;                             //右边
        oval.bottom = 1280;                                //下边
        canvas.drawArc(oval, 170, 200, false, InnerCircleP);    //绘制圆弧
    }

    /**
     * 划外圈
     *
     * @param canvas
     */
    private void DrawOutLine(Canvas canvas) {

        RectF oval = new RectF();                       //RectF对象
        oval.left = 270;                              //左边
        oval.top = 720;                                   //上边
        oval.right = 830;                             //右边
        oval.bottom = 1285;                                //下边
        canvas.drawArc(oval, 167, 205, false, IDrawOutLineP);    //绘制圆弧
    }

    /**
     * 画内圈大指针
     *
     * @param canvas
     */
    private void DrawInnerDot(Canvas canvas) {
        RectF oval = new RectF();                       //RectF对象
        oval.left = 305;                              //左边
        oval.top = 755;                                   //上边
        oval.right = 795;                             //右边
        oval.bottom = 1283;                                //下边
//        canvas.drawArc(oval, 170, 200, false, InnerDotP);    //绘制圆弧

        DashPathEffect pathEffect = new DashPathEffect(new float[]{6, 171f}, 1);
        InnerDotP.setStyle(Paint.Style.STROKE);
        InnerDotP.setStrokeWidth(40);
        InnerDotP.setColor(Color.WHITE);
        InnerDotP.setAntiAlias(true);
        InnerDotP.setPathEffect(pathEffect);
//        canvas.drawPath(path, InnerDotP);
        canvas.drawArc(oval, 170, 200, false, InnerDotP);
    }

    /**
     * 画内圈小指针
     *
     * @param canvas
     */
    private void DrawInnerLittleDot(Canvas canvas) {

        RectF oval = new RectF();                       //RectF对象
        oval.left = 300;                              //左边
        oval.top = 750;                                   //上边
        oval.right = 800;                             //右边
        oval.bottom = 1280;                                //下边

        DashPathEffect pathEffect = new DashPathEffect(new float[]{3, 27f}, 1);
        InnerLittleDotP.setStyle(Paint.Style.STROKE);
        InnerLittleDotP.setStrokeWidth(30);
        InnerLittleDotP.setColor(Color.WHITE);
        InnerLittleDotP.setAntiAlias(true);
        InnerLittleDotP.setPathEffect(pathEffect);

        canvas.drawArc(oval, 170, 200, false, InnerLittleDotP);

    }

    /**
     * 画刻度值
     *
     * @param canvas
     */
    private void DrawDotValue(Canvas canvas) {
        String DRAW_STR = "";
        for (int i = 0; i < titles.length; i++) {
            DRAW_STR = DRAW_STR + titles[i] + "          ";
        }

        RectF oval = new RectF();                       //RectF对象
        oval.left = 300;                              //左边
        oval.top = 750;                                   //上边
        oval.right = 800;                             //右边
        oval.bottom = 1280;                                //下边

        Path paths = new Path();
        paths.addArc(oval, 170, 200);
        paths.setFillType(Path.FillType.WINDING);

        drawOutDotP.setStyle(Paint.Style.STROKE);    //绘制路径
        canvas.drawPath(paths, drawOutDotP);
        drawOutDotP.setColor(Color.parseColor("#000000"));
        drawOutDotP.setStyle(Paint.Style.FILL_AND_STROKE);     //沿着路径绘制一段文本
        drawOutDotP.setTextSize(20f);
        canvas.drawTextOnPath(DRAW_STR, paths, -10, 50, drawOutDotP);

//        for (int i = 0; i < 1; i++) {
//
//            canvas.drawText(titles[i], 170 + i * 10, 170 - i * 10, titlePaint);
//        }

    }

    /**
     * 获取雷达图上各个点的坐标（包括维度标题与图标的坐标）
     *
     * @param position    坐标位置
     * @param radarMargin 雷达图与维度标题的间距
     * @param percent     覆盖区的的百分比
     * @return 坐标
     */
    private Point getPoint(int position, int radarMargin, float percent) {
        int x = 0;
        int y = 0;


        return new Point(x, y);
    }

    /**
     * 绘制分数
     *
     * @param canvas 画布
     */
    private void drawScore(Canvas canvas) {
        int score = 80;
        //计算总分
        canvas.drawText(score + "", centerX, centerY + scoreSize / 2, scorePaint);
    }

//    /**
//     * 绘制外圈移动白点
//     *
//     * @param canvas
//     */
//    private void drawOutDot(Canvas canvas) {
//
//        canvas.rotate((float) ((score + 2) * 3.6), getWidth() / 2, getHeight() / 2);
//        canvas.drawArc(rectf, -90, arc_y_1, false, paint_white);
//    }

    /**
     * 画外圆圆点
     */
    private void drawPoint(Canvas canvas) {

        RectF oval = new RectF();                       //RectF对象
        oval.left = 270;                              //左边
        oval.top = 720;                                   //上边
        oval.right = 830;                             //右边
        oval.bottom = 1285;

        Paint paint_big_text = new Paint();
        paint_big_text.setAntiAlias(true);
        paint_big_text.setColor(Color.WHITE);
        paint_big_text.setTextAlign(Paint.Align.CENTER);
        paint_big_text.setStyle(Paint.Style.FILL);
//        paint_big_text.set(InnerLittleDotP);
        //initPaint();
        canvas.drawCircle(px, py
                , 10, paint_big_text);
////        canvas.drawPoint();

    }

    class threadtwo implements Runnable {
        private Thread thread;
        private int statek = 0;

        public threadtwo() {
            thread = new Thread(this);
            thread.start();
        }

        public void run() {
            while (true) {
                switch (statek) {
                    case 0:
                        try {
                            Thread.sleep(400);
                            statek = 1;
                        } catch (InterruptedException e) {
                        }
                        break;
                    case 1:
                        try {
                            Thread.sleep(15);

                            if (true) {
                                if (flag == LEFT_RIGHT_DOWN) {//上半圆的运动轨迹方程
                                    px += 2;
                                    //y = b + (int) Math.sqrt(r^2 - (x - a)^2);
                                    if (px <= px1 + radio) {
                                        py = py1 - (int) Math.sqrt(Math.pow(radio, 2) - Math.pow((px - px1), 2));
                                    } else {
                                        px = px1 + radio;
                                        flag = RIGHT_LEFT_UP;
                                    }
                                } else if (flag == RIGHT_LEFT_UP) {//下半圆的运动轨迹方程
                                    px -= 2;
                                    if (px >= px1 - radio) {
                                        py = py1 + (int) Math.sqrt(Math.pow(radio, 2) - Math.pow((px - px1), 2));
                                    } else {
                                        px = px1 - radio;
                                        flag = LEFT_RIGHT_DOWN;
                                    }
                                }
                            }
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        }
    }


    private void drawTest(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.translate(canvas.getWidth() / 2, 200); //将位置移动画纸的坐标点:150,150
        canvas.drawCircle(0, 0, 100, paint); //画圆圈

        //使用path绘制路径文字
        canvas.save();
        canvas.translate(-75, -75);
        Path path = new Path();
        path.addArc(new RectF(0, 0, 150, 150), -180, 180);
        Paint citePaint = new Paint(paint);
        citePaint.setTextSize(14);
        citePaint.setStrokeWidth(1);
        canvas.drawTextOnPath("http://www.android777.com", path, 28, 0, citePaint);
        canvas.restore();

        Paint tmpPaint = new Paint(paint); //小刻度画笔对象
        tmpPaint.setStrokeWidth(1);

        float y = 100;
        int count = 60; //总刻度数

        for (int i = 0; i < count; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(0f, y, 0, y + 12f, paint);
                canvas.drawText(String.valueOf(i / 5 + 1), -4f, y + 25f, tmpPaint);

            } else {
                canvas.drawLine(0f, y, 0f, y + 5f, tmpPaint);
            }
            canvas.rotate(360 / count, 0f, 0f); //旋转画纸
        }

        //绘制指针
        tmpPaint.setColor(Color.GRAY);
        tmpPaint.setStrokeWidth(4);
        canvas.drawCircle(0, 0, 7, tmpPaint);
        tmpPaint.setStyle(Paint.Style.FILL);
        tmpPaint.setColor(Color.YELLOW);
        canvas.drawCircle(0, 0, 5, tmpPaint);
        canvas.drawLine(0, 10, 0, -65, paint);
    }
}
