package e.costa.starwarsapp.swAPI

import android.net.Uri
import com.google.gson.GsonBuilder
import e.costa.starwarsapp.swModels.Character
import e.costa.starwarsapp.swModels.SpecieApp
import e.costa.starwarsapp.swModels.VehicleApp
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by costa on 27/02/2019.
 */

class  SWapiInit{

    val service: SWapi

    init{
        val logWeb = HttpLoggingInterceptor()
        logWeb.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logWeb)

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://swapi.co/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()

        service = retrofit.create<SWapi>(SWapi::class.java)
    }

    //get all the data from one page (by character)
    fun loadCharactersSecond(i: Int) : Observable<Character>{
        return service.getCharactersByPage(i)
                .flatMap { charactersResult -> Observable.fromIterable(charactersResult.results)}
                .flatMap { character ->
                    Observable.zip(
                            Observable.just(Character(character.name,
                                    character.gender,
                                    character.homeworld,
                                    ArrayList<SpecieApp>(),
                                    character.skin_color,
                                    ArrayList<VehicleApp>())),
                            Observable.fromIterable(character.species)
                                    .flatMap { specieURL ->
                                        service.getSpecie(Uri.parse(specieURL).lastPathSegment)
                                    }.flatMap { specieFinal ->
                                        Observable.just(SpecieApp(specieFinal.name))
                                    }.toList().toObservable(),
                            Observable.fromIterable(character.vehicles)
                                    .flatMap { vehicleURL ->
                                        service.getVehicles(Uri.parse(vehicleURL).lastPathSegment)
                                    }.flatMap { vehicleFinal ->
                                        Observable.just(VehicleApp(vehicleFinal.name))
                                    }.toList().toObservable(),
                            Function3{ finalChar: Character,
                                       finalSpecie: MutableList<SpecieApp>,
                                       finalVehicle: MutableList<VehicleApp> ->
                                finalChar.species.addAll(finalSpecie)
                                finalChar.vehicles.addAll(finalVehicle)
                                finalChar
                            }
                    )
                }
    }

    //get homeworld name
    fun loadCharactersHome(i: Int) : Observable<Character>{
        return service.getCharactersByPage(i)
                .flatMap { charactersResult -> Observable.fromIterable(charactersResult.results)}
                .flatMap { character ->
                    Observable.zip(
                            Observable.just(Character(character.name,
                                    character.gender,
                                    character.homeworld,
                                    ArrayList<SpecieApp>(),
                                    character.skin_color,
                                    ArrayList<VehicleApp>())),
                            Observable.just(character.homeworld)
                                    .flatMap { homeworldURL ->
                                        service.getHomeworld(Uri.parse(homeworldURL).lastPathSegment)
                                    }.flatMap { homeworldFinal ->
                                        Observable.just((homeworldFinal))
                                    },
                            BiFunction {finalChar: Character,
                                        finalHomeworld: String ->
                                finalChar.homeworld = finalHomeworld
                                finalChar
                            })
                }


    }

    //load first page with characters count
    fun loadCharacters() : Observable<People> {
        return service.getCharaters() //87 chars
    }

    //Observable concatMap to get all pages but was to slow -> function implemented in MainActivity
    /*
    fun loadCharactersTest(i: Int) : Observable<Character> {
        return service.getCharaters(i)
                .flatMap { Observable.range(1,9)
                        .concatMap { pageNum -> service.getCharaters(pageNum)
                                .flatMap { charactersResult -> Observable.fromIterable(charactersResult.results)}
                                .flatMap { character ->
                                    Observable.zip(
                                            Observable.just(Character(character.name,
                                                    character.gender,
                                                    character.homeworld,
                                                    ArrayList<SpecieApp>(),
                                                    character.skin_color,
                                                    ArrayList<VehicleApp>())),
                                            Observable.fromIterable(character.species)
                                                    .flatMap { specieURL ->
                                                        service.getSpecie(Uri.parse(specieURL).lastPathSegment)
                                                    }.flatMap { specieFinal ->
                                                        Observable.just(SpecieApp(specieFinal.name))
                                                    }.toList().toObservable(),
                                            Observable.fromIterable(character.vehicles)
                                                    .flatMap { vehicleURL ->
                                                        service.getVehicles(Uri.parse(vehicleURL).lastPathSegment)
                                                    }.flatMap { vehicleFinal ->
                                                        Observable.just(VehicleApp(vehicleFinal.name))
                                                    }.toList().toObservable(),
                                            Function3{ finalChar: Character,
                                                       finalSpecie: MutableList<SpecieApp>,
                                                       finalVehicle: MutableList<VehicleApp> ->
                                                finalChar.species.addAll(finalSpecie)
                                                finalChar.vehicles.addAll(finalVehicle)
                                                finalChar
                                            }
                                    )
                                }}
                }

    }
    */
}
