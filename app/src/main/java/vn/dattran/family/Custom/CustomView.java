package vn.dattran.family.Custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.dattran.family.Activity.MainActivity;
import vn.dattran.family.Model.MyPoint;
import vn.dattran.family.Model.Person;

import static vn.dattran.family.Custom.CustomView.Mode.DRAG;
import static vn.dattran.family.Custom.CustomView.Mode.NONE;
import static vn.dattran.family.Custom.CustomView.Mode.ZOOM;

/**
 * Created by DatTran on 09/01/2018.
 */

public class CustomView extends View {
    private List<Person> lsPerson;
    private ScaleGestureDetector detectorScale;
    private GestureDetector gestureDetector;
    private List<MyPoint> lsPoint = new ArrayList<>();
    private static final int WIDTH_RECT = 40, HEIGHT_RECT = 30;
    private static final int HEIGHT_TRIANGLE = 5;
    private static final float offset = 10f;
    private int width=1080,height=1920;
    private boolean initialized;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint;
    private Paint paintLine;
    private TextPaint textPaint;

    private void init() {
        density = getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//ko rang cua
        paint.setColor(Color.parseColor("#009688"));
//        paint.setStrokeWidth(5f);
//        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);

        textPaint.setTextSize(dpToPx(8));
        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setStrokeWidth(2f);
        paintLine.setColor(Color.BLACK);
        detectorScale = new ScaleGestureDetector(getContext(), new ScaleListener());
//        gestureDetector = new GestureDetector(getContext(), new GestureListener());
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
////        MainActivity.print(MeasureSpec.getSize(widthMeasureSpec));
//        int w= (int) (scale*width);
//        int h= (int) (scale*height);
//        setMeasuredDimension(w,h);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.save();
//        canvas.scale(scale, scale,detectorScale.getFocusX(),detectorScale.getFocusY());
        if (lsPerson != null) {
            lsPoint.clear();
            Person root = findRoot();
            float x = offset * 3;
            drawRect(canvas, root, x, x);
            drawFamily(canvas, root, x, x);
        }
//        canvas.restore();
    }

    private void drawFamily(Canvas canvas, Person root, float X, float Y) {
        float startX = X, startY = Y, stopX = X, stopY = Y;
        float x, y;
        if (root != null) {
            //DRAW ROOT
            Person rootF = findCouple(root);
            if (rootF != null) {
                startX += WIDTH_RECT;
                startY += HEIGHT_RECT / 2;
                stopX = startX + offset;
                stopY = startY;
                drawLine(canvas, startX, startY, stopX, stopY, false);
                x = stopX;
                y = stopY - HEIGHT_RECT / 2;
                drawRect(canvas, rootF, y, x);
                if (rootF.getParent() > 0) {
                    drawParent(canvas, rootF, x, y);
                }
            }
            //DRAW CHILDREN
            List<Person> children = findChildren(root);
            int count = children.size();
            if (count > 0) {
                if (rootF == null) {
                    startX = X + WIDTH_RECT / 2;
                    startY = Y + HEIGHT_RECT;
                    stopX = startX;
                    stopY = startY + HEIGHT_RECT / 2;
                    drawLine(canvas, startX, startY, stopX, stopY, false);
                } else {
                    startX = (startX + stopX) / 2;
                    startY = (startY + stopY) / 2;
                    stopX = startX;
                    stopY = stopY + HEIGHT_RECT;
                    drawLine(canvas, startX, startY, stopX, stopY, false);
                }
                float dx;
                if (children.get(0).getLevel() == 3) {
                    dx = (count + 1) * (WIDTH_RECT + offset);
                } else {
                    dx = (count - 1) * (WIDTH_RECT + offset);
                }
                startX = stopX - dx / 2;
                startY = stopY;
                stopX = startX + dx;
                stopY = startY;
                if (count > 1) {
                    drawLine(canvas, startX - 0.2f, startY, stopX + 0.2f, stopY, false);
                } else {
                    drawLine(canvas, startX, startY, stopX, stopY, false);
                }
                float div = count == 1 ? (dx) : (dx) / (count - 1);
                float fromX = 0, fromY = 0, toX = 0, toY = 0;
                for (int i = 0; i < count; i++) {
                    Person child = children.get(i);
                    fromX = startX + div * i;
                    fromY = startY;
                    toX = fromX;
                    toY = startY + 5;
                    drawLine(canvas, fromX, fromY, toX, toY, true);
                    x = toX - WIDTH_RECT / 2;
                    y = toY + HEIGHT_TRIANGLE;
                    drawRect(canvas, child, y, x);
                    drawFamily(canvas, child, x, y);
                }
            }
        }
    }

