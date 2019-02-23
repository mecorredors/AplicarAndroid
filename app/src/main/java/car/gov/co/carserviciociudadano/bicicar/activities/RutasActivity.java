package car.gov.co.carserviciociudadano.bicicar.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.bicicar.adapter.RutasUsuariosAdapter;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Ruta;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewRutas;
import car.gov.co.carserviciociudadano.bicicar.presenter.RutasPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class RutasActivity extends BaseActivity implements  IViewRutas, View.OnClickListener {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.cheMisRutas) CheckBox cheMisRutas;

    RutasUsuariosAdapter mAdaptador;
    List<Ruta> mLstRutas = new ArrayList<>();
    Beneficiario mBeneficiarioLogin;
    RutasPresenter rutasPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        bar.setDisplayHomeAsUpEnabled(true);
     //   if (mBeneficiarioLogin != null)
       //     bar.setTitle( mBeneficiarioLogin.Nombres + " " + mBeneficiarioLogin.Apellidos);

        recyclerView.setHasFixedSize(true);
        mAdaptador = new RutasUsuariosAdapter(mLstRutas);
        mAdaptador.setOnClickListener(this);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        rutasPresenter = new RutasPresenter(this);
        obtenerRutas();

        cheMisRutas.setOnCheckedChangeListener(onCheckedChangeListener);
        if (mBeneficiarioLogin == null) {
            cheMisRutas.setVisibility(View.GONE);
        }
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            obtenerRutas();
        }
    };

    private void obtenerRutas(){
        if (mBeneficiarioLogin != null) {
            mostrarProgressDialog("Cargando rutas");
            rutasPresenter.getRutas(cheMisRutas.isChecked() ? mBeneficiarioLogin.IDBeneficiario : 0);
        }else{
            mostrarProgressDialog("Cargando rutas");
            rutasPresenter.getRutas(cheMisRutas.isChecked() ? 0 : 0);
        }
    }


    @Override
    public void onSuccess(List<Ruta> lstRutas) {
        ocultarProgressDialog();
        mLstRutas.clear();
        mLstRutas.addAll(lstRutas);
        mAdaptador.notifyDataSetChanged();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(ErrorApi errorApi) {
        ocultarProgressDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(RutasActivity.this);
        builder.setMessage(errorApi.getMessage());

        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obtenerRutas();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        Ruta ruta = mLstRutas.get(position);
        Intent i = new Intent(this, SeguirRutaActivity.class);
        IntentHelper.addObjectForKey(ruta, SeguirRutaActivity.RUTA);
        startActivity(i);
    }


}
