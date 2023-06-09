package com.example.myweatherbase.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherbase.R;

import java.util.List;

public class LugaresGuardadosAdapter extends RecyclerView.Adapter<LugaresGuardadosAdapter.ViewHolder> {

    private CityRepository cityRepository;
    private List<CiudadGuardada> ciudades;
    private final LayoutInflater layoutInflater;
    private final OnItemListener onItemListener;

    public LugaresGuardadosAdapter(Context context, OnItemListener onItemListener) {
        cityRepository = CityRepository.getInstance();
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onItemListener = onItemListener;
    }

    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    @NonNull
    @Override
    public LugaresGuardadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.mini_cardview, parent, false);
        return new ViewHolder(view, this.onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CiudadGuardada c = cityRepository.getCiudad(position);
        holder.ciudad.setText(c.name);
        holder.estado.setText(c.state);
        holder.pais.setText(c.country);
    }

    @Override
    public int getItemCount() {
        return cityRepository.getSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView ciudad, pais, estado;
        private OnItemListener holderListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            ciudad = itemView.findViewById(R.id.miniCardCiudad);
            estado = itemView.findViewById(R.id.miniCardEstado);
            pais = itemView.findViewById(R.id.miniCardPais);
            holderListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            holderListener.onItemClick(getAdapterPosition());
        }
    }
}
