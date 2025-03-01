package ir.ninjacoder.prograsssheet;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.annotation.StringRes;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import ir.ninjacoder.prograsssheet.databinding.LayoutEdittextSheetBinding;

public class LayoutSheetEditText {

  private Context context;
  private LayoutEdittextSheetBinding bind;
  private BottomSheetDialog dialog;

  public LayoutSheetEditText(Context context) {
    this.context = context;
    bind = LayoutEdittextSheetBinding.inflate(LayoutInflater.from(context));
    dialog = new BottomSheetDialog(context);
    if (dialog != null) dialog.setContentView(bind.getRoot());
  }

  public LayoutSheetEditText setTitle(@StringRes int title) {
    bind.title.setText(title);
    return this;
  }

  public LayoutSheetEditText setTitle(String title) {
    bind.title.setText(title);
    return this;
  }

  public LayoutSheetEditText setokClick(View.OnClickListener click) {
    bind.btnok.setOnClickListener(click);
    return this;
  }

  public LayoutSheetEditText setnoClick(View.OnClickListener click) {
    bind.btnno.setOnClickListener(click);
    return this;
  }

  public Button getButtonOk() {
    return bind.btnok;
  }

  public Button getButtonNo() {
    return bind.btnno;
  }

  public TextInputLayout getInputLayout() {
    return bind.editors;
  }

  public LayoutSheetEditText setText(String text) {
    bind.editors.getEditText().setText(text);
    return this;
  }

  public boolean isEmptyText() {
    return TextUtils.isEmpty(bind.editors.getEditText().getText().toString());
  }

  public String getText() {
    return bind.editors.getEditText().getText().toString();
  }
  
  public String getTitle(){
    return bind.title.getText().toString();
  }

  public LayoutSheetEditText show() {
    dialog.show();
    return this;
  }

  public LayoutSheetEditText dismiss() {
    if (dialog.isShowing()) dialog.dismiss();
    return this;
  }
}
