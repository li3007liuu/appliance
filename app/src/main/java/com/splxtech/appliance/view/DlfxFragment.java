package com.splxtech.appliance.view;

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
import com.splxtech.appliance.chart.BxtActivity;
import com.splxtech.appliance.chart.XbfxActivity;
import com.splxtech.appliance.model.SelectButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li300 on 2016/7/20 0020.
 */
public class DlfxFragment extends Fragment {

    private ListView listView;
    private List<SelectButton> selectButtonList = new ArrayList<SelectButton>();
    private SelectBtnAdapter selectBtnAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_dlfx,container,false);
        listView = (ListView)view.findViewById(R.id.list_dlfx_item);
        initSelectButton();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        selectBtnAdapter = new SelectBtnAdapter(this.getActivity(), R.layout.item_main_button, selectButtonList);
        listView.setAdapter(selectBtnAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectButton selectButton = selectButtonList.get(i);
                if (selectButton.getName() == "电力参数") {
                    Intent intent = new Intent(getActivity(), DlcsActivity.class);
                    getActivity().startActivity(intent);
                }
                else if(selectButton.getName() == "波形图")
                {
                    Intent intent = new Intent(getActivity(), BxtActivity.class);
                    getActivity().startActivity(intent);
                }
                else if(selectButton.getName() == "谐波列表")
                {
                    Intent intent = new Intent(getActivity(), XblbActivity.class);
                    getActivity().startActivity(intent);
                }
                else if(selectButton.getName() == "谐波分析")
                {
                    Intent intent = new Intent(getActivity(), XbfxActivity.class);
                    getActivity().startActivity(intent);
                }

            }
        });
    }


    private void initSelectButton()
    {
        if (selectButtonList.size()==0)
        {
            selectButtonList.add(new SelectButton("电力参数",R.drawable.dlcs));
            selectButtonList.add(new SelectButton("波形图",R.drawable.dlbx));
            selectButtonList.add(new SelectButton("谐波列表",R.drawable.xblb));
            selectButtonList.add(new SelectButton("谐波分析",R.drawable.xbfx));
        }
    }

}
