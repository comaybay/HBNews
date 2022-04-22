package com.cmb.hbnews

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import userData

class userAdapter(private val userDatalist:ArrayList<userData>):RecyclerView.Adapter<userAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userAdapter.MyViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_data,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: userAdapter.MyViewHolder, position: Int) {
       val data = userDatalist[position]
        holder.title.text = data.title
        holder.descriptionlayout.text = data.description
        holder.news_date.text = data.date
        holder.timestamp.text = data.timestamp

        Picasso.get().load(data.newsImage)
            .placeholder(R.drawable.ic_image_not_found)
            .into(holder.img_view)
        //
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, reading_news::class.java)
            intent.putExtra("title",holder.title.text)
            intent.putExtra("description",holder.descriptionlayout.text)
            intent.putExtra("newsImage",data.newsImage)
            intent.putExtra("date",holder.news_date.text)
            intent.putExtra("newsUrl",data.newsUrl)
            intent.putExtra("newsSource",data.newsSource)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userDatalist.size
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val title:TextView = itemView.findViewById(R.id.title)
            val descriptionlayout:TextView = itemView.findViewById(R.id.description)
            val news_date:TextView = itemView.findViewById(R.id.news_date)
            val timestamp:TextView = itemView.findViewById(R.id.timestamp)
            val delete_img:ImageView = itemView.findViewById(R.id.delete)
            val img_view:ImageView = itemView.findViewById(R.id.image_view)


    }
}