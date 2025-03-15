package ir.ninjacoder.prograsssheet;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.slider.Slider;
import ir.ninjacoder.prograsssheet.databinding.LayoutMusicPlayersBinding;
import ir.ninjacoder.prograsssheet.interfaces.MediaPlayerListener;
import java.io.File;

public class MusicSheet implements Slider.OnChangeListener {

  private Context context;
  private String musicpatch;
  private Music md;
  private double forwardTime = 0;
  private double backwardTime = 0;
  private Sheet dialog;
  private LayoutMusicPlayersBinding bind;

  public MusicSheet(Context context, String musicpatch) {
    this.context = context;
    this.musicpatch = musicpatch;
    md = new Music(context, musicpatch);
    md.setPathSource(new File(musicpatch));
    bind = LayoutMusicPlayersBinding.inflate(LayoutInflater.from(context));
    dialog = new Sheet(context);
    /// dialog.setContentView(bind.getRoot());
    bind.titlemusic.setText(md.getNameArtist());
    bind.submusic.setText(md.getNameAlbom());
    setMatchParentDialog(true);
    dialog
        .getBehavior()
        .addBottomSheetCallback(
            new BottomSheetBehavior.BottomSheetCallback() {

              @Override
              public void onStateChanged(View arg0, int state) {
                if (state == BottomSheetBehavior.STATE_HIDDEN) {
                  if (md != null) md.pause();
                }
              }

              @Override
              public void onSlide(View arg0, float arg1) {}
            });
    setAsPaletteBackground(true);
    bind.musicseekbar.addOnChangeListener(this);
    Handler mHandler = new Handler(Looper.getMainLooper());
    ((Activity) context)
        .runOnUiThread(
            new Runnable() {

              @Override
              public void run() {
                if (md != null) {
                  int mCurrentPosition = 0;
                  bind.musicseekbar.setValue(md.getCurrentDuration() / 90);
                  int currentPositionInMillis =
                      md.getCurrentDuration(); // زمان در حال پخش در میلی‌ثانیه
                  int currentPositionInSeconds = currentPositionInMillis / 1000; // زمان در ثانیه
                  int minutes = currentPositionInSeconds / 60;
                  int seconds = currentPositionInSeconds % 60;
                  bind.tvtr.setText(String.format("%d:%02d", minutes, seconds));
                }
                mHandler.postDelayed(this, 90);
              }
            });

    forwardTime = 5000;
    backwardTime = 5000;
    int durationInMillis = md.getDuration(); // زمان در میلی‌ثانیه
    int durationInSeconds = durationInMillis / 1000; // زمان در ثانیه
    int minutes = durationInSeconds / 60;
    int seconds = durationInSeconds % 60;
    bind.tvname.setText(String.format("%d:%02d", minutes, seconds));
    bind.musicicon.setImageBitmap(md.getImageBitmap());
    bind.play.setColorFilter(
        MaterialColors.getColor(bind.play, com.google.android.material.R.attr.colorPrimary));
    bind.pre.setOnClickListener(
        v -> {
          if ((md.getCurrentDuration() - backwardTime) > 0) {
            md.seekTo(md.getCurrentDuration() - (int) backwardTime);
          } else {
            md.seekTo(0);
          }
        });
    bind.next.setOnClickListener(
        v -> {
          if ((md.getCurrentDuration() + (int) forwardTime) <= md.getDuration()) {
            md.seekTo(md.getCurrentDuration() + (int) forwardTime);
          }
        });
    bind.play.setOnClickListener(
        i -> {
          if (md.isPlaying()) {
            md.pause();
          } else {
            md.start();
          }
        });
    bind.musicseekbar.setValueTo(md.getDuration() / 90);
    start();
  }

  void start() {
    md.setMediaPlayerListener(
        new MediaPlayerListener() {

          @Override
          public void isPlaying(int currentDuration) {}

          @Override
          public void onPause() {
            animateIcon(R.drawable.musicstop, bind.play);
          }

          @Override
          public void onStart() {
            animateIcon(R.drawable.musicplay, bind.play);
          }
        });
  }

