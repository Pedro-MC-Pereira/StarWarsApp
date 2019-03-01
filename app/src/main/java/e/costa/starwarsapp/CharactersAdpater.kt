package e.costa.starwarsapp

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import e.costa.starwarsapp.swAPI.SWapiInit
import e.costa.starwarsapp.swModels.Character
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog

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
        var planetName: String = ""
        //define which values to pass on item click
        init{
            itemView.setOnClickListener{
                val dialog = context.indeterminateProgressDialog("Getting details!")
                dialog.show()
                val api1 = SWapiInit();
                api1.loadCharactersHome(currentCharacter!!.homeworld) //get characters number
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ homeworldResult ->
                            planetName = homeworldResult.name

                            val intent = Intent(itemView.context, CharacterDetailsActivity::class.java)
                            intent.putExtra("charName", currentCharacter!!.name)
                            intent.putExtra("charGender", currentCharacter!!.gender)
                            if(planetName == ""){
                                planetName = "No planet"
                            }
                            intent.putExtra("charPlanet", planetName)
                            intent.putExtra("charSkinColor", currentCharacter!!.skin_color)

                            var vehiclesString: String = ""
                            if(currentCharacter!!.vehicles.count() > 0) {
                                for(x in 0 until (currentCharacter!!.vehicles.count()))
                                {
                                    vehiclesString = currentCharacter!!.vehicles[x].name + " \n " + vehiclesString
                                }
                                intent.putExtra("charVehicles", vehiclesString)
                            }
                            else{
                                intent.putExtra("charVehicles", "None")
                            }

                            dialog.dismiss()
                            itemView.context.startActivity(intent)
                        }, { e ->
                            e.printStackTrace()
                        }, {
                        }
                        )

                //Toast.makeText(context, currentCharacter!!.name + "clicked", Toast.LENGTH_SHORT).show()

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