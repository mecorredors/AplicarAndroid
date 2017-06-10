package car.gov.co.carserviciociudadano.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;

/**
 * Created by Olger on 21/09/2016.
 */
public class BaseActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override protected void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id== android.R.id.home){
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                getSupportFragmentManager().popBackStack();
            else
                super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void mostrarMensaje(String mensaje,View view){
        Snackbar.make(view,mensaje  , Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    protected void mostrarMensaje(String message){
        Toast.makeText( AppCar.getContext() , message ,Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress( final View progress,final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void mostrarMensajeDialog(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);

        builder.setMessage(mensaje);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }
    protected void ocultarTeclado(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(AppCar.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
