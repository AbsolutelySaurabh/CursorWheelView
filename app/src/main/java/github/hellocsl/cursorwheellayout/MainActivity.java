package github.hellocsl.cursorwheellayout;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.hellocsl.cursorwheel.CursorWheelLayout;
import github.hellocsl.cursorwheellayout.adapter.SimpleImageAdapter;
import github.hellocsl.cursorwheellayout.adapter.SimpleTextAdapter;
import github.hellocsl.cursorwheellayout.data.ImageData;
import github.hellocsl.cursorwheellayout.data.MenuItemData;
import github.hellocsl.cursorwheellayout.widget.SimpleTextCursorWheelLayout;

public class MainActivity extends AppCompatActivity implements CursorWheelLayout.OnMenuSelectedListener {

    Random mRandom = new Random();
    @Bind(R.id.test_circle_menu_top)
    CursorWheelLayout mTestCircleMenuTop;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
       // imageView = (ImageView) findViewById(R.id.wheel_image_center);
        initData();
    }

    private void initData() {
        String[] res = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "OFF"};
        final List<MenuItemData> menuItemDatas = new ArrayList<MenuItemData>();
        for (int i = 0; i < res.length; i++) {
            menuItemDatas.add(new MenuItemData(res[i]));
        }

        final ImageView t = (ImageView) findViewById(R.id.id_wheel_menu_center_item);
        final List<ImageData> imageDatas = new ArrayList<ImageData>();
        imageDatas.add(new ImageData(R.drawable.ic_bank_bc, "0"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_china, "1"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_guangda, "2"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_guangfa, "3"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_jianshe, "4"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_jiaotong, "5"));
        SimpleImageAdapter simpleImageAdapter = new SimpleImageAdapter(this, imageDatas);
        mTestCircleMenuTop.setAdapter(simpleImageAdapter);
        mTestCircleMenuTop.setOnMenuSelectedListener(new CursorWheelLayout.OnMenuSelectedListener() {
            @Override
            public void onItemSelected(CursorWheelLayout parent, View view, int pos) {

                t.setImageResource(imageDatas.get(pos).getIMageRes());
                Toast.makeText(MainActivity.this, "Top Menu selected position:" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        mTestCircleMenuTop.setOnMenuItemClickListener(new CursorWheelLayout.OnMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Toast.makeText(MainActivity.this, "Top Menu click position:" + pos, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemSelected(CursorWheelLayout p, View view, int pos) {
    }

}
