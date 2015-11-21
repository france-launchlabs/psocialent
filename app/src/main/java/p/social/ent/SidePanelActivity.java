package p.social.ent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SidePanelActivity extends AppCompatActivity{

    String TAG= "delNear-menu";
    ArrayList<NavItems> navItems;

    DrawerLayout mDrawerLayout;
    LinearLayout mDrawerPane;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;

    int currentFragment=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_panel);


//        if(getIntent().getBooleanExtra("shouldAddLocation",false))
//            startActivity(new Intent(SidePanelActivity.this,AddLocation.class));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);//
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.theme_color)));


        //add navigation items
        navItems = new ArrayList<>();
        navItems.add(new NavItems("Home",R.mipmap.ic_launcher));
        navItems.add(new NavItems("Events",R.mipmap.ic_launcher));
        navItems.add(new NavItems("Music",R.mipmap.ic_launcher));
        navItems.add(new NavItems("Media",R.mipmap.ic_launcher));
        navItems.add(new NavItems("News",R.mipmap.ic_launcher));
        navItems.add(new NavItems("Calendar",R.mipmap.ic_launcher));
        navItems.add(new NavItems("About",R.mipmap.ic_launcher));


        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerPane = (LinearLayout)findViewById(R.id.drawerPane);
        mDrawerList = (ListView)findViewById(R.id.drawerList);
        mDrawerList.setAdapter(new DrawerListAdapter(this,navItems));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerLayout.closeDrawer(mDrawerPane);

                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,0,0){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        selectItemFromDrawer(1);
    }

    public void selectItemFromDrawer(int menuIndex) {

        Log.v(TAG, ">>" + menuIndex + "--" + currentFragment);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = null;

//        switch (menuIndex){
//            case 0:{
//                startActivity(new Intent(SidePanelActivity.this,AddLocation.class));
//            }
//            break;
//            case 1:{
//                fragment = new FragmentRestaurants();
//            }
//            break;
//
//        }

        if(currentFragment != menuIndex && fragment!=null) {
            try {
                fragmentManager.beginTransaction()
                        .replace(R.id.mainContent, fragment)
                        .commit();

                currentFragment = menuIndex;

                mDrawerList.setItemChecked(menuIndex, true);
                setTitle(navItems.get(menuIndex).title);
            }catch (IllegalStateException ise){
                Log.v(TAG, "ERROR: " + ise.getLocalizedMessage());
            }
        }




        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerPane);
//        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public class NavItems {
        String title;
        int icon;
        public NavItems(String title,int icon){
            this.title = title;
            this.icon = icon;
        }
    }

    public class DrawerListAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<NavItems> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItems> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int i) {
            return mNavItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_main_menu, null);

                TextView titleView = (TextView) view.findViewById(R.id.menu_title);
                ImageView iconView = (ImageView) view.findViewById(R.id.menu_icon);

                titleView.setText(mNavItems.get(i).title);
//            titleView.setTypeface(Constants.GLOBAL_FONT);


                if(mNavItems.get(i).icon != -1) {
                    Picasso.with(mContext).load(mNavItems.get(i).icon).into(iconView);
                }
                else
                    iconView.setImageBitmap(null);

            } else
                view = convertView;

            return view;
        }
    }

}
