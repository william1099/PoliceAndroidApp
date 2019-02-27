package william1099.com.polisiku;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import william1099.com.polisiku.FragmentInfo.FragmentA;
import william1099.com.polisiku.FragmentInfo.FragmentB;
import william1099.com.polisiku.FragmentInfo.ViewPagerAdapter;

public class Informasi extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter.addFragment(new FragmentA(), "Info Kontak Penipuan");
        adapter.addFragment(new FragmentB(), "Info Kendaraan");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}
