package com.splxtech.appliance.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.splxtech.appliance.R;
import com.splxtech.appliance.adapter.SelectBtnAdapter;
import com.splxtech.appliance.model.SelectButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li300 on 2016/7/20 0020.
 */
public class GrzxFragment extends Fragment {

    private ListView listView;
    private List<SelectButton> selectButtonList = new ArrayList<SelectButton>();
    private SelectBtnAdapter selectBtnAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_grzx,container,false);
        listView = (ListView)view.findViewById(R.id.list_grzx_item);
        initSelectButton();
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        selectBtnAdapter = new SelectBtnAdapter(this.getActivity(),R.layout.item_main_button,selectButtonList);
        listView.setAdapter(selectBtnAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectButton selectButton = selectButtonList.get(i);
                if(selectButton.getName()=="连接管理")
                {
                    Intent intent = new Intent(getActivity(),LjglActivity.class);
                    getActivity().startActivity(intent);
                }
                else if(selectButton.getName()=="调试助手")
                {
                    Intent intent = new Intent(getActivity(),DebugTestActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

    }

    private void initSelectButton()
    {
        if (selectButtonList.size()==0)
        {
            selectButtonList.add(new SelectButton("连接管理",R.drawable.ljgl));
            selectButtonList.add(new SelectButton("设置",R.drawable.sz));
            selectButtonList.add(new SelectButton("关于",R.drawable.gy));
            selectButtonList.add(new SelectButton("调试助手",R.drawable.tszj));
        }
    }

}
