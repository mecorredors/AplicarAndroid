package car.gov.co.carserviciociudadano.parques.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import car.gov.co.carserviciociudadano.AppCar;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    ProgressDialog mProgressDialog;
    public BaseFragment() {
        // Required empty public constructor
    }

    protected void showProgress( final View progress,final boolean show) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            progress.setVisibility(show ? View.VISIBLE : View.GONE);
//            progress.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    progress.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            progress.setVisibility(show ? View.VISIBLE : View.GONE);
//        }
        if(isAdded())
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void mostrarMensaje(String mensaje,View view){
        Snackbar.make(view,mensaje  , Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
    protected void mostrarMensaje(String message){
        Toast.makeText( AppCar.getContext() , message ,Toast.LENGTH_SHORT).show();
    }
    protected void mostrarProgressDialog(String mensaje){
        if (isAdded()) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(mensaje);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }
    protected void ocultarProgressDialog(){
        if (mProgressDialog != null) mProgressDialog.dismiss();
    }
}
