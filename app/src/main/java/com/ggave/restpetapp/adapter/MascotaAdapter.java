package com.ggave.restpetapp.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggave.restpetapp.R;
import com.ggave.restpetapp.activities.DetailPetActivity;
import com.ggave.restpetapp.model.Mascota;
import com.ggave.restpetapp.service.ApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {
    private static final String TAG = MascotaAdapter.class.getSimpleName();

    private List<Mascota> mascota;

    public MascotaAdapter(){
        this.mascota = new ArrayList<>();
    }

    public void setMascota(List<Mascota> mascota){
        this.mascota = mascota;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView fotoImage;
        TextView nombreText;
        TextView razaText;
        TextView edadText;

        ViewHolder(View itemView) {
            super(itemView);
            fotoImage = itemView.findViewById(R.id.foto_image);
            nombreText = itemView.findViewById(R.id.nombre_text);
            razaText = itemView.findViewById(R.id.raza_text);
            edadText = itemView.findViewById(R.id.edad_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int position) {

        final Mascota mascota = this.mascota.get(position);

        viewHolder.nombreText.setText(mascota.getNombre());
        viewHolder.razaText.setText(mascota.getRaza());
        viewHolder.edadText.setText(mascota.getEdad());

        String url = ApiService.API_BASE_URL + "/mascota/images/" + mascota.getImagen();
        Picasso.with(viewHolder.itemView.getContext()).load(url).into(viewHolder.fotoImage);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), DetailPetActivity.class);
                intent.putExtra("ID", mascota.getId());
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return this.mascota.size();
    }
}
