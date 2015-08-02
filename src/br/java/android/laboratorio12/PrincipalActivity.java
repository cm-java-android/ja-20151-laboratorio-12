package br.java.android.laboratorio12;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class PrincipalActivity extends Activity {
	
	private ImageView imageView;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		
		imageView = (ImageView) findViewById(R.id.imageView1);
		
		findViewById(R.id.workerThreadButton).setOnClickListener(
				new View.OnClickListener() {
			
			@Override
			public void onClick(View vParam) {
				acaoNoWorkThread();
				
			}
		});
		
		findViewById(R.id.asyncTaskButton).setOnClickListener(
				new View.OnClickListener() {
			
			@Override
			public void onClick(View vParam) {
				acaoNoAsyncTask();
			}
		});
		
	}
	
	protected void acaoNoAsyncTask() {

		final String urlImagem = ((EditText)findViewById(R.id.urlEditText))
				.getText().toString();
		
		// String - Parâmetro
		// Void - Progresso
		// Drawable - Retorno
		new AsyncTask<String, Void, Drawable>(){

			@Override
			protected void onPreExecute() {
				dialog = ProgressDialog.show(
						PrincipalActivity.this, // Contexto
						"Aguarde", // Titulo
						"Estamos buscando a sua imagem..."); // Mensagem
			}
			
			@Override
			protected Drawable doInBackground(String... params) {
				Drawable d = carregaImagemDaInternet(params[0]);
				return d;
			}
			
			protected void onPostExecute(Drawable result) {
				imageView.setImageDrawable(result);
				dialog.dismiss();
			};		
			
		}.execute(new String[]{urlImagem});
		
	}

	protected void acaoNoWorkThread() {
		
		final String urlImagem = ((EditText)findViewById(R.id.urlEditText))
				.getText().toString();
		
		new Thread(){
			public void run(){
				final Drawable desenho = carregaImagemDaInternet(urlImagem);
				
				imageView.post(new Runnable() {
					
					@Override
					public void run() {
						imageView.setImageDrawable(desenho);
						
					}
				});
			}
		}.start();
		
	}

	public static Drawable carregaImagemDaInternet(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
