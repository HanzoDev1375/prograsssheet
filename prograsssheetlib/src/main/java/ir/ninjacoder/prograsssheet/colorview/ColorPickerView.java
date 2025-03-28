package ir.ninjacoder.prograsssheet.colorview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import androidx.annotation.ColorInt;
import ir.ninjacoder.prograsssheet.databinding.ViewColorPickerBinding;
import ir.ninjacoder.prograsssheet.interfaces.OnColorChangedListener;

public class ColorPickerView extends FrameLayout {

  private ViewColorPickerBinding binding;
  private final float[] currentColorHsv = new float[3];
  private OnColorChangedListener colorchange;

  public ColorPickerView(Context context) {
    super(context);
    init(context);
  }

  public ColorPickerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public void setOnColorChange(OnColorChangedListener colorchange) {
    this.colorchange = colorchange;
  }

  @SuppressLint("ClickableViewAccessibility")
  private void init(Context context) {
    binding = ViewColorPickerBinding.inflate(LayoutInflater.from(context));
    removeAllViews();
    addView(binding.getRoot());

    binding.imageColorPickerHue.setOnTouchListener(
        (v, event) -> {
          if (event.getAction() == MotionEvent.ACTION_MOVE
              || event.getAction() == MotionEvent.ACTION_DOWN
              || event.getAction() == MotionEvent.ACTION_UP) {
            float y = event.getY();
            if (y < 0) y = 0;
            if (y > binding.imageColorPickerHue.getMeasuredHeight()) {
              // to avoid jumping the cursor from bottom to top
              y = binding.imageColorPickerHue.getMeasuredHeight() - 0.001f;
            }
            float hue = 360 - 360f / binding.imageColorPickerHue.getMeasuredHeight() * y;
            if (hue == 360) hue = 0;
            setHue(hue);

            // update view
            binding.viewColorPickerLuminance.setHue(getHue());
            moveCursor();
            binding.cardColorPickerColorNew.setCardBackgroundColor(getColor());

            if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_UP) {
              Log.w("Mod", "");
              if (colorchange != null) {
                colorchange.onColorChanged(getColor());
              }
            }
            return true;
          }
          return false;
        });

    binding.viewColorPickerLuminance.setOnTouchListener(
        (v, event) -> {
          if (event.getAction() == MotionEvent.ACTION_MOVE
              || event.getAction() == MotionEvent.ACTION_DOWN
              || event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX(); // touch event are in dp units.
            float y = event.getY();

            if (x < 0) {
              x = 0;
            }
            if (x > binding.viewColorPickerLuminance.getMeasuredWidth()) {
              x = binding.viewColorPickerLuminance.getMeasuredWidth();
            }
            if (y < 0) {
              y = 0;
            }
            if (y > binding.viewColorPickerLuminance.getMeasuredHeight()) {
              y = binding.viewColorPickerLuminance.getMeasuredHeight();
            }

            setSat(1f / binding.viewColorPickerLuminance.getMeasuredWidth() * x);
            setVal(1 - (1f / binding.viewColorPickerLuminance.getMeasuredHeight() * y));

            // update view
            moveTarget();
            binding.cardColorPickerColorNew.setCardBackgroundColor(getColor());

            if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_UP) {
              if (colorchange != null) {
                colorchange.onColorChanged(getColor());
              }
            }

            return true;
          }
          return false;
        });

    binding.cardColorPickerColorOld.setOnClickListener(
        v -> {
          setColor(binding.cardColorPickerColorOld.getCardBackgroundColor().getDefaultColor());
        });

    // move cursor & target on first draw
    ViewUtil.addOnGlobalLayoutListener(
        this,
        new OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            moveCursor();
            moveTarget();
            ViewUtil.removeOnGlobalLayoutListener(ColorPickerView.this, this);
          }
        });
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    binding = null;
  }

  @ColorInt
  public int getColor() {
    return 255 << 24 | (Color.HSVToColor(currentColorHsv) & 0x00ffffff);
  }

  public void setColor(@ColorInt int color) {
    color = color | 0xff000000; // remove alpha
    Color.colorToHSV(color, currentColorHsv);
    if (binding != null) {
      binding.viewColorPickerLuminance.setHue(getHue());
      binding.cardColorPickerColorNew.setCardBackgroundColor(color);
      binding.cardColorPickerColorOld.setCardBackgroundColor(color);
    }
    moveCursor();
    moveTarget();
  }

  public void setColor(@ColorInt int colorOld, @ColorInt int colorNew) {
    colorOld = colorOld ; // remove alpha
    colorNew = colorNew ;
    Color.colorToHSV(colorNew, currentColorHsv);
    if (binding != null) {
      binding.viewColorPickerLuminance.setHue(getHue());
      binding.cardColorPickerColorOld.setCardBackgroundColor(colorOld);
      binding.cardColorPickerColorNew.setCardBackgroundColor(colorNew);
    }
    moveCursor();
    moveTarget();
  }

  private void moveCursor() {
    if (binding == null) {
      return;
    }
    float y =
        binding.imageColorPickerHue.getMeasuredHeight()
            - (getHue() * binding.imageColorPickerHue.getMeasuredHeight() / 360);
    if (y == binding.imageColorPickerHue.getMeasuredHeight()) {
      y = 0;
    }
    FrameLayout.LayoutParams layoutParams =
        (FrameLayout.LayoutParams) binding.imageColorPickerCursor.getLayoutParams();
    layoutParams.topMargin =
        (int)
            (binding.imageColorPickerHue.getTop()
                + y
                - Math.floor(binding.imageColorPickerCursor.getMeasuredHeight() / 2f));
    binding.imageColorPickerCursor.setLayoutParams(layoutParams);
  }

  private void moveTarget() {
    if (binding == null) {
      return;
    }
    float x = getSat() * binding.viewColorPickerLuminance.getMeasuredWidth();
    float y = (1 - getVal()) * binding.viewColorPickerLuminance.getMeasuredHeight();
    FrameLayout.LayoutParams layoutParams =
        (FrameLayout.LayoutParams) binding.cardColorPickerTarget.getLayoutParams();
    if (false) {
      layoutParams.rightMargin =
          (int)
              (binding.viewColorPickerLuminance.getRight()
                  + x
                  - Math.floor(binding.cardColorPickerTarget.getMeasuredWidth() / 2f));
    } else {
      layoutParams.leftMargin =
          (int)
              (binding.viewColorPickerLuminance.getLeft()
                  + x
                  - Math.floor(binding.cardColorPickerTarget.getMeasuredWidth() / 2f));
    }
    layoutParams.topMargin =
        (int)
            (binding.viewColorPickerLuminance.getTop()
                + y
                - Math.floor(binding.cardColorPickerTarget.getMeasuredHeight() / 2f));
    binding.cardColorPickerTarget.setLayoutParams(layoutParams);
  }

  private float getHue() {
    return currentColorHsv[0];
  }

  private float getSat() {
    return currentColorHsv[1];
  }

  private float getVal() {
    return currentColorHsv[2];
  }

  private void setHue(float hue) {
    currentColorHsv[0] = hue;
  }

  private void setSat(float sat) {
    currentColorHsv[1] = sat;
  }

  private void setVal(float val) {
    currentColorHsv[2] = val;
  }
}
