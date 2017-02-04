package car.gov.co.carserviciociudadano.consultapublica.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.consultapublica.model.Expediente;


public class DocumentosExpedienteFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DocumentosExpedienteFragment() {

    }


    public static DocumentosExpedienteFragment newInstance(int idExpediente) {
        DocumentosExpedienteFragment fragment = new DocumentosExpedienteFragment();
        Bundle args = new Bundle();
        args.putInt(Expediente.ID_EXPEDIENTE, idExpediente);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int idExpediente = getArguments().getInt(Expediente.ID_EXPEDIENTE,0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_documentos_expediente, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