    private void drawParent(Canvas canvas, Person child, float X, float Y) {
        float startX = X, startY = Y, stopX = X, stopY = Y;
        startX = X + WIDTH_RECT / 2;
        startY = Y - HEIGHT_TRIANGLE * 2;
        stopX = startX;
        stopY = Y - HEIGHT_TRIANGLE;
        drawLine(canvas, startX, startY, stopX, stopY, true);
        drawLine(canvas, startX, startY, startX + WIDTH_RECT, startY, false);
        drawLine(canvas, startX + WIDTH_RECT, startY, startX + WIDTH_RECT, startY - HEIGHT_RECT, false);
        startX = startX + WIDTH_RECT - offset / 2;
        startY = startY - HEIGHT_RECT;
        stopX = startX + offset;
        stopY = startY;
        drawLine(canvas, startX, startY, stopX, stopY, false);
        List<Person> parent = findParent(child);
        float x = startX - WIDTH_RECT;
        float y = startY - HEIGHT_RECT / 2;
        drawRect(canvas, parent.get(0), y, x);
        x = stopX;
        y = stopY - HEIGHT_RECT / 2;
        drawRect(canvas, parent.get(1), y, x);
    }

    private List<Person> findChildren(Person parent) {
        List<Person> ls = new ArrayList<>();
        for (int i = 0; i < lsPerson.size(); i++) {
            Person person = lsPerson.get(i);
            if (person.getParent() > 0 && parent.getFamily() == person.getParent()) {
                ls.add(person);
            }
        }
        return ls;
    }

    private List<Person> findParent(Person child) {
        List<Person> ls = new ArrayList<>();
        for (int i = 0; i < lsPerson.size(); i++) {
            Person person = lsPerson.get(i);
            if (person.getFamily() > 0 && person.getFamily() == child.getParent()) {
                ls.add(person);
            }
        }
        return ls;
    }

    private Person findCouple(Person p) {
        for (int i = 0; i < lsPerson.size(); i++) {
            Person person = lsPerson.get(i);
            if (person.getId() != p.getId() && person.getFamily() > 0 && person.getFamily() == p.getFamily()) {
                return lsPerson.get(i);
            }
        }
        return null;
    }

    private Person findRoot() {
        for (int i = 0; i < lsPerson.size(); i++) {
            if (lsPerson.get(i).getParent() == 0) {
                return lsPerson.get(i);
            }
        }
        return null;
    }


    private void drawRect(Canvas canvas, Person p, float top, float left) {
        RectF rect = new RectF(dpToPx(left), dpToPx(top), dpToPx(left + WIDTH_RECT), dpToPx(top + HEIGHT_RECT));
//        Rect rect = new Rect(left, top, left+WIDTH_RECT, top+HEIGHT_RECT);
        canvas.drawRoundRect(rect, 10, 10, paint);
        float widthText = textPaint.measureText(p.getName());
        float heightText = textPaint.descent() - textPaint.ascent();
        float dx = rect.left + (rect.width() - widthText) / 2;
        float dy = rect.top + (rect.height() - heightText) / 2 - textPaint.ascent();
        canvas.drawText(p.getName(), dx, dy, textPaint);
        lsPoint.add(new MyPoint(rect.left, rect.top, rect.left + rect.width(), rect.top + rect.height(), p));
    }

    private void drawLine(Canvas canvas, float startX, float startY, float stopX, float stopY, boolean isArrow) {
        float toX = dpToPx(stopX), toY = dpToPx(stopY), fromX = dpToPx(startX), fromY = dpToPx(startY);
        canvas.drawLine(fromX, fromY, toX, toY, paintLine);
        if (isArrow) {
            drawTriangle(canvas, paintLine, toX, toY, dpToPx(HEIGHT_TRIANGLE), false);
        }
    }

