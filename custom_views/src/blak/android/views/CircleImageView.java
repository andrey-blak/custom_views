package blak.android.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {
    private final Path mPath = new Path();
    private Drawable mFrame;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attributes) {
        super(context, attributes);
        parseAttributes(context, attributes);
        ViewUtils.resetHardwareAcceleration(this);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        mFrame = values.getDrawable(R.styleable.CircleImageView_frame);
    }

    public void setFrameDrawable(Drawable frame) {
        mFrame = frame;
        updateBounds();
        invalidate();
    }

    public void setFrameResource(int resId) {
        Resources resources = getResources();
        mFrame = resources.getDrawable(resId);
        updateBounds();
        invalidate();
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        updateBounds();
        return changed;
    }

    private void updateBounds() {
        int w = getWidth();
        int h = getHeight();

        if (mFrame != null) {
            mFrame.setBounds(0, 0, w, h);
        }

        float left = getPaddingLeft();
        float right = w - getPaddingRight();
        float top = getPaddingTop();
        float bottom = h - getPaddingBottom();
        RectF oval = new RectF(left, top, right, bottom);
        mPath.rewind();
        mPath.addOval(oval, Path.Direction.CW);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        super.onDraw(canvas);
        canvas.restore();

        if (mFrame != null) {
            mFrame.draw(canvas);
        }
    }
}
