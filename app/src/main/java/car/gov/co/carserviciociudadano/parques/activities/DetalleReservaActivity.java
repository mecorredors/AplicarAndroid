package car.gov.co.carserviciociudadano.parques.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.dataaccess.DetalleReservas;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;

public class DetalleReservaActivity extends BaseActivity {

    @BindView(R.id.lblTitulo)  TextView mLblTitulo;
    private DetalleReserva mDetalleReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reserva);
        ButterKnife.bind(this);

        mDetalleReserva = (DetalleReserva) IntentHelper.getObjectForKey(DetalleReservas.TAG);

        mLblTitulo.setText(mDetalleReserva.getNombreParque());

    }
}
