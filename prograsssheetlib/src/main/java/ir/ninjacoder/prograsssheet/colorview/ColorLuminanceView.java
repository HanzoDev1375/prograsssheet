package ir.ninjacoder.prograsssheet.colorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;

public class ColorLuminanceView extends View {

  private Paint paint;
  private Shader luar;
  private final float[] color = {1, 1, 1};
  private boolean hasChanged = true;

  public ColorLuminanceView(Context context) {
    super(context);
  }

  public ColorLuminanceView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    if (paint == null) {
      paint = new Paint();
      luar = new LinearGradient(0, 0, 0, getHeight(), 0xffffffff, 0xff000000, TileMode.CLAMP);
    }
    if (hasChanged) {
      int rgb = Color.HSVToColor(color);
      Shader dalam = new LinearGradient(0, 0, getWidth(), 0, 0xffffffff, rgb, TileMode.CLAMP);
      paint.setShader(new ComposeShader(luar, dalam, PorterDuff.Mode.MULTIPLY));
      hasChanged = false;
    }
    canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
  }

  public void setHue(float hue) {
    color[0] = hue;
    hasChanged = true;
    invalidate();
  }
}
