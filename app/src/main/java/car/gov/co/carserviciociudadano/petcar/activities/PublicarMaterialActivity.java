package car.gov.co.carserviciociudadano.petcar.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.common.BaseActivity;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.adapter.MaterialRecogidoAdapter;
import car.gov.co.carserviciociudadano.petcar.dataaccess.MaterialesRecogidos;
import car.gov.co.carserviciociudadano.petcar.interfaces.IViewAdjuntoPetCar;
import car.gov.co.carserviciociudadano.petcar.model.AdjuntoPetCar;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.presenter.AdjuntoPetCarPresenter;
import car.gov.co.carserviciociudadano.petcar.presenter.IViewMaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.presenter.MaterialRecogidoPresenter;

public class PublicarMaterialActivity extends BaseActivity  implements IViewMaterialRecogido, IViewAdjuntoPetCar {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.rbPendiente) RadioButton rbPendiente;
    @BindView(R.id.rbPublicado) RadioButton rbPublicado;


    MaterialRecogidoAdapter mAdaptador;
    List<MaterialRecogido> mLstMaterialRecogido = new ArrayList<>();

    public static final int REQUEST_REGISTRAR_KILOS  = 100;

    MaterialRecogidoPresenter materialRecogidoPresenter;
    AdjuntoPetCarPresenter adjuntoPetCarPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_material);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        mAdaptador = new MaterialRecogidoAdapter(mLstMaterialRecogido);
        recyclerView.setAdapter(mAdaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdaptador.setOnClickListener(onClickListener);
        adjuntoPetCarPresenter = new AdjuntoPetCarPresenter(this);
        obtenerMaterial();

    }

      @OnClick({R.id.rbPendiente, R.id.rbPublicado}) void onRadioButtonClick(){
          obtenerMaterial();
      }

    private  void obtenerMaterial(){

        String where = "";
        if (rbPendiente.isChecked()){
            where = MaterialRecogido.ESTADO + " = " + Enumerator.Estado.PENDIENTE_PUBLICAR + " or " + MaterialRecogido.ESTADO + " = " + Enumerator.Estado.PENDIENTE_PUBLICAR_FOTOS;
        }else{
            where =  MaterialRecogido.ESTADO + " = " + Enumerator.Estado.PUBLICADO;
        }

        mLstMaterialRecogido.clear();
        mLstMaterialRecogido.addAll(new MaterialesRecogidos().list(where));
        mAdaptador.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_REGISTRAR_KILOS){
                obtenerMaterial();
            }
        }
    }

    public  void publicarAdjuntosPetcar(){
        List<MaterialRecogido> lstMaterialRecogido = new MaterialesRecogidos().list(Enumerator.Estado.PENDIENTE_PUBLICAR_FOTOS);

        if (lstMaterialRecogido.size() > 0) {
            adjuntoPetCarPresenter.publicarArchivosAdjuntos(lstMaterialRecogido.get(0));
        }else{
            ocultarProgressDialog();
            mostrarMensajeDialog("Material publicado correctamente");
            obtenerMaterial();
        }
    }

    @OnClick(R.id.btnPublicar) void onPublicar(){
        mostrarProgressDialog("Publicando Material");
        materialRecogidoPresenter = new MaterialRecogidoPresenter(this);
        materialRecogidoPresenter.publicar();
    }

    @Override
    public void onSuccessPublicarMaterial() {
        ocultarProgressDialog();
        mostrarProgressDialog("Publicando fotos");
        publicarAdjuntosPetcar();
    }

    @Override
    public void onErrorPublicarMaterial(ErrorApi error) {
        ocultarProgressDialog();
        mostrarMensajeDialog(error.getMessage() + ", No fue posible publicar material");
        obtenerMaterial();
    }

    @Override
    public void onErrorValidacion(String mensaje){
        ocultarProgressDialog();
        mostrarMensajeDialog(mensaje);
    }

    @Override
    public void onSuccessArchivosAdjunto(MaterialRecogido materialRecogido) {
        materialRecogido.Estado = Enumerator.Estado.PUBLICADO;
        if (new MaterialesRecogidos().update(materialRecogido)) {
            publicarAdjuntosPetcar();
        }else {
            onErrorValidacion("Error al guardar localmente despues de publicar adjunto");
        }

    }

    @Override
    public void onErrorArchivoAdjunto(String mensaje) {
        ocultarProgressDialog();
        mostrarMensajeDialog(mensaje);
    }
}
