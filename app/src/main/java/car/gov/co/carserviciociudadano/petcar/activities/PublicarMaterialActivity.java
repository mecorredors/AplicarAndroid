package car.gov.co.carserviciociudadano.petcar.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.common.BaseActivity;

import car.gov.co.carserviciociudadano.petcar.adapter.MaterialRecogidoAdapter;
import car.gov.co.carserviciociudadano.petcar.dataaccess.MaterialesRecogidos;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;

public class PublicarMaterialActivity extends BaseActivity {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;


    MaterialRecogidoAdapter mAdaptador;
    List<MaterialRecogido> mLstMaterialRecogido = new ArrayList<>();

    public static final int REQUEST_REGISTRAR_KILOS  = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_material);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mLstMaterialRecogido.addAll(new MaterialesRecogidos().list());

        mAdaptador = new MaterialRecogidoAdapter(mLstMaterialRecogido);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdaptador.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = recyclerView.getChildAdapterPosition(v);
            MaterialRecogido materialRecogido = mLstMaterialRecogido.get(position);

            Intent i = new Intent(PublicarMaterialActivity.this, RegistrarKilosYFotosMaterialActivity.class);
            i.putExtra(MaterialRecogido.ID, materialRecogido.Id);
            startActivityForResult(i,REQUEST_REGISTRAR_KILOS);

        }
    };
}
