package org.gdgpoland.devfest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.gdgpoland.devfest.fragments.ListingFragment;
import org.gdgpoland.devfest.network.TSVRequest;
import org.gdgpoland.devfest.network.VolleySingleton;
import org.gdgpoland.devfest.objects.Conference;
import org.gdgpoland.devfest.objects.ConferenceDay;

import java.util.ArrayList;
import java.util.List;



/**
 * Main activity of the application, list all conferences slots into a listView
 * @author Arnaud Camus
 */
public class MainActivity extends AppCompatActivity
        implements Response.Listener<List<Conference>>,
            Response.ErrorListener {

    public static final String CONFERENCES = "conferences";
    private ArrayList<Conference> mConferences = new ArrayList<>();
    private Toolbar mToolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isUpdating;

    private VolleySingleton mVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary),
                ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark),
                ContextCompat.getColor(MainActivity.this, R.color.scrollbar_center));
        swipeRefreshLayout.setEnabled(false);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle(getString(R.string.app_name));
        }
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            //mConferences.addAll(savedInstanceState.<Conference>getParcelableArrayList(CONFERENCES));
            //TODO
        } else {
            mConferences.addAll(Conference.loadFromPreferences(this));
        }
        setupViewPager(mConferences);
        initVolley(this);
        if (mConferences.size() == 0) {
            mToolbar.post(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList(CONFERENCES, mConferences);
    }


    private void setupViewPager(ArrayList<Conference> mConferences) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentFrame);
        if(currentFragment != null && currentFragment instanceof  ListingFragment) {
            //((ListingFragment) currentFragment).notifyDataSetChanged();
            //TODO
        } else {
        }
        ConferenceDay day1 = new ConferenceDay(1, "11/28/2015");
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ListingFragment listingFragment = ListingFragment.newInstance(mConferences, day1);
        ft.replace(R.id.fragmentFrame, listingFragment, "listFragment");
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_more) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if(id == R.id.action_refresh) {
            update();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initVolley(Context context) {
        if (mVolley == null) {
            mVolley = VolleySingleton.getInstance(context);
        }
    }

    public void update() {
        if (!isUpdating) {
            isUpdating = true;
            if(swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(true);
            }
            mVolley.addToRequestQueue(new TSVRequest(this, Request.Method.GET, getString(R.string.url_agenda), this, this));
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Toast.makeText(MainActivity.this, getString(R.string.toast_error_network), Toast.LENGTH_LONG).show();
        onUpdateDone();
    }

    @Override
    public void onResponse(List<Conference> response) {
        mConferences.clear();
        mConferences.addAll(response);
        setupViewPager(mConferences);
        onUpdateDone();
    }

    private void onUpdateDone() {
        isUpdating = false;
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public ArrayList<Conference> getConferences() {
        return mConferences;
    }
}
