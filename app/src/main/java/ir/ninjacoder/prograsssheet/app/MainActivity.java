package ir.ninjacoder.prograsssheet.app;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import ir.ninjacoder.prograsssheet.PrograssSheet;
import ir.ninjacoder.prograsssheet.LayoutSheetEditText;
import ir.ninjacoder.prograsssheet.app.databinding.ActivityMainBinding;
import ir.ninjacoder.prograsssheet.enums.StateMod;

public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Inflate and get instance of binding
    binding = ActivityMainBinding.inflate(getLayoutInflater());

    // set content view to binding's root
    setContentView(binding.getRoot());
    PrograssSheet sheet = new PrograssSheet(this);

    binding.btn.setOnClickListener(
        i -> {
          sheet.setMode(StateMod.PROGRASSH);
          sheet.setTitle("Hello Words393e9e9kckdkddkekekekeowowowowo2929292929292");
          sheet.show();
        });
    binding.btn.setOnLongClickListener(
        i -> {
          var layout = new LayoutSheetEditText(MainActivity.this);
          layout.setTitle(R.string.app_name);
          layout.setokClick(
              it -> {
                Toast.makeText(getApplicationContext(), layout.getText(), 2).show();
                layout.dismiss();
              });
          layout.setnoClick(
              it -> {
                Toast.makeText(getApplicationContext(), layout.getTitle(), 2).show();
                layout.dismiss();
              });
          layout.show();
          return true;
        });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.binding = null;
  }
}
