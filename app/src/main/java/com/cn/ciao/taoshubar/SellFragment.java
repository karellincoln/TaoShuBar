package com.cn.ciao.taoshubar;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.JSONArrayDeserializer;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellFragment extends Fragment {


    boolean firstTime =true;
    ListView tems;
    String[] placeArray;
    Activity myActivity;
    LayoutInflater myInflate;
    private AVUser avUser;
    private String collegeBookId;
    Button sellButton;
    AppCompatSpinner placeChoicer, bookChoicer;
    EditText priceEditor;
    List<String> recommandedBookList;
    List<Book> bookList;
    ListView fragmentTable;
    BaseAdapterForBookSell  baseAdapter;
    Book chosenBook =null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SellFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellFragment newInstance(String param1, String param2) {
        SellFragment fragment = new SellFragment();
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
        collegeBookId = "56d8189c7db2a2005124d128";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myActivity = getActivity();
        Bundle arguments = getArguments();
        avUser = arguments.getParcelable("AVuser");
        if(firstTime) {
            baseAdapter = new BaseAdapterForBookSell(myActivity);
            AVQuery<AVObject> bookqueryItem = new AVQuery<AVObject>("BookOnSell");
            AVQuery<AVObject> ownerBooks= bookqueryItem.whereEqualTo("OwnerName",avUser.getUsername());
            ownerBooks.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    try {
                        for (AVObject temp : list) {
                            Book myBook = Book.fromList(temp.getJSONArray("BookList")).get(0);
                            baseAdapter.addBook(myBook,temp.getObjectId());
                        }
                    }catch(Exception f){
                        f.printStackTrace();
                    }
                }
            });
            firstTime = false;
        }
        final ViewGroup fragmentHolder = (ViewGroup) inflater.inflate(R.layout.fragment_sell, container, false);
        fragmentTable = (ListView) fragmentHolder.findViewById(R.id.sell_fragmen_table);
        fragmentTable.setAdapter(baseAdapter);
        placeArray = getResources().getStringArray(R.array.place_choice);
        sellButton = (Button) fragmentHolder.findViewById(R.id.sell_button_in_fragment_sell);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSell();
            }
        });
        return fragmentHolder;
    }

    public void startSell() {
        myInflate = LayoutInflater.from(getContext());
        AVQuery<AVObject> bookqueryItem = new AVQuery<AVObject>("NeededBook");
        bookqueryItem.getInBackground("56dc0660816dfa005a50de45", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    JSONArray temp = avObject.getJSONArray("RecommandBooks");
                    try {
                        bookList = Book.fromList(temp);
                        recommandedBookList = new LinkedList<String>();
                        for (Book b : bookList) {
                            recommandedBookList.add(b.getBookname());
                        }
                        ViewGroup sellboard = (ViewGroup) myInflate.inflate(R.layout.layout_sell_board, null);
                        placeChoicer = (AppCompatSpinner) sellboard.findViewById(R.id.sell_board_place_choicer);
                        bookChoicer = (AppCompatSpinner) sellboard.findViewById(R.id.sell_board_book_choicer);
                        bookChoicer.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, recommandedBookList
                        ));
                        priceEditor = (EditText) sellboard.findViewById(R.id.sell_board_price_editor);
                        AlertDialog sellBoard = new AlertDialog.Builder(getContext()).setView(sellboard).setPositiveButton("确定卖出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                int bookId = (int) bookChoicer.getSelectedItemId();
                                int placeId = (int) placeChoicer.getSelectedItemId();
                                String tracePrice = priceEditor.getText().toString();
                               chosenBook = bookList.get(bookId);
                                String tracePlace = placeArray[placeId];
                                chosenBook.setOwnerName(avUser.getUsername());
                                chosenBook.setTracePrice(tracePrice);
                                chosenBook.setTracePlace(tracePlace);
                                JSONArray sellItem = new JSONArray(chosenBook.toList());
                                final AVObject updateSell = new AVObject("BookOnSell");
                                updateSell.put("BookList", sellItem);
                                updateSell.put("OwnerName", avUser.getUsername());
                                updateSell.put("BookName", chosenBook.getBookname());
                                updateSell.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        String id =updateSell.getObjectId();
                                        baseAdapter.addBook(chosenBook, id);
                                        dialog.dismiss();
                                    }
                                });

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        sellBoard.show();
                    } catch (Exception F) {
                        F.printStackTrace();
                    }
                } else {

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
    public void updateLayoutTransition(){
        LayoutTransition mLayoutTransition = new LayoutTransition();
        AnimatorInflater inflater = new AnimatorInflater();
        AnimatorSet animatorAppearing =(AnimatorSet)inflater.loadAnimator(myActivity,R.animator.layout_transition_appearing);
        mLayoutTransition.setAnimator(LayoutTransition.APPEARING,animatorAppearing);
    }
}
