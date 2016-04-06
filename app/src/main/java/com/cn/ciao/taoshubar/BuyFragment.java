package com.cn.ciao.taoshubar;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyFragment extends Fragment {

    boolean firstTime =true;
    ListView tems;
    String[] placeArray;
    Activity myActivity;
    LayoutInflater myInflate;
    private AVUser avUser;
    Button refreshButton;
    ListView fragmentTable;
    BaseAdapterForBookBuy  baseAdapter;
    Book chosenBook =null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BuyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyFragment newInstance(String param1, String param2) {
        BuyFragment fragment = new BuyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myActivity = getActivity();
        Bundle arguments = getArguments();
        avUser = arguments.getParcelable("AVuser");
        if(firstTime) {
            baseAdapter = new BaseAdapterForBookBuy(myActivity);
            AVQuery<AVObject> bookqueryItem = new AVQuery<AVObject>("BookOnSell");
            AVQuery<AVObject> ownerBooks = bookqueryItem.whereEqualTo("CustomerName", avUser.getUsername());
            ownerBooks.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    try {
                        for (AVObject temp : list) {
                            Book myBook = Book.fromList(temp.getJSONArray("BookList")).get(0);
                            String date_time = temp.getString("Trace_Time");
//                            String
                            myBook.setTraceTime(date_time);
                            baseAdapter.addBook(myBook, temp.getObjectId());
                        }
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
            });
            firstTime = false;
        }
        final ViewGroup fragmentHolder = (ViewGroup) inflater.inflate(R.layout.fragment_buy, container, false);
        fragmentTable = (ListView) fragmentHolder.findViewById(R.id.buy_fragmen_table);
        fragmentTable.setAdapter(baseAdapter);
        refreshButton=(Button) fragmentHolder.findViewById(R.id.refresh_button_in_fragment_buy);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyChange();
            }
        });
        return fragmentHolder;
    }

    public void notifyChange(){
        baseAdapter.removeAllItem();
        AVQuery<AVObject> bookqueryItem = new AVQuery<AVObject>("BookOnSell");
        AVQuery<AVObject> ownerBooks= bookqueryItem.whereEqualTo("CustomerName",avUser.getUsername());
        ownerBooks.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                try {
                    for (AVObject temp : list) {
                        Book myBook = Book.fromList(temp.getJSONArray("BookList")).get(0);
                        String s = temp.getString("Trace_Time");

                        Log.i(" back     ","back     " + s);
                        myBook.setTraceTime(s);
                        baseAdapter.addBook(myBook, temp.getObjectId());
                    }
                } catch (Exception f) {
                    f.printStackTrace();
                }
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
