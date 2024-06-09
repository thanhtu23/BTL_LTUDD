package com.example.greenfresh.adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenfresh.R

class PhotoAdapter(var context: Context,var mListPhoto : List<String>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_photo)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        var view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_banner,parent,false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        var photo : String = mListPhoto.get(position)
        if(photo ==null){
            return
        }

        Glide.with(context).load(photo).into(holder.imgPhoto)
    }

    override fun getItemCount(): Int {
        if(mListPhoto != null){
            return mListPhoto.size
        }
        return 0
    }
}