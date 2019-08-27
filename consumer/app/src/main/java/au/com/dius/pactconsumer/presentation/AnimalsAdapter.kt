package au.com.dius.pactconsumer.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import au.com.dius.pactconsumer.R
import au.com.dius.pactconsumer.model.Animal

class AnimalsAdapter(
    private val animals: List<Animal>
) : RecyclerView.Adapter<AnimalsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_animal,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (name, image) = animals[position]
        holder.titleView.text = name
        val context = holder.itemView.context
        val id =
            holder.itemView.context.resources.getIdentifier(image, "drawable", context.packageName)
        holder.imageView.setImageDrawable(context.resources.getDrawable(id, null))
    }

    override fun getItemCount(): Int {
        return animals.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById<View>(R.id.img_animal) as ImageView
        val titleView: TextView = itemView.findViewById<View>(R.id.txt_title) as TextView
    }
}
