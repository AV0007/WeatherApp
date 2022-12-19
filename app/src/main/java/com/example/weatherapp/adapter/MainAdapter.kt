package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ForcastItemBinding
import com.example.weatherapp.model.Hour
import com.example.weatherapp.util.Util
import kotlin.collections.ArrayList

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var weatherUpdatesList = mutableListOf<Hour>()

    /*  //updating the latest weather data in list */
    fun setWeatherData(weatherData: ArrayList<Hour>) {
        this.weatherUpdatesList = weatherData.toMutableList()
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForcastItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = weatherUpdatesList[position]
        Glide.with(holder.itemView.context).load("http:".plus(data.condition?.icon))
            .error(R.drawable.ic_launcher_foreground).into(holder.binding.ivForcast)
        holder.binding.tvTime.text = data.time?.let { Util.formatTime(it) }
        holder.binding.tvTemp.text = data.tempC?.let { "${data.tempC?.toInt()}${Util.degreeCelcius()}" }


    }

    override fun getItemCount(): Int {
        return weatherUpdatesList.size

    }

    class ViewHolder(val binding: ForcastItemBinding) : RecyclerView.ViewHolder(binding.root)
}
