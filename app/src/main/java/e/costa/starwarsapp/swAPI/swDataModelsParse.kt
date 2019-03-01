package e.costa.starwarsapp.swAPI

import android.util.MutableChar

/**
 * Created by costa on 27/02/2019.
 */

//data models to parse api data

data class People(val count: String, val next: String, val previous: String, val results: List<Person>)

data class Person(val name: String,
                  val gender: String,
                  val homeworld: String,
                  val species: MutableList<String>,
                  val skin_color: String,
                  val vehicles: List<String>
                  )

data class Specie(val name: String)

data class Vehicle(val name: String)