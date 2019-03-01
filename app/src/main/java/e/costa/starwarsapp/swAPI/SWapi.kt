package e.costa.starwarsapp.swAPI

import retrofit2.http.GET
import io.reactivex.Observable
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by costa on 27/02/2019.
 */
interface SWapi {
    @GET("people") //get characters (first page -> use "count")
    fun getCharaters(): Observable<People>

    @GET("people/") //get characters by page
    fun getCharactersByPage(@Query("page") page: Int): Observable<People>

    @GET("people/") //get characteres using the Observable.concatMap (test fase)
    fun getCharactersTest(@Query("page") page: Int): Observable<People>

    @GET("species/{specieID}") //get specie by specie number
    fun getSpecie(@Path("specieID") specieID : String) : Observable<Specie>

    @GET("vehicles/{vehicleID}") //get vehicle by vehicle number
    fun getVehicles(@Path("vehicleID") vehicleID : String) : Observable<Vehicle>

    @GET("planets/{planetID}") //get planet (homeworld) by planet number
    fun getHomeworld(@Path("planetID") planetID : String) : Observable<HomeWorld>


}