package com.example.myweatherbase.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherbase.R;
import com.example.myweatherbase.base.ImageDownloader;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrediccionAdapter extends RecyclerView.Adapter<PrediccionAdapter.ViewHolder> {

    private Prediccion prediccion;
    private final LayoutInflater layoutInflater;
    private final OnItemListener oNL;

    public PrediccionAdapter(Context context, OnItemListener oNL) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.oNL = oNL;
    }

    public void setPrediccion(Prediccion prediccion) {
        this.prediccion = prediccion;
    }


    @NonNull
    @Override
    public PrediccionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view, oNL);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setPos(position);
        holder.descripcion.setText(Tools.primeraMayu(prediccion.list.get(position).weather.get(0).description));
        String aux = String.valueOf(prediccion.list.get(position).main.temp_max);
        holder.max.setText(holder.quitarPuntosGrados(aux)+"º");
        if (prediccion.list.get(position).rain!=null)
            holder.rain.setText(prediccion.list.get(position).rain.lluvia+"mm");
        else
            holder.rain.setText("0%");
        ImageDownloader.downloadImage(prediccion.list.get(position).weather.get(0).icon, holder.image);

        Date date = new Date((long) prediccion.list.get(position).dt * 1000);
        SimpleDateFormat dateDayOfWeek = new SimpleDateFormat("EEEE");
        SimpleDateFormat dateGenersl = new SimpleDateFormat(" MMM d, ''yy");
        SimpleDateFormat dateHora = new SimpleDateFormat("h:mm a");
        holder.dia.setText((dateDayOfWeek.format(date)).toUpperCase());
        holder.fecha.setText((dateGenersl.format(date)).substring(1));
        holder.hora.setText((dateHora.format(date)));
    }

    @Override
    public int getItemCount() {
        return prediccion.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dia, descripcion, fecha, max, rain, hora;
        private ImageView image;
        private int pos;
        OnItemListener holderListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            dia = itemView.findViewById(R.id.diaSemana);
            hora = itemView.findViewById(R.id.hora);
            descripcion = itemView.findViewById(R.id.desc);
            fecha = itemView.findViewById(R.id.fecha);
            max = itemView.findViewById(R.id.temp);
            rain = itemView.findViewById(R.id.rain);
            holderListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String quitarPuntosGrados(String str) {
            String aux;
            int posPunto = 0;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '.')
                    posPunto = i;
            }
            if (posPunto == 0)
                posPunto = str.length();
            aux = str.substring(0, posPunto);
            return aux;
        }

        @Override
        public void onClick(View view) {
            holderListener.onItemClick(getAdapterPosition());
        }
    }
}
