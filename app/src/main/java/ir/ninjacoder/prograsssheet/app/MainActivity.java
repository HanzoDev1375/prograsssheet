package ir.ninjacoder.prograsssheet.app;

import android.Manifest;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import android.graphics.drawable.PictureDrawable;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import ir.ninjacoder.prograsssheet.MusicSheet;
import ir.ninjacoder.prograsssheet.PrograssSheet;
import ir.ninjacoder.prograsssheet.LayoutSheetEditText;
import ir.ninjacoder.prograsssheet.RecyclerViewSearchLayoutSheet;
import ir.ninjacoder.prograsssheet.app.databinding.ActivityMainBinding;
import ir.ninjacoder.prograsssheet.app.glidesvg.SvgSoftwareLayerSetter;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import java.io.File;

public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding binding;
  private RequestBuilder<PictureDrawable> requestBuilder;
  private MusicSheet mso;

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
    Uri uri = Uri.fromFile(new File("/storage/emulated/0/Android/10/Ninja/test.svg"));
    binding.musicfile.setText("/storage/emulated/0/Download/07. Shadmehr Aghili - Mahaal.mp3");
   requestBuilder =   Glide.with(this)
        .as(PictureDrawable.class)
        //  .placeholder(R.drawable.image_loading)
        .error(R.drawable.ic_launcher_background)
        .transition(withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .listener(new SvgSoftwareLayerSetter());
        
        requestBuilder.load(uri).into(binding.videoview);
        

    // for size icon
    ImageView.ScaleType curr = binding.videoview.getScaleType();

    ImageView.ScaleType[] all = ImageView.ScaleType.values();
    int nextOrdinal = (curr.ordinal() + 1) % all.length;
    ImageView.ScaleType next = all[nextOrdinal];
    binding.videoview.setScaleType(next);

    binding.btn.setOnClickListener(
        i -> {
          //          sheet.setMode(StateMod.PROGRASSH);
          //          sheet.setTitle("Hello Words393e9e9kckdkddkekekekeowowowowo2929292929292");
          //          sheet.show();
          mso = new MusicSheet(MainActivity.this, binding.musicfile.getText().toString());

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
    binding.btn2.setOnClickListener(
        i -> {
          var it = new RecyclerViewSearchLayoutSheet(MainActivity.this);
          it.getRecyclerView().setLayoutManager(new LinearLayoutManager(MainActivity.this));
          it.setAdapter(new AdTest(MainActivity.this));
          it.show();
        });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.binding = null;
    mso.setMusicDead();
  }
}
