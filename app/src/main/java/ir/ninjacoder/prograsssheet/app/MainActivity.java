package ir.ninjacoder.prograsssheet.app;

import android.Manifest;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import ir.ninjacoder.prograsssheet.MusicSheet;
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
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED) {
      ActivityCompat.requestPermissions(
          this,
          new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
          },
          1000);
    }
    
    

    binding.btn.setOnClickListener(
        i -> {
          //          sheet.setMode(StateMod.PROGRASSH);
          //          sheet.setTitle("Hello Words393e9e9kckdkddkekekekeowowowowo2929292929292");
          //          sheet.show();
          MusicSheet mso = new MusicSheet(MainActivity.this,binding.musicfile.getText().toString());
          
          mso.show();
          mso.playMusic();
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