  private void animateIcon(int newIcon, View view) {
    AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
    fadeOut.setDuration(300);
    fadeOut.setAnimationListener(
        new Animation.AnimationListener() {
          @Override
          public void onAnimationEnd(Animation animation) {
            ((ImageView) view).setImageResource(newIcon);

            ScaleAnimation scaleIn =
                new ScaleAnimation(
                    0.5f,
                    1.0f,
                    0.5f,
                    1.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);
            scaleIn.setDuration(300);
            view.startAnimation(scaleIn);
            view.setVisibility(View.VISIBLE);
          }

          @Override
          public void onAnimationRepeat(Animation animation) {}

          @Override
          public void onAnimationStart(Animation animation) {}
        });

    bind.play.startAnimation(fadeOut);
  }

  public MusicSheet show() {
    dialog.show();
    return this;
  }

  public MusicSheet dismiss() {
    if (md.isPlaying()) md.pause();
    dialog.dismiss();
    return this;
  }

  public void playMusic() {
    if (dialog.isShowing()) {
      md.start();
    }
  }

  class Sheet extends CustomSheet {

    public Sheet(Context c) {
      super(c);
    }

    @Override
    public View getView() {
      return bind.getRoot();
    }
  }

  @Override
  public void onValueChange(Slider arg0, float progressValue, boolean arg2) {
    if (md != null && arg2) {
      md.seekTo((int) progressValue * 90);
    }
  }

  public void setMusicDead() {
    if (md != null) {
      md.pause();
      md.release();
      md = null;
    }
  }

  public void setAsPaletteBackground(boolean is) {
    if (is) {
      Bitmap map = md.getImageBitmap();
      if (map != null) {
        Palette.from(map)
            .generate(
                pa -> {
                  int dominantColor = pa.getDarkMutedColor(Color.WHITE); // رنگ غالب
                  int vibrantColor = pa.getDarkVibrantColor(Color.WHITE); // رنگ پرانرژی
                  int mutedColor = pa.getDarkMutedColor(Color.BLACK); //   رنگ مات

                  bind.getRoot().setBackgroundColor(darkenColor(dominantColor, 0.4f));
                  bind.next.setColorFilter(darkenColor(mutedColor, 5.0f));
                  autoColor(darkenColor(mutedColor, 5.0f), bind.play);
                  bind.pre.setColorFilter(darkenColor(mutedColor, 5.0f));
                  bind.cardmusic.setCardBackgroundColor(darkenColor(mutedColor, 3.3f));
                  bind.musicseekbar.setThumbTintList(
                      ColorStateList.valueOf(darkenColor(mutedColor, 4.0f)));
                  bind.musicseekbar.setTrackActiveTintList(
                      ColorStateList.valueOf(darkenColor(mutedColor, 4.0f)));
                  int colo = darkenColor(mutedColor, 5.0f);
                  bind.submusic.setTextColor(colo);
                  bind.tvname.setTextColor(colo);
                  bind.tvtr.setTextColor(colo);
                  bind.titlemusic.setTextColor(colo);
                  dialog.getWindow().setNavigationBarColor(darkenColor(dominantColor, 0.4f));
                  dialog.setTitle("Hello");
                });
      }
    }
  }

  private int darkenColor(int color, float factor) {
    // تجزیه رنگ به مقادیر ARGB
    int a = Color.alpha(color);
    int r = (int) (Color.red(color) * factor);
    int g = (int) (Color.green(color) * factor);
    int b = (int) (Color.blue(color) * factor);

    // محدود کردن مقادیر به بازه ۰ تا ۲۵۵
    r = Math.max(0, Math.min(r, 255));
    g = Math.max(0, Math.min(g, 255));
    b = Math.max(0, Math.min(b, 255));

    // بازگرداندن رنگ جدید
    return Color.argb(a, r, g, b);
  }

  void autoColor(int colors, ImageView views) {
    if (ColorUtils.calculateLuminance(colors) > 0.5) {
      views.setColorFilter(Color.BLACK);
    } else if (ColorUtils.calculateLuminance(colors) <= 0.5) {
      views.setColorFilter(Color.WHITE);
    }
  }

  void setMatchParentDialog(boolean is) {
    if (is) dialog.setFullScreen();
  }
}
