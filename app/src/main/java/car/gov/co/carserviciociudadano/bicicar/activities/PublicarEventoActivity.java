package car.gov.co.carserviciociudadano.bicicar.activities;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewLogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.presenter.LogTrayectoPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class PublicarEventoActivity extends BaseActivity implements View.OnClickListener, IViewLogTrayecto {
    @BindView(R.id.rbDatosEvento)  RadioButton rbDatosEvento;
    @BindView(R.id.rbMiRecorrido)  RadioButton rbMiRecorrido;
    @BindView(R.id.lblDatosEvento) TextView lblDatosEvento;
    @BindView(R.id.lblDatosRecorrido) TextView lblDatosRecorrido;
    @BindView(R.id.btnPublicar) Button btnPublicar;
    @BindView(R.id.btnCancelar) TextView btnCancelar;

    Evento mEvento;
    Beneficiario mBeneficiarioLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_evento);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        if (b != null){
            int idEvento = b.getInt(Evento.ID_EVENTO,0);
            mEvento = new Eventos().read(idEvento);
        }
        lblDatosEvento.setText("");
        lblDatosEvento.setText("Distancia: " + mEvento.DistanciaKm + " KM Tiempo:" + mEvento.DuracionMinutos + " Minutos");

        mBeneficiarioLogin = Beneficiarios.readBeneficio();
        List<LogTrayecto> logTrayectoList = new LogTrayectos().List(Enumerator.Estado.TODOS,0, mBeneficiarioLogin.IDBeneficiario, mEvento.IDEvento);

        lblDatosRecorrido.setText("");
        if (logTrayectoList.size() > 0) {
            LogTrayecto logTrayecto = logTrayectoList.get(0);

            String datos = "Distancia: " + logTrayecto.DistanciaKm + ", KM Tiempo:" + logTrayecto.DuracionMinutos + " Minutos, " +
                    " Inicio : " + Utils.round(2, logTrayecto.LatitudePuntoA) + "," + Utils.round(2, logTrayecto.LongitudePuntoA) +
                    " Fin : " + Utils.round(2, logTrayecto.LatitudePuntoB) + "," + Utils.round(2, logTrayecto.LongitudePuntoB );

            lblDatosRecorrido.setText(datos);


        }

        btnPublicar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        rbDatosEvento.setOnClickListener(this);
        rbMiRecorrido.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rbMiRecorrido:
                rbDatosEvento.setChecked(false);
                break;
            case R.id.rbDatosEvento:
                rbMiRecorrido.setChecked(false);
                break;
            case R.id.btnPublicar:
                if (!rbMiRecorrido.isChecked() && !rbDatosEvento.isChecked()){
                    mostrarMensajeDialog("Seleccione tipo publicación");
                    return;
                }

                Intent i = getIntent();
                LogTrayectoPresenter logTrayectoPresenter = new LogTrayectoPresenter(this);
                if (rbMiRecorrido.isChecked()) {
                    if (lblDatosRecorrido.getText().toString() == "") {
                        mostrarMensajeDialog("No hay datos del recorrido");
                    }else{
                        logTrayectoPresenter.actualizarConRecorridoPedagogo(mBeneficiarioLogin.IDBeneficiario, mEvento.IDEvento);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }else{
                    logTrayectoPresenter.actualizarDistanciaConEvento(mEvento.IDEvento);
                    setResult(RESULT_OK, i);
                    finish();
                }


                break;
            case R.id.btnCancelar:
                finish();
                break;
        }
    }


    @Override
    public void onSuccessLogTrayecto() {

    }

    @Override
    public void onErrorLogTrayecto(ErrorApi errorApi) {

    }
}
