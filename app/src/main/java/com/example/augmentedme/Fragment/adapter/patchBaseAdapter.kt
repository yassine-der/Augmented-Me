package com.example.augmentedme.Fragment.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.augmentedme.Model.Patch
import com.example.augmentedme.R
import com.example.augmentedme.api.RetrofiteInstance

class patchBaseAdapter (var context: Context, var arrayList: List<Patch>): BaseAdapter() {
    override fun getCount(): Int {
        return  arrayList.size
    }

    override fun getItem(position: Int): Any {
        return  arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View = View.inflate(context, R.layout.cart_patch,null)

        var logo : ImageView = view.findViewById(R.id.imageEquiperecycle88)
        var nom: TextView =view.findViewById(R.id.nomEquipeRecycle88)

        var equipe : Patch = arrayList.get(position)

        nom.text = equipe.nom
        val replaced = equipe.image!!.replace("\\", "/")

        Glide.with(view).load(RetrofiteInstance.BASE_URL + replaced).into(logo)

        return  view
    }
}