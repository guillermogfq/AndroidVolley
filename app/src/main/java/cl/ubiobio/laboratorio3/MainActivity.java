package cl.ubiobio.laboratorio3;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout layout_login;
    private TextInputLayout layout_pass;
    private EditText login;
    private EditText pass;
    private Button init_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout_login = findViewById(R.id.layout_login);
        layout_pass = findViewById(R.id.layout_pass);
        login = findViewById(R.id.login);
        pass = findViewById(R.id.pass);
        init_session = findViewById(R.id.init_session);

        init_session.setOnClickListener(this);
    }

    private boolean verificar(){

        layout_login.setError(null);
        layout_pass.setError(null);

        if(pass.getText().length() < 4){
            layout_pass.setError("Tu contraseña debe tener más de 4 carácteres");
            return false;
        }

        if(login.getText().length() < 4){
            layout_login.setError("Tu nick o tu email debe tener más de 4 carácteres");
            return false;
        }

        return true;
    }

    private void serviceWebLogin(final String login, final String pass){
        Log.d("LOG WS", "entre");
        String WS_URL = "http://servicioswebmoviles.hol.es/index.php/LOGIN_UBB";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(
                Request.Method.POST,
                WS_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("LOG WS", response);
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            Log.d("LOG WS", "nombre: " + responseJson.getJSONObject("data").getString("nombres"));
                            Log.d("LOG WS", "email: " + responseJson.getJSONObject("data").getString("email"));
                            generateToast(responseJson.getString("info"));
                            if(responseJson.getBoolean("resp")){
                                //iniciar otra actividad.....
                            }else{
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LOG WS", error.toString());
                        generateToast("Error en el WEB Service");
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("login",login);
                params.put("pass",pass);
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.init_session:
                if(verificar()){
                    final String password = pass.getText().toString();
                    final String logintext = login.getText().toString();
                    serviceWebLogin(logintext, password);
                }
                break;
        }
    }

    private void generateToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
    }
}
