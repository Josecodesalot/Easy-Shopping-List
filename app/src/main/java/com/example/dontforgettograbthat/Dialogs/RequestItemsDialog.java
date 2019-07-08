package com.example.dontforgettograbthat.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dontforgettograbthat.Interface.RequestItemsActivityInterface;
import com.example.dontforgettograbthat.Models.Item;
import com.example.dontforgettograbthat.Models.User;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;

public class RequestItemsDialog extends DialogFragment {

    private static final String TAG = "itemDialog";
    private Button btnDelete, btnAddToList;
    private TextView tvItemName, tvListname, tvPrice;
    private FirebaseMethods firebase;
    private User user;
    private String familyName;
    RequestItemsActivityInterface mInterface;
    private Context mContext = getContext();

    Item item;

    public static RequestItemsDialog newInstance(Item item) {
        RequestItemsDialog frag = new RequestItemsDialog();
        frag.setItems(item);
        return frag;
    }

    public void setItems(Item item) {
        this.item=item;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_request , container, false);
        referenceWidgets(view);
        final Bundle bundle;
        bundle=getArguments();

        mInterface = (RequestItemsActivityInterface) getContext();

        familyName = bundle.getString("familyName");
        Log.d(TAG, "onCreateView: family name = " +  familyName);

        if (bundle!=null){
            tvListname.setText(bundle.getString("tvListName"));
            tvItemName.setText(bundle.getString("tvItemName"));
            tvPrice.setText(String.format("%s", bundle.getDouble("tvPrice")));
        }


        btnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.restoreItem(
                        item);

                mInterface.deleteFromRequestedItems(item.getItemKey());
                mInterface.addToCartList(item);
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.deleteRequestedItem(bundle.getString("id"), familyName);
                Log.d(TAG, "onClick: should delete key " + bundle.getString("id"));
                mInterface.deleteFromRequestedItems(item.getItemKey());
                dismiss();
            }
        });


        return view;
    }



    public void referenceWidgets(View view){
        tvItemName = view.findViewById(R.id.tvItemName);
        tvListname = view.findViewById(R.id.tvListName);
        tvPrice =  view.findViewById(R.id.tvPrice);

        btnDelete = view.findViewById(R.id.btnDeleteFromList);
        btnAddToList = view.findViewById(R.id.btnAccept);
    }
}
