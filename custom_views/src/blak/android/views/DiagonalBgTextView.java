package blak.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

// background must be "@drawable/..."
public class DiagonalBgTextView extends TextView {
    private final Path mPath = new Path();

    public DiagonalBgTextView(Context context) {
        this(context, null, 0);
    }

    public DiagonalBgTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiagonalBgTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewUtils.resetHardwareAcceleration(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        updatePath(w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        canvas.restore();
    }

    private void updatePath(float w, float h) {
        mPath.rewind();
        mPath.moveTo(0, 0);
        mPath.lineTo(w, 0);
        mPath.lineTo(w, h);
        mPath.lineTo(0, 0);
    }
}
