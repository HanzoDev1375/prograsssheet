package ir.ninjacoder.prograsssheet.perfence;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.widget.LinearLayoutCompat;
import ir.ninjacoder.prograsssheet.R;
import ir.ninjacoder.prograsssheet.databinding.LayoutPerfenceGroupsBinding;
import ir.ninjacoder.prograsssheet.enums.ModBackground;

public class PerfenceLayoutSubTitle extends LinearLayoutCompat {
  private LayoutPerfenceGroupsBinding binding;
  private ModBackground mod = ModBackground.NONE;

  public PerfenceLayoutSubTitle(Context context, AttributeSet set) {
    super(context, set);
    init();
  }

  public PerfenceLayoutSubTitle(Context context) {
    super(context);
    init();
  }

  void init() {
    binding = LayoutPerfenceGroupsBinding.inflate(LayoutInflater.from(getContext()));
    if (binding != null) {
      removeAllViews();
      addView(binding.getRoot());
      LinearLayoutCompat.LayoutParams params =
          new LinearLayoutCompat.LayoutParams(
              LinearLayoutCompat.LayoutParams.MATCH_PARENT,
              LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

      binding.getRoot().setLayoutParams(params);
      updateBackground();
    }
  }

  public void setTitle(String value) {
    binding.preferenceTitle.setText(value);
  }

  public void setDescription(String value) {
    binding.description.setText(value);
  }

  public void setOnClickListener(OnClickListener c) {
    binding.getRoot().setOnClickListener(c);
  }

  public void setBackgroundMod(ModBackground mod) {
    this.mod = mod;
    updateBackground();
  }

  public void updateBackground() {
    switch (mod) {
      case BOTTOM:
        setBackgroundResource(R.drawable.perfence_bottom);
        break;
      case TOP:
        setBackgroundResource(R.drawable.perfence_top);
        break;
      case MIDDLE:
        setBackgroundResource(R.drawable.perfence_middel);
        break;
      case NONE:
        setBackgroundColor(Color.TRANSPARENT);
        break;
    }
  }
}
