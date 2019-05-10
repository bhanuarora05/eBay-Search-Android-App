package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private int page;
    CheckBox checkBox,New,Used,Unspecified,Localpickup,Freeshipping;
    View group;
    Button search;
    Button clear;
    EditText keyword,milesfrom;
    AutoCompleteTextView zipcode;
    TextView keyword_err,zipcode_err;
    RadioButton zip,current;
    RadioGroup location;
    Spinner spinner;
    String current_loc;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoCompleteAdapter autoSuggestAdapter;

    public static SearchFragment newInstance(int page) {
        SearchFragment fragmentSearch = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        fragmentSearch.setArguments(args);
        return fragmentSearch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        ApiCall.zipcode(getContext(),new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);

                        current_loc=responseObject.getString("zip");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        checkBox = view.findViewById(R.id.checkBox6);
        New = view.findViewById(R.id.checkBox);
        Used = view.findViewById(R.id.checkBox2);
        Unspecified = view.findViewById(R.id.checkBox3);
        Localpickup = view.findViewById(R.id.checkBox4);
        Freeshipping = view.findViewById(R.id.checkBox5);
        group = view.findViewById(R.id.group);
        search = view.findViewById(R.id.button);
        clear= view.findViewById(R.id.button2);
        keyword=view.findViewById(R.id.editText);
        milesfrom=view.findViewById(R.id.editText2);
        zipcode=view.findViewById(R.id.autoCompleteTextView);
        keyword_err=view.findViewById(R.id.textView);
        zipcode_err=view.findViewById(R.id.textView8);
        zip=view.findViewById(R.id.radioButton3);
        current=view.findViewById(R.id.radioButton2);
        location=view.findViewById(R.id.radioGroup);
        RadioButton checkedRadioButton = (RadioButton)location.findViewById(location.getCheckedRadioButtonId());
        zipcode.setEnabled(false);
        zipcode.setInputType(InputType.TYPE_NULL);

        autoSuggestAdapter = new AutoCompleteAdapter(view.getContext(),android.R.layout.simple_dropdown_item_1line);
        zipcode.setThreshold(1);
        zipcode.setAdapter(autoSuggestAdapter);


        zipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback()  {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(zipcode.getText())) {
                        makeApiCall(zipcode.getText().toString());
                    }
                }
                return false;
            }
        });

        location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                if (checkedRadioButton !=zip)
                {
                    zipcode.setEnabled(false);
                    zipcode.setInputType(InputType.TYPE_NULL);
                }
                else{
                    zipcode.setEnabled(true);
                    zipcode.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldsinvisible(v);


            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errormessages(v);


            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearmessages(v);


            }
        });
    }

    private void makeApiCall(String text) {
        ApiCall.make(getView().getContext(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("postalCodes");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("postalCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void fieldsinvisible(View v){
        if (checkBox.isChecked()) {
            group.setVisibility(View.VISIBLE);
        } else if(!checkBox.isChecked()) {

            group.setVisibility(View.GONE);
        }
    }

    public void clearmessages(View v){

        keyword_err.setVisibility(View.GONE);
        zipcode_err.setVisibility(View.GONE);
        zipcode.getText().clear();
        keyword.getText().clear();
        milesfrom.getText().clear();
        spinner.setSelection(0);

        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
            group.setVisibility(View.GONE);
        }

        if (zip.isChecked()) {
            zip.setChecked(false);
            current.setChecked(true);

        }


    }

    public void errormessages(View v){
        keyword_err.setVisibility(View.GONE);
        zipcode_err.setVisibility(View.GONE);
        Context context = getView().getContext();
        CharSequence text = "Please fix all fields with errors";
        int duration = Toast.LENGTH_SHORT;
        boolean flag = false;

        if (keyword.getText().toString().trim().length() == 0) {
            keyword_err.setVisibility(View.VISIBLE);
            flag=true;
        } else{
            keyword_err.setVisibility(View.GONE);
        }

        if (zip.isChecked()) {
            if (zipcode.getText().toString().trim().length() == 0) {
                zipcode_err.setVisibility(View.VISIBLE);
                flag=true;
            } else{
                zipcode_err.setVisibility(View.GONE);
            }
        }


        if(flag ){
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            Intent intent = new Intent(getContext(), DetailsProd.class);
            intent.putExtra("keyword", keyword.getText().toString());
            intent.putExtra("category", spinner.getSelectedItemPosition());
            intent.putExtra("new", String.valueOf(New.isChecked()));
            intent.putExtra("used", String.valueOf(Used.isChecked()));
            intent.putExtra("unspecified", String.valueOf(Unspecified.isChecked()));
            intent.putExtra("localpickup", String.valueOf(Localpickup.isChecked()));
            intent.putExtra("freeshipping", String.valueOf(Freeshipping.isChecked()));
            intent.putExtra("nearbyshipping",String.valueOf(checkBox.isChecked()));
            intent.putExtra("milesfrom", milesfrom.getText().toString());
            intent.putExtra("ziphere", current_loc);
            if(zip.isChecked()) {
                intent.putExtra("otherdist", zipcode.getText().toString());
            }
            else{
                intent.putExtra("otherdist", "");
            }
            startActivityForResult(intent,100);


        }

    }
}
