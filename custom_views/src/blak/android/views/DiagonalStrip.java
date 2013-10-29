package blak.android.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

// background must be "@drawable/..."
public class DiagonalStrip extends TextView {
    private double mAngleRadians;
    private int mAngleDegrees;

    private double mRatio;

    private double be;
    private double bd;

    private final Path mPath = new Path();

    public DiagonalStrip(Context context) {
        this(context, null, 0);
    }

    public DiagonalStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiagonalStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(context, attrs);
        setSingleLine(true);
        setEllipsize(null);

        ViewUtils.resetHardwareAcceleration(this);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.DiagonalStrip);

        mAngleDegrees = values.getInt(R.styleable.DiagonalStrip_angle, 0);
        mAngleRadians = Math.toRadians(mAngleDegrees);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        double sin = Math.sin(mAngleRadians);
        double cos = Math.cos(mAngleRadians);

        be = w * cos;
        bd = w * sin;

        int width = (int) Math.round(be + h / sin);
        int height = (int) Math.round(bd + h / cos);

        setMeasuredDimension(width, height);

        mRatio = (double) width / w;
        if (mRatio < 1) {
            float textSize = getTextSize();
            textSize *= (float) mRatio;
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        Path path = createStripPolygon(mPath, getWidth(), getHeight());
        canvas.clipPath(path, Region.Op.INTERSECT);
        super.draw(canvas);
        canvas.restore();
    }

    private Path createStripPolygon(Path path, int w, int h) {
        path.rewind();
        path.moveTo(w, 0);
        path.lineTo(0, h);
        path.lineTo(0, (float) bd);
        path.lineTo((float) be, 0);
        path.lineTo(w, 0);
        return path;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, Math.round(bd));
        if (mRatio < 1) {
            canvas.scale((float) ((float) 1 / mRatio), (float) ((float) 1 / mRatio));
        }
        canvas.rotate(-mAngleDegrees, 0, 0);
        super.onDraw(canvas);
        canvas.restore();
    }
}
