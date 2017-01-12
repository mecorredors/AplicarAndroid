package car.gov.co.carserviciociudadano.parques.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.adapter.AbonosAdapter;
import car.gov.co.carserviciociudadano.parques.adapter.MisReservasAdapter;
import car.gov.co.carserviciociudadano.parques.dataaccess.Abonos;
import car.gov.co.carserviciociudadano.parques.dataaccess.DetalleReservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.Reservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
import car.gov.co.carserviciociudadano.parques.interfaces.IAbono;
import car.gov.co.carserviciociudadano.parques.interfaces.IReserva;
import car.gov.co.carserviciociudadano.parques.model.Abono;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.ServicioReserva;
import car.gov.co.carserviciociudadano.parques.model.Usuario;

public class DetalleReservaActivity extends BaseActivity {

    @BindView(R.id.lblTitulo)  TextView mLblTitulo;
    @BindView(R.id.lblFecha)  TextView mLblFecha;
    @BindView(R.id.lblTotal)  TextView mLblTotal;
    @BindView(R.id.lblEstado)  TextView mLblEstado;
    @BindView(R.id.imageView)   ImageView mImageView;
    @BindView(R.id.lblServicio)  TextView mLblServicio;
    @BindView(R.id.lblNroNoches)  TextView mLblNroNoches;
    @BindView(R.id.lblPrecio)  TextView mLblPrecio;
    @BindView(R.id.lblImpuesto)  TextView mLblImpuesto;
    @BindView(R.id.lblFechaDesde)  TextView mLblFechaDesde;
    @BindView(R.id.lblFechaHasta)  TextView mLblFechaHasta;
    @BindView(R.id.lblNroReserva)  TextView mLblNroReserva;
    @BindView(R.id.btnCancelarReserva)  Button mBtnCancelarReserva;
    @BindView(R.id.btnDescargaTiquete)  Button mBtnDescargaTiquete;
    @BindView(R.id.btnEnviarConsignacion)  Button mBtnEnviarConsignacion;
    @BindView(R.id.activity_detalle_reserva)  View mActivity_detalle_reserva;
    @BindView(R.id.recycler_view)  RecyclerView mRecyclerView;
    @BindView(R.id.progressView)  ProgressBar mProgressView;
    @BindView(R.id.lblAbonos)  TextView mLblAbonos;


    private DetalleReserva mDetalleReserva;
    ProgressDialog mProgressDialog;
    AbonosAdapter mAdaptador;
    List<Abono> mLstAbonos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reserva);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mDetalleReserva = (DetalleReserva) IntentHelper.getObjectForKey(DetalleReservas.TAG);

        mImageView.setVisibility(View.GONE);
        mLblTitulo.setText(mDetalleReserva.getNombreParque());
        mLblEstado.setText(mDetalleReserva.getEstadoReserva() +" "+ mDetalleReserva.getEstadoNombre());
        mLblFecha.setText(Utils.toStringLargeFromDate(mDetalleReserva.getFechaSistemaReserva()));
        mLblTotal.setText(Utils.formatoMoney(mDetalleReserva.getTotalValorReserva()));
        mLblFechaDesde.setText(Utils.toStringLargeFromDate(mDetalleReserva.getFechaInicialReserva()));
        mLblFechaHasta.setText(Utils.toStringLargeFromDate(mDetalleReserva.getFechaFinalReserva()));
        mLblServicio.setText(mDetalleReserva.getNombreServicio());
        mLblNroNoches.setText(String.valueOf(mDetalleReserva.getCantidadReserva()));
        mLblImpuesto.setText(String.valueOf(mDetalleReserva.getImpuestoReserva()));
        mLblPrecio.setText(Utils.formatoMoney(mDetalleReserva.getPrecioReserva()));
        mLblNroReserva.setText(String.valueOf(mDetalleReserva.getIDReserva()));

        configurarControles();


        mRecyclerView.setHasFixedSize(true);

        mLstAbonos= new ArrayList<>();
        mAdaptador = new AbonosAdapter(mLstAbonos);

        mRecyclerView.setAdapter(mAdaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadAbonos();

    }
    private void configurarControles(){
        mLblEstado.setText(mDetalleReserva.getEstadoReserva() +" "+ mDetalleReserva.getEstadoNombre());
        if (mDetalleReserva.getEstadoReserva() == 0){
            mBtnCancelarReserva.setVisibility(View.VISIBLE);
            mBtnDescargaTiquete.setVisibility(View.GONE);
            mBtnEnviarConsignacion.setVisibility(View.VISIBLE);
        }else{
            mBtnCancelarReserva.setVisibility(View.GONE);
            mBtnDescargaTiquete.setVisibility(View.GONE);
            mBtnEnviarConsignacion.setVisibility(View.GONE);

            if (mDetalleReserva.getEstadoReserva() == 1){
                mBtnDescargaTiquete.setVisibility(View.VISIBLE);
            }
        }

    }

    private void  loadAbonos(){
        Abonos abonos = new Abonos();
        showProgress(mProgressView,true);
        abonos.list(mDetalleReserva.getIDReserva(), new IAbono() {
            @Override
            public void onSuccess(List<Abono> lstAbono) {
                showProgress(mProgressView,false);
                mLstAbonos.clear();
                mLstAbonos.addAll(lstAbono);
                mAdaptador.notifyDataSetChanged();
                mLblAbonos.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess() {
                showProgress(mProgressView,false);


            }

            @Override
            public void onError(ErrorApi error) {
                showProgress(mProgressView,false);
            }
        });
    }

    @OnClick(R.id.btnEnviarConsignacion) void onEnviarConsignacion() {
        Intent i = new Intent(this,AbonoActivity.class);
        i.putExtra(DetalleReserva.ID_RESERVA,mDetalleReserva.getIDReserva());
        startActivityForResult(i,0);
    }

    @OnClick(R.id.btnCancelarReserva) void onCancelarReserva() {
        cancelarReservaDialog();
    }

    private void cancelarReservaDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleReservaActivity.this);

        builder.setMessage("¿Desea cancelar la reserva?");

        builder.setPositiveButton("Cancelar Reserva", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelarReserva();
            }
        });

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    private void cancelarReserva(){
        Usuario usuario = new Usuarios().leer();

        Reservas reservas = new Reservas();
        ServicioReserva servicioReserva = new ServicioReserva();
        servicioReserva.setIDReserva(mDetalleReserva.getIDReserva());
        servicioReserva.setLogin(usuario.getLogin());
        servicioReserva.setClaveUsuario(usuario.getClaveUsuario());
        mostrarProgressDialog();
        reservas.cancelarReserva(servicioReserva, new IReserva() {
            @Override
            public void onSuccess(ServicioReserva servicioReserva) {
                if (mProgressDialog!=null) mProgressDialog.dismiss();
                mDetalleReserva.setEstadoReserva(3);
                mDetalleReserva.setEstadoNombre("Anulada");
                configurarControles();
                mostrarMensaje("La reserva fue cancelada ",mActivity_detalle_reserva);
            }

            @Override
            public void onSuccess(boolean res) {

            }

            @Override
            public void onError(ErrorApi error) {
                if (mProgressDialog!=null) mProgressDialog.dismiss();
                mostrarMensaje(error.getMessage(),mActivity_detalle_reserva);
            }
        });
    }

    private void mostrarProgressDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Espere...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            loadAbonos();
        }
    }
}
