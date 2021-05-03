package id.example.scanjudul.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.example.scanjudul.Api.Model.DataListjudul;
import id.example.scanjudul.R;
public class AdapterListJudul extends RecyclerView.Adapter<AdapterListJudul.MyViewHolder> {

    public List<DataListjudul> dataListjudul;
    public Context context;
    public String valueJudul;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        TextView tv_judul,tv_tahun,tv_no_buku,tv_nama_mahasiswa,tv_kemiripan_text,tv_kemiripan_angka;
        TextView tv_kalimat_mirip;
        View view_garis;

        public MyViewHolder(@NonNull View view) {
            super(view);
            container = (CardView) view.findViewById(R.id.container);
            tv_judul = (TextView) view.findViewById(R.id.tv_judul);
            tv_tahun = (TextView) view.findViewById(R.id.tv_tahun);
            tv_no_buku = (TextView) view.findViewById(R.id.tv_no_buku);
            tv_kemiripan_text = (TextView) view.findViewById(R.id.tv_kemiripan_text);
            tv_kemiripan_angka = (TextView) view.findViewById(R.id.tv_kemiripan_angka);
            tv_kalimat_mirip = (TextView) view.findViewById(R.id.tv_kalimat_mirip);
            tv_nama_mahasiswa = (TextView) view.findViewById(R.id.tv_nama_mahasiswa);
            view_garis = (View) view.findViewById(R.id.view_garis);
        }
    }

    public AdapterListJudul(List<DataListjudul> list, Context context2, String valueJudul2) {
        dataListjudul = list;
        context = context2;
        valueJudul = valueJudul2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rows_listjudul, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));
        if(valueJudul.isEmpty()) {
            holder.tv_kemiripan_text.setVisibility(View.GONE);
            holder.tv_kemiripan_angka.setVisibility(View.GONE);
            holder.view_garis.setVisibility(View.GONE);
            holder.tv_kalimat_mirip.setVisibility(View.GONE);
        }else{
            if(dataListjudul.get(position).getKemiripan() > 0) {
                holder.tv_kemiripan_text.setVisibility(View.VISIBLE);
                holder.tv_kemiripan_angka.setVisibility(View.VISIBLE);
                holder.view_garis.setVisibility(View.VISIBLE);
                holder.tv_kalimat_mirip.setVisibility(View.VISIBLE);
                holder.tv_kemiripan_angka.setText(String.valueOf(dataListjudul.get(position).getKemiripan() + "%"));
                holder.tv_kalimat_mirip.setText(Html.fromHtml(dataListjudul.get(position).getKata_mirip()));
            }else{
                holder.tv_kemiripan_text.setVisibility(View.GONE);
                holder.tv_kemiripan_angka.setVisibility(View.GONE);
                holder.view_garis.setVisibility(View.GONE);
                holder.tv_kalimat_mirip.setVisibility(View.GONE);
            }
        }
        holder.tv_judul.setText(dataListjudul.get(position).getJudul_skripsi());
        holder.tv_tahun.setText(dataListjudul.get(position).getTahun());
        holder.tv_no_buku.setText(dataListjudul.get(position).getNo_buku());
        holder.tv_nama_mahasiswa.setText(dataListjudul.get(position).getNama_mahasiswa());
    }

    public int getItemCount() {
        return dataListjudul.size();
    }

    public void updateDataJadwal(List<DataListjudul> list) {
        dataListjudul.clear();
        dataListjudul.addAll(list);
        notifyDataSetChanged();
    }

    public void addDataJudul(List<DataListjudul> list) {
        for (DataListjudul add : list) {
            dataListjudul.add(add);
        }
        notifyDataSetChanged();
    }
        
}
