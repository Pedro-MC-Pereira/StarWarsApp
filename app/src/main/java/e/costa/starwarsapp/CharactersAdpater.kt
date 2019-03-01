package e.costa.starwarsapp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import e.costa.starwarsapp.swModels.Character
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * Created by costa on 28/02/2019.
 */

//create a recycler view adapter and inflate it with the list_item designed

class CharactersAdpater (val context: Context, val charactersList: MutableList<Character>) : RecyclerView.Adapter<CharactersAdpater.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return charactersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val character = charactersList[position]
        holder.setData(character, position)
    }

    inner class MyViewHolder(itemView: View, var character: Character? = null) : RecyclerView.ViewHolder(itemView){

        var currentCharacter: Character? = null
        var currentPosition: Int = 0

        //define which values to pass on item click
        init{
            itemView.setOnClickListener{
                //Toast.makeText(context, currentCharacter!!.name + "clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(itemView.context, CharacterDetailsActivity::class.java)
                intent.putExtra("charName", currentCharacter!!.name)
                intent.putExtra("charGender", currentCharacter!!.gender)
                intent.putExtra("charPlanet", currentCharacter!!.homeworld)
                intent.putExtra("charSkinColor", currentCharacter!!.skin_color)

                var vechilesString: String = ""
                if(currentCharacter!!.vehicles.count() > 0) {
                    for(x in 0 until (currentCharacter!!.vehicles.count()))
                    {
                        vechilesString = currentCharacter!!.vehicles[x].name + " \n " + vechilesString
                    }
                    intent.putExtra("charVehicles", vechilesString)
                }
                else{
                    intent.putExtra("charVehicles", "None")
                }
                itemView.context.startActivity(intent)
            }
        }

        fun setData(character: Character?, pos: Int ){
            itemView!!.id_tvName.text = character!!.name
            if(character.species.count() > 0) {
                itemView!!.id_tvSpecie.text = character!!.species[0].name
            }
            else{
                itemView!!.id_tvSpecie.text = "No specie"
            }

            itemView!!.id_tvNVehicles.text = character!!.vehicles.count().toString()

            this.currentCharacter = character
            this.currentPosition = pos
        }
    }
}