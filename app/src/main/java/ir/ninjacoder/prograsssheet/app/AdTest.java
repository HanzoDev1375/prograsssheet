package ir.ninjacoder.prograsssheet.app;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class AdTest extends RecyclerView.Adapter<AdTest.VH> {

  private Context context;

  public AdTest(Context context) {
    this.context = context;
  }

  static class VH extends RecyclerView.ViewHolder {
      
    public VH(View view) {
      super(view);
    }
  }

  @Override
  public VH onCreateViewHolder(ViewGroup arg0, int arg1) {
      var tv = new TextView(context);
      tv.setPadding(9,9,9,9);
      tv.setText("Hello");
      tv.setGravity(Gravity.CENTER);
      return new VH(tv);
  }

  @Override
  public void onBindViewHolder(VH arg0, int arg1) {}

  @Override
  public int getItemCount() {
      return 20;
  }
}
