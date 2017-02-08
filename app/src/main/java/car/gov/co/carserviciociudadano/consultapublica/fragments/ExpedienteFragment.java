package car.gov.co.carserviciociudadano.consultapublica.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.consultapublica.adapter.DocumentosRespuestaAdapter;
import car.gov.co.carserviciociudadano.consultapublica.adapter.IntegrantesExpedienteAdapter;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Expedientes;
import car.gov.co.carserviciociudadano.consultapublica.interfaces.IExpediente;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;
import car.gov.co.carserviciociudadano.consultapublica.model.ExpedienteResumen;
import car.gov.co.carserviciociudadano.consultapublica.model.Integrante;
import car.gov.co.carserviciociudadano.consultapublica.model.RadicadosDigitalesRespuesta;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.fragments.BaseFragment;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class ExpedienteFragment extends BaseFragment {
    @BindView(R.id.lblNumero)    TextView lblNumero;
    @BindView(R.id.lblProceso)    TextView lblProceso;
    @BindView(R.id.fragmentExpediente)    View fragmentExpediente;
    @BindView(R.id.progressView) ProgressBar progressView;
    @BindView(R.id.lblFechaCreacion)    TextView lblFechaCreacion;
    @BindView(R.id.lblActividad)    TextView lblActividad;
    @BindView(R.id.lblUltimoActoAdmin)    TextView lblUltimoActoAdmin;
    @BindView(R.id.lblTituloProceso)    TextView lblTituloProceso;
    @BindView(R.id.lblVigenciaProceso)    TextView lblVigenciaProceso;
    @BindView(R.id.lblCostoProyecto)    TextView lblCostoProyecto;
    @BindView(R.id.lblCuadernos)    TextView lblCuadernos;
    @BindView(R.id.lblAnexos)    TextView lblAnexos;
    @BindView(R.id.recyclerIntegrantes)  RecyclerView recyclerIntegrantes;
    @BindView(R.id.lblIntegrantesTitulo)  TextView lblIntegrantesTitulo;
    @BindView(R.id.lyDatos)  View lyDatos;


    private OnFragmentInteractionListener mListener;
    private int mIdExpediente;
    IntegrantesExpedienteAdapter mAdaptadorIntegrantes;
    List<Integrante> mLstIntegrantes;
    public ExpedienteFragment() {

    }


    public static ExpedienteFragment newInstance(int idExpediente) {
        ExpedienteFragment fragment = new ExpedienteFragment();
        Bundle args = new Bundle();
        args.putInt(Expediente.ID_EXPEDIENTE, idExpediente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdExpediente = getArguments().getInt(Expediente.ID_EXPEDIENTE,0);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expediente, container, false);

        ButterKnife.bind(this, view);
        mLstIntegrantes = new ArrayList<>();
        mAdaptadorIntegrantes = new IntegrantesExpedienteAdapter(mLstIntegrantes);

        recyclerIntegrantes.setAdapter(mAdaptadorIntegrantes);
        recyclerIntegrantes.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerIntegrantes.setItemAnimator(new DefaultItemAnimator());

        obtenerExpediente();
        return  view;
    }
    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Expedientes.TAG);
        super.onPause();

    }
   private void obtenerExpediente(){
       lyDatos.setVisibility(View.GONE);
       showProgress(progressView,true);
       new Expedientes().list(mIdExpediente, new IExpediente() {
           @Override
           public void onSuccess(List<Expediente> lstExpediente) {

           }

           @Override
           public void onSuccess(ExpedienteResumen expedienteResumen) {
               showProgress(progressView,false);
               lyDatos.setVisibility(View.VISIBLE);
               bindData(expedienteResumen);
           }

           @Override
           public void onError(ErrorApi error) {
               lyDatos.setVisibility(View.GONE);
               showProgress(progressView,false);

               Snackbar.make(fragmentExpediente, error.getMessage(), Snackbar.LENGTH_INDEFINITE)
                       //.setActionTextColor(Color.CYAN)
                       .setActionTextColor(ContextCompat.getColor(getContext(), R.color.green) )
                       .setAction("REINTENTAR", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               obtenerExpediente();
                           }
                       })
                       .show();
           }
       });
   }

    private void bindData(ExpedienteResumen item){
        lblNumero.setText(getString(R.string.expediente_nro) +" " +item.getIDExpediente());
        lblProceso.setText(item.getProceso());
        lblFechaCreacion.setText(item.getFechaCreacion());
        lblActividad.setText(item.getActividad());
        lblUltimoActoAdmin.setText(item.getUltimoActoAdmin());
        lblTituloProceso.setText(item.getTituloProceso());
        lblVigenciaProceso.setText(item.getVigenciaProceso());
        lblCostoProyecto.setText(item.getCostoProyecto());
        lblAnexos.setText(item.getAnexos());
        lblCuadernos.setText(item.getCuadernos());

        lblIntegrantesTitulo.setText(item.getIntegrantesTitulo().toUpperCase());

        if (item.getPredios().size() == 1 && item.getIntegrantes().size() > 1)
        {

        }
        else if (!item.isNoManejaPredio())
        {
           mLstIntegrantes.clear();
            mLstIntegrantes.addAll(item.getIntegrantes());
            mAdaptadorIntegrantes.notifyDataSetChanged();
        }
        else
        {
          //  this.lblTitlePredio.Text = "Lugar donde se encontró la afectación";
           //his.ObtenerPredio();


        }



    }




    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
