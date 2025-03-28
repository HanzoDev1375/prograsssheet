package ir.ninjacoder.prograsssheet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import ir.ninjacoder.prograsssheet.colorview.ColorPickerView;
import ir.ninjacoder.prograsssheet.databinding.ColorSheetviewBinding;

public class ColorPickerSheet {

  private BottomSheetDialog dialig;
  private Context context;
  private ColorSheetviewBinding binding;
  private OnColorCallBack onColorCallBack;
  private SharedPreferences saveColor;
  private Handler handler = new Handler();
  private Runnable colorValidationRunnable;
  private static final long DEBOUNCE_DELAY = 500;

  public interface OnColorCallBack {
    void onColorResult(String result);
  }

  public ColorPickerSheet(Context context) {
    this.context = context;
    saveColor = context.getSharedPreferences("colors", Context.MODE_PRIVATE);
    dialig = new BottomSheetDialog(context);
    binding = ColorSheetviewBinding.inflate(LayoutInflater.from(context));
    dialig.setContentView(binding.getRoot());
    dialig.setCancelable(false);
    dialig.show();

    if (saveColor != null) {
      binding.colorviews.setColor(saveColor.getInt("cp", 0));
    }

    binding.texthelper.getEditText().setText(String.format("#%06X", binding.colorviews.getColor()));

    binding.colorviews.setOnColorChange(
        c -> {
          binding.texthelper.getEditText().setText(String.format("#%06X", c));
        });

    binding
        .texthelper
        .getEditText()
        .addTextChangedListener(
            new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

              @Override
              public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

              @Override
              public void afterTextChanged(Editable arg0) {
                if (colorValidationRunnable != null) {
                  handler.removeCallbacks(colorValidationRunnable);
                }

                colorValidationRunnable = () -> validateAndSetColor(arg0.toString());
                handler.postDelayed(colorValidationRunnable, DEBOUNCE_DELAY);
              }
            });

    binding.btndissmiss.setOnClickListener(g -> dialig.dismiss());
  }

  private void validateAndSetColor(String colorStr) {
    try {
      if (colorStr.isEmpty() || !colorStr.startsWith("#")) {
        return;
      }

      colorStr = colorStr.trim();

      if (colorStr.length() == 7 || colorStr.length() == 9) {
        int color = Color.parseColor(colorStr);
        binding.colorviews.setColor(color);
      }
    } catch (IllegalArgumentException e) {
    }
  }

  public void setOnColorCallBack(OnColorCallBack onColorCallBack) {
    this.onColorCallBack = onColorCallBack;
    binding.btnok.setOnClickListener(
        v -> {
          int color = binding.colorviews.getColor();
          String result = String.format("#%06X", color);
          onColorCallBack.onColorResult(result);
          Editor editor = saveColor.edit();
          editor.putInt("cp", binding.colorviews.getColor());
          editor.apply();
          dialig.dismiss();
        });
  }
}
