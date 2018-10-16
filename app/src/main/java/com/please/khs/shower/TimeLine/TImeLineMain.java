/*package com.example.khs.shower.TimeLine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khs.shower.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TImeLineMain extends AppCompatActivity {
    private static final String TAG_TIME="time";

    private static final String ARG_PARAM1 = "param1";  //왜썼냐
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView RV;
    private LinearLayoutManager mLinearLayoutManager;
    JSONArray timeArray = null;

    TimeLineListAdapter adapter;

    // TODO: Rename and change types of parameters
    //param 왜쓰는거..?
    private String mParam1;
    private String mParam2;

   //private OnFragmentInteractionListener mListener;

    ArrayList<TimeLine> memoList;

    public TImeLineMain() {
        // Required empty public constructor
    }

    //fragment라 주석처리 함.
    // TODO: Rename and change types and number of parameters
    //public static TImeLineMain newInstance() {
        //TImeLineMain fragment = new TImeLineMain();

       // Bundle args = new Bundle();
       // fragment.setArguments(args);
     //   return fragment;
   //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.); xml을 감정에 따라 recyclerview로 보여줄껀데 이거 보여줘야되냐
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_ag_dag_battle, container, false);
        memoList = new ArrayList<TimeLine>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV = (RecyclerView) findViewById(R.id.RV);
        RV.setHasFixedSize(true);
        RV.setLayoutManager(mLinearLayoutManager);

        adapter = new TimeLineListAdapter(getActivity(),memoList);
        RV.setAdapter(adapter);

        new RefreshAgDag().execute();

    }

    //JSON 파싱
    class RefreshAgDag extends AsyncTask<Void,Void,String>
    {
        String target;

        //TODO:뿅
        @Override
        protected void onPreExecute() { target = "http://10.1.146.100/CNSAY/ListAgDag.php"; }

        @Override
        protected String doInBackground(Void... voids) {
            //JSON 받아온다.
            //String uri = params[0];
            //BufferedReader br = null;
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void  onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String memo, url;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    memo = object.getString("memo");
                    url = object.getString("imageUrl");

                    TimeLine card = new TimeLine(memo,url);

                    memoList.add(card);
                    adapter.notifyDataSetChanged();
                    count++; //왜 필요하져???
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}*/
