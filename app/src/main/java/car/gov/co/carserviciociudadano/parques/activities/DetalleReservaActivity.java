package car.gov.co.carserviciociudadano.parques.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.DetalleReservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.Reservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
import car.gov.co.carserviciociudadano.parques.interfaces.IReserva;
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
    @BindView(R.id.btnCancelarReserva)  Button mBtnCancelarReserva;
    @BindView(R.id.btnDescargaTiquete)  Button mBtnDescargaTiquete;
    @BindView(R.id.btnEnviarConsignacion)  Button mBtnEnviarConsignacion;
    @BindView(R.id.activity_detalle_reserva)  View mActivity_detalle_reserva;


    private DetalleReserva mDetalleReserva;

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

        configurarControles();

    }
    private void configurarControles(){

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

    @OnClick(R.id.btnEnviarConsignacion) void onEnviarConsignacion() {
        Intent i = new Intent(this,AbonoActivity.class);
        IntentHelper.addObjectForKey(mDetalleReserva,AbonoActivity.TAG);
        startActivityForResult(i,0);
    }

    @OnClick(R.id.btnCancelarReserva) void onCancelarReserva() {
       cancelarReserva();
    }

    private void cancelarReserva(){
        Usuario usuario = new Usuarios().leer();

        Reservas reservas = new Reservas();
        ServicioReserva servicioReserva = new ServicioReserva();
        servicioReserva.setIDReserva(mDetalleReserva.getIDReserva());
        servicioReserva.setLogin(usuario.getLogin());
        servicioReserva.setClaveUsuario(usuario.getClaveUsuario());

        reservas.cancelarReserva(servicioReserva, new IReserva() {
            @Override
            public void onSuccess(ServicioReserva servicioReserva) {
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
                mostrarMensaje(error.getMessage(),mActivity_detalle_reserva);
            }
        });
    }

}
