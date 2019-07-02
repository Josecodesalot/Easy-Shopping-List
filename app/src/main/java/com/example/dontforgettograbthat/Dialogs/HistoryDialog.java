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

import com.example.dontforgettograbthat.HistoryActivity.HistoryActivity;
import com.example.dontforgettograbthat.Interface.DialogInterface;
import com.example.dontforgettograbthat.R;
import com.example.dontforgettograbthat.utils.FirebaseMethods;

public class HistoryDialog extends DialogFragment {

    private static final String TAG = "itemDialog";
    private Button btnDelete, btnRestore;
    private TextView tvItemName, tvListname, tvPrice;
    private FirebaseMethods firebase;
    DialogInterface mInterface;
    private Context mContext = getActivity();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_history , container, false);
        referenceWidgets(view);
        final Bundle bundle;
        bundle=getArguments();
        firebase = new FirebaseMethods(getActivity());
        mInterface = (HistoryActivity) getContext();

        if (bundle!=null){
            tvListname.setText(bundle.getString("tvListName"));
            tvItemName.setText(bundle.getString("tvItemName"));
            tvPrice.setText(String.format("%s", bundle.getDouble("tvPrice")));
        }


        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.restoreItem(
                        bundle.getString("tvItemName"),
                        bundle.getString("tvListName"),
                        bundle.getLong("tvQuantity"),
                        bundle.getDouble("tvPrice"),
                        bundle.getString("id"));
                firebase.deleteHistory(bundle.getString("id"));
                mInterface.delete("delete");
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.deleteHistory(bundle.getString("id"));
                Log.d(TAG, "onClick: should delete key " + bundle.getString("id"));
                mInterface.delete("delete");
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
        btnRestore = view.findViewById(R.id.btnAccept);



    }
}
