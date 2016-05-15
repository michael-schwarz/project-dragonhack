package net.m_schwarz.lecturepotato;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.m_schwarz.lecturepotato.Network.Leaderboard;

import javax.security.auth.login.LoginException;

public class LeaderboardActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("userId");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leaderboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public View rootView;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

            int details = this.getArguments().getInt(ARG_SECTION_NUMBER);
            String range ="";

            switch(details) {
                case 0:
                    range = "day";
                    break;
                case 1:
                    range = "month";
                    break;
                case 2:
                    range = "total";
                    break;
                default:
                    boolean stuff = false;
                    range = "poison"+ details;
                    break;
            }


            AsyncTask<String,Void,Leaderboard.LeaderboardEntries> getDataForLeader = new AsyncTask<String, Void, Leaderboard.LeaderboardEntries>() {
                @Override
                protected Leaderboard.LeaderboardEntries doInBackground(String... params) {
                    try {
                        return Leaderboard.getEntries(params[0],params[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Leaderboard.LeaderboardEntries entries) {
                    receivedBoard(entries);
                }
            };

            getDataForLeader.execute(userId+"",range);

            return rootView;
        }

        public void receivedBoard(Leaderboard.LeaderboardEntries entries){
            LeaderPosition[] values = new LeaderPosition[entries.data.length];

            for(int i=0;i < entries.data.length;i++){
                values[i] = new LeaderPosition(entries.data[i].nickname,""+(1000-Math.floor(entries.data[i].stat*1000)));
            }

            ListView lv = (ListView) rootView.findViewById(R.id.listview);
            ArrayAdapter<LeaderPosition> adapter = new LeaderboardAdapter(getContext(), values);

            lv.setAdapter(adapter);
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // Be really careful
            return (new PlaceholderFragment()).newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DAILY";
                case 1:
                    return "MONTHLY";
                case 2:
                    return "ALL-TIME";
            }
            return null;
        }
    }
}
