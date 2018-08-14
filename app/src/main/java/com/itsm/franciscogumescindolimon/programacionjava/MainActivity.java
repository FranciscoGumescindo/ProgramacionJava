package com.itsm.franciscogumescindolimon.programacionjava;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.JsonElement;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.widget.ListView;

import com.itsm.franciscogumescindolimon.programacionjava.Modelo.Message;

//El controlador
public class MainActivity extends AppCompatActivity implements AIListener, View.OnClickListener{

    //Declaracion de las variables...
    private ImageButton btnVoice;
    private ImageButton sendmessage;
    private AIService aiService;
    private static final int REQUEST_INTERNET = 200;
    TextToSpeech t1;
    // public static String TAG = "AndroidDialogflow";
    //Declaracion de enlace a Dialogflow....
    public static String TAG = "Ahttps://api.dialogflow.com/v1/query?v=20150910";
    //Declaracion de variables para el chat....
    List<Message> lista;
    ListView milista;
    EditText message;
    AIConfiguration config;
    // public static String API_KEY="YOUR API KEY";
    //public static String API_KEY = "805e679458b84f828030aa72ab6519b1";
    //Declaracion del token de DialogflowUsado para poder conectar...
    public static String API_KEY = "2f86dd5e1c684398b9c627c1630a38e9";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declaracion de boton, y arra...
        btnVoice = (ImageButton) findViewById(R.id.sendspeacker);
        lista = new ArrayList<>();
        validateOS();
        //Configuracion del Idioma especifico del chat....
        config = new AIConfiguration(API_KEY,
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);
        //Casteo de las variables....
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        milista = (ListView) findViewById(R.id.chat);
        sendmessage = (ImageButton) findViewById(R.id.sendmessage);
        sendmessage.setOnClickListener(this);
        //Metodo que se accionara cuando se presione el boton para
        //poder mandar una peticion de voz...
        btnVoice.setOnClickListener(this);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Locale locSpanish = new Locale("spa", "MEX");
                    t1.setLanguage(locSpanish);
                }
            }
        });
        message = (EditText) findViewById(R.id.textmessage);

    }

    //Para bloquear botonoes de navegacion deja en blanco
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(MainActivity.this, Principal.class);
        startActivity(intent);
        finish();
    }

    //Validacion de los permisos de Audio...
    private void validateOS() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_INTERNET);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_INTERNET);
        }
    }
    //Metodo para enviar la peticion a dialogflow con la peticion....
    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();
        final Status status = response.getStatus();
        Log.i(TAG, "Status code: " + status.getCode());
        Log.i(TAG, "Status type: " + status.getErrorType());
        final Metadata metadata = result.getMetadata();
        if (metadata != null) {
            Log.i(TAG, "Intent id: " + metadata.getIntentId());
            Log.i(TAG, "Intent name: " + metadata.getIntentName());
        }
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue();
            }
        }
        String cad = response.getResult().getFulfillment().getSpeech().toString();
        if (cad.compareTo("") == 0) {
            cad = "Algunos Datos salieron con error";
        }
        // texto a voz recibe respuesta de texto
        t1.speak(cad, TextToSpeech.QUEUE_FLUSH, null);
        lista.add(new Message(result.getResolvedQuery(), 1));
        lista.add(new Message(cad, 0));
        AdapMessage adap = new AdapMessage(lista, this);
        milista.setAdapter(adap);
    }

    @Override
    public void onError(AIError error) {
        Log.e(TAG, error.toString());
    }

    @Override
    public void onAudioLevel(float level) {
    }

    @Override
    public void onListeningStarted() {
    }

    @Override
    public void onListeningCanceled() {
    }

    @Override
    public void onListeningFinished() {
    }
    //Declaracion de caso para enviar peticion de texto o voz...
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendspeacker:
                aiService.startListening();
                break;
            case R.id.sendmessage:
                SendText(message.getText().toString());
                break;
        }
    }
    ///Metodo para enviar un peticion de texto a DialogFlow....
    void SendText(String query) {
        final AIConfiguration config = new AIConfiguration(API_KEY,
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);
        final AIDataService aiDataService = new AIDataService(this, config);
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(query);

        new AsyncTask<AIRequest, Void, AIResponse>() {
            private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

            protected void onPreExecute() {
                this.dialog.setMessage("Enviando Mensaje...");
                this.dialog.show();
                super.onPreExecute();
            }

            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    // process aiResponse here
                    Result result = aiResponse.getResult();
                    String cad = aiResponse.getResult().getFulfillment().getSpeech().toString();
                    if (cad.compareTo("") == 0) {
                        cad = "Algunos Datos salieron con error";
                    }
                    t1.speak(cad, TextToSpeech.QUEUE_FLUSH, null);
                    lista.add(new Message(result.getResolvedQuery(), 1));
                    lista.add(new Message(cad, 0));
                    AdapMessage adap = new AdapMessage(lista, MainActivity.this);
                    milista.setAdapter(adap);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    message.setText("");

                }
            }
        }.execute(aiRequest);
    }

}
