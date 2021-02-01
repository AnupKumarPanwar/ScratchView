package com.anupkumarpanwar.scratchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.anupkumarpanwar.utils.BitmapUtils;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ScratchView extends View {

    public interface IRevealListener {
        public void onRevealed(ScratchView scratchView);

        public void onRevealPercentChangedListener(ScratchView scratchView, float percent);
    }

    /**
     * Core Items
     */
    private Context mContext;
    private AttributeSet attrs;
    private int styleAttr;
    private View view;

    public static final float STROKE_WIDTH = 12f;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    /**
     * Bitmap holding the scratch region.
     */
    private Bitmap mScratchBitmap;

    /**
     * Drawable canvas area through which the scratchable area is drawn.
     */
    private Canvas mCanvas;

    /**
     * Path holding the erasing path done by the user.
     */
    private Path mErasePath;

    /**
     * Path to indicate where the user have touched.
     */
    private Path mTouchPath;

    /**
     * Paint properties for drawing the scratch area.
     */
    private Paint mBitmapPaint;

    /**
     * Paint properties for erasing the scratch region.
     */
    private Paint mErasePaint;

    /**
     * Gradient paint properties that lies as a background for scratch region.
     */
    private Paint mGradientBgPaint;

    /**
     * Sample Drawable bitmap having the scratch pattern.
     */
    private BitmapDrawable mDrawable;


    /**
     * Listener object callback reference to send back the callback when the text has been revealed.
     */
    private IRevealListener mRevealListener;

    /**
     * Reveal percent value.
     */
    private float mRevealPercent;

    /**
     * Thread Count
     */
    private int mThreadCount = 0;

    Bitmap scratchBitmap;


    public ScratchView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }


    public ScratchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.attrs = attrs;
        init();
    }

    public ScratchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        init();
    }

    /**
     * Initialises the paint drawing elements.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void init() {

        mTouchPath = new Path();

        mErasePaint = new Paint();
        mErasePaint.setAntiAlias(true);
        mErasePaint.setDither(true);
        mErasePaint.setColor(0xFFFF0000);
        mErasePaint.setStyle(Paint.Style.STROKE);
        mErasePaint.setStrokeJoin(Paint.Join.BEVEL);
        mErasePaint.setStrokeCap(Paint.Cap.ROUND);
        mErasePaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));
        setStrokeWidth(6);

        mGradientBgPaint = new Paint();

        mErasePath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        TypedArray arr = mContext.obtainStyledAttributes(attrs, R.styleable.ScratchView,
                styleAttr, 0);

        int overlayImage = arr.getResourceId(R.styleable.ScratchView_overlay_image, R.drawable.ic_scratch_pattern);

        float overlayWidth = arr.getDimension(R.styleable.ScratchView_overlay_width, 1000);
        float overlayHeight = arr.getDimension(R.styleable.ScratchView_overlay_height, 1000);


        String tileMode = arr.getString(R.styleable.ScratchView_tile_mode);
        if (tileMode == null) {
            tileMode = "CLAMP";
        }
        scratchBitmap = BitmapFactory.decodeResource(getResources(), overlayImage);
        if (scratchBitmap == null) {
            scratchBitmap = drawableToBitmap(ContextCompat.getDrawable(getContext(), overlayImage));
        }
        scratchBitmap = Bitmap.createScaledBitmap(scratchBitmap, (int) overlayWidth, (int) overlayHeight, false);
        mDrawable = new BitmapDrawable(getResources(), scratchBitmap);

        switch (tileMode) {
            case "REPEAT":
                mDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                break;
            case "MIRROR":
                mDrawable.setTileModeXY(Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                break;
            case "CLAMP":
                mDrawable.setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                break;
            default:
                mDrawable.setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }

    }

    /**
     * Set the strokes width based on the parameter multiplier.
     *
     * @param multiplier can be 1,2,3 and so on to set the stroke width of the paint.
     */
    public void setStrokeWidth(int multiplier) {
        mErasePaint.setStrokeWidth(multiplier * STROKE_WIDTH);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScratchBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mScratchBitmap);

        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        mDrawable.setBounds(rect);

        int startGradientColor = ContextCompat.getColor(getContext(), R.color.scratch_start_gradient);
        int endGradientColor = ContextCompat.getColor(getContext(), R.color.scratch_end_gradient);


        mGradientBgPaint.setShader(new LinearGradient(0, 0, 0, getHeight(), startGradientColor, endGradientColor, Shader.TileMode.MIRROR));

        mCanvas.drawRect(rect, mGradientBgPaint);
        mDrawable.draw(mCanvas);
