package org.gdgpoland.devfest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PushActivity extends AppCompatActivity {
    private static final String TAG = PushActivity.class.getSimpleName();
    TextView textViewTitle;
    TextView textViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        textViewContent = (TextView) findViewById(R.id.content);
        textViewTitle = (TextView) findViewById(R.id.title);
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if(getIntent() != null && getIntent().getExtras() != null) {
            if(getIntent().getExtras().containsKey("ratingUrl")) {
                String ratingUrl = getIntent().getExtras().getString("ratingUrl");
                Log.d(TAG, "onCreate opening: " + ratingUrl);
                if(ratingUrl != null && !("".equals(ratingUrl))) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(ratingUrl));
                    startActivity(i);
                    this.finish();
                    return;
                }
            }
            if(getIntent().getExtras().containsKey("title")) {
                textViewTitle.setText(getIntent().getExtras().getString("title"));
            }
            if(getIntent().getExtras().containsKey("content")) {
                textViewContent.setText(getIntent().getExtras().getString("content"));
            }
        }
    }
}
