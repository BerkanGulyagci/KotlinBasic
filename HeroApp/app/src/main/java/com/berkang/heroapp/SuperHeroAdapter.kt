package com.berkang.heroapp
import android.graphics.Color

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.berkang.heroapp.databinding.RecyclerRowBinding

// constructor tanımı düzeltildi (önce ":" vardı, artık "()" ile çağrılıyor)
class SuperHeroAdapter(private val superHeroList: ArrayList<SuperHero>) :
    RecyclerView.Adapter<SuperHeroAdapter.SuperHeroViewHolder>() {

    // ViewHolder tanımı
    class SuperHeroViewHolder(val binding: RecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    // XML layout'u şişir ve ViewHolder döndür
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuperHeroViewHolder(binding)
    }

    // Kaç öğe gösterilecek
    override fun getItemCount(): Int {
        return superHeroList.size
    }

    // Her satıra veriyi bağla
    override fun onBindViewHolder(holder: SuperHeroViewHolder, position: Int) {
        holder.binding.textViewRecyclerView.text = superHeroList[position].name

        // Her kartın arka plan rengi
        val colors = listOf(
            Color.parseColor("#FFCDD2"), // Pembe
            Color.parseColor("#BBDEFB"), // Açık mavi
            Color.parseColor("#C8E6C9"), // Açık yeşil
            Color.parseColor("#FFF9C4")  // Sarı
        )
        holder.itemView.setBackgroundColor(colors[position % colors.size])

        // Tıklanınca intent ile veri gönder
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DefinitionActivity::class.java)
            intent.putExtra("selectedHero", superHeroList[position])
            holder.itemView.context.startActivity(intent)
        }
    }
    }
