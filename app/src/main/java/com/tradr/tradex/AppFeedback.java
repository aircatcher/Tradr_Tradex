package com.tradr.tradex;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.tradr.tradex.request.FeedbackRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AppFeedback extends AppCompatActivity
{
    EditText etFeedbackText;
    Button bSubmitFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_request);

        etFeedbackText  = (EditText) findViewById(R.id.etFeedbackText);
        bSubmitFeedback = (Button) findViewById(R.id.bSubmitFeedback);

        // Temporary user details
        final TextView tempFullName    = (TextView) findViewById(R.id.tempFullName);
        final TextView tempEmail       = (TextView) findViewById(R.id.tempEmail);
        Intent intent   = getIntent();
        String fullName = intent.getStringExtra("tvFullName");
        String email    = intent.getStringExtra("tvEmail");
        tempFullName.setText(fullName);
        tempEmail.setText(email);

        bSubmitFeedback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String feedbackText = etFeedbackText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success)
                            {
                                String feedbackText = jsonResponse.getString("feedbackText");
                                jsonResponse.toString();
                                AlertDialog.Builder builder = new AlertDialog.Builder(AppFeedback.this);
                                builder.setMessage("Feedback sent!").setPositiveButton("OK", null).create().show();

                                Intent goToMain = new Intent(AppFeedback.this, MainActivity.class);
                                goToMain.putExtra("feedbackText", feedbackText);
                                AppFeedback.this.startActivity(goToMain);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AppFeedback.this);
                                builder.setMessage("Fails to send the feedback, please try again").setNegativeButton("Retry", null).create().show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                FeedbackRequest feedbackRequest = new FeedbackRequest(feedbackText, responseListener);
                RequestQueue queue = Volley.newRequestQueue(AppFeedback.this);
                queue.add(feedbackRequest);
            }
        });
    }

    // Show action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        return true;
    }
    // Determines if Actin Bar item was selected, then do corresponding action
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.home:
                finishActivity(1);
                return true;

            case R.id.action_bar_about:
                startActivity(new Intent(this, About.class));
                return true;

            case R.id.action_bar_settings:
                startActivity(new Intent(this, Settings.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
