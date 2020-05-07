package by.bsu.famcs.otvinta.musicrecognition;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Tab1Home");
        tabSpec1.setIndicator("", getResources().getDrawable(R.drawable.ic_tab1_main));
        tabSpec1.setContent(new Intent(this, Tab1HomeActivity.class));

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Tab2AudioList");
        tabSpec2.setIndicator("", getResources().getDrawable(R.drawable.ic_tab2_main));
        tabSpec2.setContent(new Intent(this, Tab2AudioListActivity.class));

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("Tab3Info");
        tabSpec3.setIndicator("", getResources().getDrawable(R.drawable.ic_tab3_main));
        tabSpec3.setContent(new Intent(this, Tab3InfoActivity.class));

        tabHost.addTab(tabSpec1);
        tabHost.addTab(tabSpec2);
        tabHost.addTab(tabSpec3);
    }
}