//        Toast.makeText(mContext, String.valueOf(getWidth()), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        canvas.drawBitmap(mScratchBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mErasePath, mErasePaint);
    }

    private void touch_start(float x, float y) {
        mErasePath.reset();
        mErasePath.moveTo(x, y);
        mX = x;
        mY = y;
    }


    /**
     * clears the scratch area to reveal the hidden image.
     */
    public void clear() {

        int[] bounds = getViewBounds();
        int left = bounds[0];
        int top = bounds[1];
        int right = bounds[2];
        int bottom = bounds[3];

        int width = right - left;
        int height = bottom - top;
        int centerX = left + width / 2;
        int centerY = top + height / 2;

        left = centerX - width / 2;
        top = centerY - height / 2;
        right = left + width;
        bottom = top + height;

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));

        mCanvas.drawRect(left, top, right, bottom, paint);
        checkRevealed();
        invalidate();
    }


    private void touch_move(float x, float y) {

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mErasePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            drawPath();
        }


        mTouchPath.reset();
        mTouchPath.addCircle(mX, mY, 30, Path.Direction.CW);

    }

    private void drawPath() {
        mErasePath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mErasePath, mErasePaint);
        // kill this so we don't double draw
        mTouchPath.reset();
        mErasePath.reset();
        mErasePath.moveTo(mX, mY);

        checkRevealed();
    }

    public void reveal() {
        clear();
    }

    public void mask() {
        clear();
        mRevealPercent = 0;
        mCanvas.drawBitmap(scratchBitmap, 0, 0, mBitmapPaint);
        invalidate();
    }

    private void touch_up() {
        drawPath();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public int getColor() {
        return mErasePaint.getColor();
    }


    public Paint getErasePaint() {
        return mErasePaint;
    }

    public void setEraserMode() {

        getErasePaint().setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));

    }

    public void setRevealListener(IRevealListener listener) {
        this.mRevealListener = listener;
    }

    public boolean isRevealed() {
        return mRevealPercent >= 0.50;
    }

    private void checkRevealed() {
        if (!isRevealed() && mRevealListener != null) {
            int[] bounds = getViewBounds();
            int left = bounds[0];
            int top = bounds[1];
            int width = bounds[2] - left;
            int height = bounds[3] - top;

            // Do not create multiple calls to compare.
            if (mThreadCount > 1) {
                Log.d("Captcha", "Count greater than 1");
                return;
            }

            mThreadCount++;

            new AsyncTask<Integer, Void, Float>() {

                @Override
                protected Float doInBackground(Integer... params) {

                    try {
                        int left = params[0];
                        int top = params[1];
                        int width = params[2];
                        int height = params[3];

                        Bitmap croppedBitmap = Bitmap.createBitmap(mScratchBitmap, left, top, width, height);

                        return BitmapUtils.getTransparentPixelPercent(croppedBitmap);
                    } finally {
                        mThreadCount--;
                    }
                }

                public void onPostExecute(Float percentRevealed) {

                    // check if not revealed before.
                    if (!isRevealed()) {

                        float oldValue = mRevealPercent;
                        mRevealPercent = percentRevealed;

                        if (oldValue != percentRevealed) {
                            mRevealListener.onRevealPercentChangedListener(ScratchView.this, percentRevealed);
                        }

                        // if now revealed.
                        if (isRevealed()) {
                            mRevealListener.onRevealed(ScratchView.this);
                        }
                    }
                }
            }.execute(left, top, width, height);

        }
    }

    public int[] getViewBounds() {
        int left = 0;
        int top = 0;
        int width = getWidth();
        int height = getHeight();
        return new int[]{left, top, left + width, top + height};
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
