package e.costa.starwarsapp.swModels

/**
 * Created by costa on 27/02/2019.
 */

//data models to use in the app

data class Character (val name: String,
                      val gender: String,
                      var homeworld: String,
                      val species: MutableList<SpecieApp>,
                      val skin_color: String,
                      val vehicles: MutableList<VehicleApp>
                      )

data class SpecieApp (val name: String)

data class VehicleApp (val name: String){
    override fun toString():String{
        return "${name}"
    }
}
