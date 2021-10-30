package com.example.shoppingapp;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TermsFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG_EVENT_DIALOG = "dialog_event";

    View root;

    TextView termsName;
    TextView termsContents;

    String name;
    String content;

    public TermsFragment() {}
    public static TermsFragment getInstance() {
        return new TermsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        root = inflater.inflate(R.layout.fragment_terms, container);

        termsName = (TextView) root.findViewById(R.id.termsName);
        termsContents = (TextView) root.findViewById(R.id.termsContents);
        Button btnExit = (Button) root.findViewById(R.id.btnExit);

        termsName.setText(name);
        termsContents.setText(content);

        btnExit.setOnClickListener(this);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        int width = size.x;
        int height = size.y;

        root.getLayoutParams().width = (int) (width * 0.9);
        root.getLayoutParams().height = (int) (height * 0.8);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public void setText(String name, String content) {
        this.name = name;
        this.content = content;
    }
}