package com.speech.voice_text;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class voice_to_text extends Fragment implements RecognitionListener{

    Button share_stt,copy_stt;
    ToggleButton speech;
    EditText speech_text;
    String text_before,text_after;
    Intent recognizerIntent;
    SpeechRecognizer speechRecognizer= null;
    Animation animation;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Toast toast;
    TextView toast_text;
    int cursor_position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_voice_to_text,container,false);

        animation= AnimationUtils.loadAnimation(getActivity(),R.anim.btn_anim);

        speech=view.findViewById(R.id.speech);
        speech_text=view.findViewById(R.id.speech_text);
        copy_stt=view.findViewById(R.id.copy_stt);
        share_stt=view.findViewById(R.id.share_stt);

        speech_text.onEditorAction(EditorInfo.IME_ACTION_NONE);

        builder=new  AlertDialog.Builder(getActivity());
        builder.setTitle("Oops..!");
        builder.setPositiveButton("OK",null);
        dialog = builder.create();

        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        View layout=layoutInflater.inflate(R.layout.toast,(ViewGroup)getActivity().findViewById(R.id.toast_id));
        toast_text=layout.findViewById(R.id.toast_text);

        toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,120);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.getActivity());
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);


        speech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    speechRecognizer.startListening(recognizerIntent);
                    speech.startAnimation(animation);
                    speech.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_stop));

                } else {
                    speechRecognizer.stopListening();
                    speech.startAnimation(animation);
                    speech.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_speech));
                }
            }
        });


//        speech_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    Toast.makeText(getActivity(), "Got the focus", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getActivity(), "Lost the focus", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        share_stt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body=speech_text.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(intent,"Send"));
            }
        });

        copy_stt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager= (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String data=speech_text.getText().toString();
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

    public boolean checkPermission()
    {
        return ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
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
            speech_text.getText().clear();
        }
        if (id==R.id.paste){
            ClipboardManager clipboardManager= (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item1=clipboardManager.getPrimaryClip().getItemAt(0);
            String clip_data=item1.getText().toString();
            if (!clip_data.isEmpty()){
                String speech_copy=speech_text.getText().toString();
                speech_text.setText(speech_copy+clip_data);
                speech_text.setSelection(speech_text.length());
            }
            else {
                toast_text.setText("Clipboard empty");
                toast.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        speech.setChecked(false);
        speech.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_speech));

    }

    @Override
    public void onError(int error) {
        String error_msg=getErrorText(error);
        dialog.setMessage(error_msg);
        dialog.show();
        Button positive_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positive_btn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        speech.setChecked(false);
        speech.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_speech));
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        int result_len=0;
        if (speech_text.getSelectionStart()!=speech_text.getSelectionEnd()) {
            result_len=matches.get(0).length();
            cursor_position=speech_text.getSelectionStart();
            text_before = speech_text.getText().toString().substring(0, speech_text.getSelectionStart());
            text_after = speech_text.getText().toString().substring(speech_text.getSelectionEnd(), speech_text.length());
            speech_text.setText(text_before + matches.get(0) + text_after);
            speech_text.setSelection(cursor_position + result_len);
        }
        else{
            speech_text.getText().insert(speech_text.getSelectionStart(), matches.get(0));
        }

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
//            case SpeechRecognizer.ERROR_AUDIO:
//                message = "Audio recording error";
//                break;
//            case SpeechRecognizer.ERROR_CLIENT:
//                message = "Client side error";
//                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions..!\nAllow permission to record audio";
                break;
//            case SpeechRecognizer.ERROR_NETWORK:
//                message = "Network error";
//                break;
//            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
//                message = "Network timeout";
//                break;
//            case SpeechRecognizer.ERROR_NO_MATCH:
//                message = "No match";
//                break;
//            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
//                message = "RecognitionService busy";
//                break;
//            case SpeechRecognizer.ERROR_SERVER:
//                message = "error from server";
//                break;
//            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
//                message = "No speech input";
//                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}

