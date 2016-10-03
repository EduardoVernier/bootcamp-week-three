package com.iws.futurefaces.topartists.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.iws.futurefaces.topartists.R;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		EditText username = (EditText) findViewById(R.id.main_username);
		FloatingActionButton continueButton =
				(FloatingActionButton) findViewById(R.id.main_continue_button);

		continueButton.setOnClickListener(new ContinueClickListener(username));
	}

	private class ContinueClickListener implements View.OnClickListener {

		private EditText usernameEditText;

		public ContinueClickListener(EditText usernameEditText) {
			this.usernameEditText = usernameEditText;
		}

		@Override
		public void onClick(View v) {

			Intent intent = new Intent(MainActivity.this, ArtistListActivity.class);
			intent.putExtra(getString(R.string.username), usernameEditText.getText().toString());
			startActivity(intent);
		}
	}
}
