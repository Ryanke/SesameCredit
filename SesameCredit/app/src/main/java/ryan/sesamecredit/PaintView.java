package ryan.sesamecredit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by kerui on 2016/11/23.
 */

public class PaintView extends View {

    private int width;
    private int height;
    private float scale;
    private float px1 = 360;
    private float py1 = 540;
    private float px = 360;
    private float py = 340;
    private float radio = 200;
    private int RIGHT_LEFT_UP = 2;
    private int LEFT_RIGHT_DOWN = 3;
    private int flag = LEFT_RIGHT_DOWN;
    private boolean isStart = false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (isStart) {
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
                        invalidate();
                        handler.sendEmptyMessageDelayed(0, 10);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public PaintView(Context context) {
        super(context);
        getWindow(context);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getWindow(context);
    }

    private void getWindow(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();
        scale = context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(px1, py1, radio, paint);
//      canvas.drawCircle(360, 1432-46+height-45, 1432, paint);

        Paint paint2 = new Paint();
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.WHITE);
        canvas.drawCircle(px, py, 5, paint2);
        start();

    }

    private int getRadius() {
        int x = width / 2;
        int y1 = (int) getPX(23);
        return 1143;
    }

    private float getPX(float dp) {
        return dp * scale + 0.5f;
    }

    public void start() {
        isStart = true;
        handler.sendEmptyMessageDelayed(0, 100);
    }

    public void stop() {
        isStart = false;
    }

    public boolean isStart() {
        return isStart;
    }
}