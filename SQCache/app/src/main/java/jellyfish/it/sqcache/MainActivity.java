package jellyfish.it.sqcache;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import it.jellyfish.sqcache.SQCache;

public class MainActivity extends AppCompatActivity {


    EditText key;
    EditText value;

    SQCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        key = (EditText)findViewById(R.id.key);
        value = (EditText)findViewById(R.id.value);


        cache = SQCache.with(this);
    }


    public void onSave(View v) {

        String key = this.key.getText().toString();
        String value = this.value.getText().toString();

        if (key.isEmpty() || value.isEmpty()) {
            showMsg("Insert key and value");
            return;
        }

        cache.set(key, value);
    }

    public void onGet(View v) {

        String key = this.key.getText().toString();

        if (key==null) {
            showMsg("Insert key");
            return;
        }

        String value = cache.get(key);
        showMsg(value);


    }





    private void showMsg(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("ALERT");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
}
