/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.android.gui2.activity;

import java.lang.ref.WeakReference;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.frostwire.android.R;
import com.frostwire.android.gui2.view.AbstractActivity;
import com.frostwire.util.Ref;

public class MainActivity extends AbstractActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    public MainActivity() {
        super(R.layout.activity_main2);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void initComponents(Bundle savedInstanceState) {
        drawerLayout = findView(R.id.activity_main_drawer_layout);

        drawerList = findView(R.id.activity_main_left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, new String[] { "a", "b", "c" }));
        drawerList.setOnItemClickListener(new DrawerItemClickListener(this));

        drawerToggle = new MenuDrawerToggle(this, drawerLayout);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    protected void initActionBar(ActionBar bar) {
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }

    private void selectItem(int position) {
        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);
    }

    private static final class DrawerItemClickListener implements ListView.OnItemClickListener {

        private final WeakReference<MainActivity> activityRef;

        public DrawerItemClickListener(MainActivity activity) {
            this.activityRef = Ref.weak(activity);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (Ref.alive(activityRef)) {
                activityRef.get().selectItem(position);
            }
        }
    }

    private static final class MenuDrawerToggle extends ActionBarDrawerToggle {

        private final WeakReference<MainActivity> activityRef;

        public MenuDrawerToggle(MainActivity activity, DrawerLayout drawerLayout) {
            super(activity, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);

            // aldenml: even if the parent class hold a strong reference, I decided to keep a weak one
            this.activityRef = Ref.weak(activity);

            drawerLayout.setDrawerListener(this);
        }

        @Override
        public void onDrawerClosed(View view) {
            if (Ref.alive(activityRef)) {
                activityRef.get().invalidateOptionsMenu();
            }
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if (Ref.alive(activityRef)) {
                activityRef.get().invalidateOptionsMenu();
            }
        }
    }
}
