package ir.ninjacoder.prograsssheet;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.widget.SeekBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import ir.ninjacoder.prograsssheet.databinding.LayoutMusicPlayersBinding;
import ir.ninjacoder.prograsssheet.interfaces.MediaPlayerListener;
import java.io.File;

public class MusicSheet implements SeekBar.OnSeekBarChangeListener {

  private Context context;
  private String musicpatch;
  private Music md;
  private double forwardTime = 0;
  private double backwardTime = 0;
  private BottomSheetDialog dialog;
  private LayoutMusicPlayersBinding bind;

  public MusicSheet(Context context, String musicpatch) {
    this.context = context;
    this.musicpatch = musicpatch;
    md = new Music(context, musicpatch);
    md.setPathSource(new File(musicpatch));
    bind = LayoutMusicPlayersBinding.inflate(LayoutInflater.from(context));
    dialog = new BottomSheetDialog(context);
    dialog.setContentView(bind.getRoot());
    bind.titlemusic.setText(md.getNameArtist());
    bind.submusic.setText(md.getNameAlbom());

    bind.musicseekbar.setOnSeekBarChangeListener(this);
    Handler mHandler = new Handler(Looper.getMainLooper());
    ((Activity) context)
        .runOnUiThread(
            new Runnable() {

              @Override
              public void run() {
                if (md != null) {
                  int mCurrentPosition = 0;
                  bind.musicseekbar.setProgress(md.getCurrentDuration() / 90);
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
    MaterialShapeDrawable shp = new MaterialShapeDrawable(
      ShapeAppearanceModel.builder().setAllCorners(CornerFamily.ROUNDED,20)
      .build()
    );
    bind.play.setBackground(shp);
    

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
    bind.musicseekbar.setMax(md.getDuration() / 90);
    start();
  }

  void start() {
    md.setMediaPlayerListener(
        new MediaPlayerListener() {

          @Override
          public void isPlaying(int currentDuration) {}

          @Override
          public void onPause() {
            bind.play.setImageResource(R.drawable.musicstop);
          }

          @Override
          public void onStart() {
            bind.play.setImageResource(R.drawable.musicplay);
          }
        });
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

  @Override
  public void onProgressChanged(SeekBar arg0, int progressValue, boolean arg2) {
    if (md != null && arg2) {
      md.seekTo(progressValue * 90);
    }
  }

  @Override
  public void onStartTrackingTouch(SeekBar arg0) {}

  @Override
  public void onStopTrackingTouch(SeekBar arg0) {}
}
