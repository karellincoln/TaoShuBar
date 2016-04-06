package com.cn.ciao.taoshubar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.LogRecord;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MallFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<String>book_in_all= new ArrayList<String>();
    private boolean[] chooce;

    private Button mall_for_books;
    private Activity myActivity;
    private BaseAdapterForBookMall adapterForBookMall;
    private ListView listview;
    private AVUser avUser;
    private boolean firstTime=true;

    public void findBook(String bookName){
        adapterForBookMall.removeAllItem();
        AVQuery<AVObject>queryForNeeded = new AVQuery<>("BookOnSell");
        AVQuery<AVObject>queryOnSell = queryForNeeded.whereEqualTo("OnTrace","false");
        AVQuery<AVObject> queryNotSame = queryOnSell.whereNotEqualTo("OwnerName",avUser.getUsername());
        AVQuery<AVObject>forName = queryNotSame.whereEqualTo("BookName", bookName);
        forName.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null) {
                    List<Book>books_needed = new ArrayList<Book>();
                    List<String> id = new LinkedList<String>();
                    List<Book>list_book = null;
                    for (AVObject avObject : list) {
                        JSONArray jsonArray = avObject.getJSONArray("BookList");
                        try {
                            list_book = Book.fromList(jsonArray);
                            books_needed.addAll(list_book);
                            id.add(avObject.getObjectId());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    if(books_needed != null &&books_needed.size() != 0) {
                        int length = books_needed.size();
                        if(length == 1)
                            adapterForBookMall.addBook(books_needed.get(0),id.get(0));
                        else {
//                                Collections.sort(books_needed);
                            adapterForBookMall.addBook(books_needed.get(0),id.get(0));
                            adapterForBookMall.addBook(books_needed.get(1),id.get(1));
                        }
                    }
                }
            }
        });
    }

    private OnFragmentInteractionListener mListener;

    public MallFragment() {
// Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @paramparam1 Parameter 1.
     * @paramparam2 Parameter 2.
     * @return A new instance of fragment MallFragment.
     */
// TODO: Rename and change types and number of parameters
    public static MallFragment newInstance(String param1, String param2) {
        MallFragment fragment = new MallFragment();
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
// Inflate the layout for this fragment
        Bundle arguments = getArguments();
        avUser = arguments.getParcelable("AVuser");
        final View view = inflater.inflate(R.layout.fragment_mall, container, false);
        mall_for_books= (Button)view.findViewById(R.id.mall_for_books);
        myActivity= getActivity();
        listview= (ListView)view.findViewById(R.id.needed_books);
        if(firstTime) {
            adapterForBookMall = new BaseAdapterForBookMall(myActivity, avUser);
            firstTime=false;
        }
        listview.setAdapter(adapterForBookMall);
        mall_for_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNeededBook();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener!= null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener= (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener= null;
    }

    /**
     * 获得所需要科目的书
     * @return
     */
    public void getNeededBook(){
//        Log.i("----------------","getNeeded");

        final AVQuery<AVObject>bookList = new AVQuery<AVObject>("NeededBook");
        bookList.getInBackground("56dc0660816dfa005a50de45", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    JSONArray books = avObject.getJSONArray("RecommandBooks");
//                    Log.i("books------------",books.toString());
                    try {
                        List<Book>all_books = Book.fromList(books);
//                        Log.i("books_______length___-", "" + all_books.size());
                        book_in_all= new ArrayList<String>();
                        for (Book book : all_books) {
                            if (book.getBookname() != null) {
                                book_in_all.add(book.getBookname());
//                                Log.i("------addbook",book.getBookname());
                            }
                        }
                        if (book_in_all== null || book_in_all.size() == 0) {
                            return;
                        }
                        final int length = book_in_all.size();
//                            Log.i("-------builder","alertDialog");
                        chooce= new boolean[length];

                        for (int i = 0; i< length; ++i) {
                            chooce[i] = false;
                        }

                        AlertDialog builder = new AlertDialog.Builder(getContext())
                                .setTitle("请选择您需要的书")
                                .setMultiChoiceItems(book_in_all.toArray(new String[book_in_all.size()]), chooce, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        chooce[which] = isChecked;
                                    }
                                })
                                .setPositiveButton("选择完成", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i<length; ++i){
//                                            Log.i("------i----","chooce          " + length + "      " + i);
                                            if(chooce[i]){
//                                                Log.i("choose ------",book_in_all.get(i));
                                                findBook(book_in_all.get(i));
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create();
                        builder.show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a>for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
