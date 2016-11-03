package UpdateProgressTest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.myfunctiontest.R;

/**
 * Created by Administrator on 2016/11/3.
 */

public class UpdateMain extends AppCompatActivity implements ProgressInterface{
    private ProgressBar updateBar;
    private Button btnDown,btnPause;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.updatemain);
        updateBar = (ProgressBar) findViewById(R.id.progressBar2);
        btnDown = (Button) findViewById(R.id.button);
        btnPause = (Button) findViewById(R.id.button2);
    }

    @Override
    public void updateProgress(int progress) {

    }


}
