package com.speech.voice_text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class text_to_voice extends Fragment {

    Button text_btn,share_tts,copy_tts;
    EditText text;
    TextToSpeech textToSpeech;
    TextView toast_text;
    Toast toast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_text_to_voice, container, false);

        final Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.btn_anim);


        text=view.findViewById(R.id.text_space);
        text_btn=view.findViewById(R.id.text_btn);
        share_tts=view.findViewById(R.id.share_tts);
        copy_tts=view.findViewById(R.id.copy_tts);

        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        View layout=layoutInflater.inflate(R.layout.toast,(ViewGroup)getActivity().findViewById(R.id.toast_id));
        toast_text=layout.findViewById(R.id.toast_text);

        toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,120);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        textToSpeech=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS){
                    int lang=textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        text_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_btn.startAnimation(animation);
                text.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String s=text.getText().toString();
                textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        share_tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body=text.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(intent,"Send"));
            }
        });

        copy_tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager= (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String data=text.getText().toString();
                if(!data.isEmpty()) {
                    ClipData clip = ClipData.newPlainText("Edittext", data);
                    clipboardManager.setPrimaryClip(clip);
                    toast_text.setText("Copied to clipboard");
                    toast.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.nav_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (id==R.id.delete){
            text.getText().clear();
            if(textToSpeech!=null){
                textToSpeech.stop();
            }
        }
        if (id==R.id.paste){
            ClipboardManager clipboardManager= (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item1=clipboardManager.getPrimaryClip().getItemAt(0);
            String clip_data=item1.getText().toString();
            if (!clip_data.isEmpty()){
                String text_copy=text.getText().toString();
                text.setText(text_copy+clip_data);
                text.setSelection(text.length());
            }
            else {
                toast_text.setText("Clipboard empty");
                toast.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(textToSpeech!=null){
            textToSpeech.stop();
        }
    }
}