    private float oldDist, dx, dy, scalediff, rot, rawX, rawY;
    private Mode mode = NONE;
    private int countPoint;
    private LinearLayout.LayoutParams params;
    private boolean hasMove;
    private PointF mid;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//         gestureDetector.onTouchEvent(event);
//        detectorScale.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                countPoint = event.getPointerCount();
                params = (LinearLayout.LayoutParams) this.getLayoutParams();
                rawX = event.getRawX();
                rawY = event.getRawY();
                dx = rawX - params.leftMargin;
                dy = rawY - params.topMargin;
//                params.width = 1080;
//                params.height = 1920;
//                setLayoutParams(params);
                mode = DRAG;
                hasMove = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                countPoint = event.getPointerCount();
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    mode = ZOOM;
                    mid=midPoint(event);
                }
                rot = rotation(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG && countPoint == 1) {
                    if (!hasMove) {
                        float delta = (float) Math.hypot(rawX - event.getRawX(), rawY - event.getRawY());
                        if (delta > 5f) {
                            hasMove = true;
                        }
                    }
                    int w = (int) (event.getRawX() - dx);
                    int h = (int) (event.getRawY() - dy);
                    params.leftMargin = w;
                    params.topMargin = h;
//                    params.rightMargin = 0;
//                    params.bottomMargin = 0;
//                    params.rightMargin=params.leftMargin+(params.width);
//                    params.bottomMargin=params.topMargin+(params.height);
                    setLayoutParams(params);
                }
                else if (mode == ZOOM) {
                    if (countPoint == 2) {
                        float newRot = rotation(event);
                        float angle = newRot - rot;
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            float tempScale = newDist / oldDist * getScaleX();
                            if (tempScale > 0.6) {
                                scale=tempScale;
//                                scalediff = scale;
//                                setTranslationX(mid.x);
//                                setTranslationY(mid.y);
                                setScaleX(scale);
                                setScaleY(scale);
//                                setPivotX(detectorScale.getFocusX());
//                                setPivotY(detectorScale.getFocusY());
//                                setTranslationX(-mid.x);
//                                setTranslationY(-mid.y);
//                                MainActivity.print(event.getRawX(),event.getX());

                            }
                        }
//                        animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();
//                        int w = (int) (event.getRawX() - dx + scalediff);
//                        int h = (int) (event.getRawY() - dy + scalediff);
//                        params.leftMargin = w;
//                        params.topMargin = h;
//                        setLayoutParams(params);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_UP:
                mode = NONE;
                if (onItemClick != null && countPoint == 1 && !hasMove) {
                    float x = event.getX();
                    float y = event.getY();
                    for (int i = 0; i < lsPoint.size(); i++) {
                        MyPoint point = lsPoint.get(i);
                        if ((x > point.getX() && x < point.getDx()) && (y > point.getY() && y < point.getDy())) {
                            onItemClick.onClick(point.getPerson());
                            break;
                        }
                    }
                }
                break;
        }
        return true;
    }


    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.hypot(x, y);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private void drawTriangle(Canvas canvas, Paint paint, float x, float y, float width, boolean upDown) {
        float halfWidth = width / 2;
        Path path = new Path();
        if (upDown) {//up direction
            path.moveTo(x, y - halfWidth); // Top
            path.lineTo(x - halfWidth, y + halfWidth); // Bottom left
            path.lineTo(x + halfWidth, y + halfWidth); // Bottom right
//        path.lineTo(x, y - halfWidth); // Back to Top
        } else {//down
            path.moveTo(x, y);
            path.lineTo(x - halfWidth, y);
            path.lineTo(x, y + width);
            path.lineTo(x + halfWidth, y);
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    public void setData(List<Person> data) {
        lsPerson = data;
        params = (LinearLayout.LayoutParams) this.getLayoutParams();
        params.width = 1080;
        params.height = 1920;
        setLayoutParams(params);
        invalidate();
    }
    private PointF midPoint(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x/2,y/2);
    }

    public static enum Mode {
        DRAG, ZOOM, NONE
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        public void onClick(Person person);
    }

    public static float density = 1;

    public static final int dpToPx(float dp) {
        return (int) (dp * density);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            setScaleX(scale);
            setScaleY(scale);
//            invalidate();
//            requestLayout();

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

    }

    private float scale = 1f;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            scale = (float) (getScaleX() * 1.5);
            Toast.makeText(getContext(), "double tap=" + scale, Toast.LENGTH_SHORT).show();

            invalidate();
            return true;
//            return super.onDoubleTap(e);
        }
    }
}
