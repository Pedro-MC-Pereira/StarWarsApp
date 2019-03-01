package e.costa.starwarsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import e.costa.starwarsapp.swAPI.SWapiInit
import e.costa.starwarsapp.swModels.Character
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    var characters = mutableListOf<Character>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutmanager = GridLayoutManager(this,2) //layout of the recyclerview
        layoutmanager.orientation = GridLayoutManager.VERTICAL
        idRVCharacters.layoutManager = layoutmanager

        val adapter = CharactersAdpater(this, characters)
        idRVCharacters.adapter  = adapter

        downloadChars(adapter) //call the api using async threads
    }

    fun downloadChars(adapter: CharactersAdpater){
        val dialog = indeterminateProgressDialog("Searching for characters in all the Star Wars universe!")
        doAsync {
            val api1 = SWapiInit();
            api1.loadCharacters() //get characters number
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ charactersResult ->
                        dialog.show()
                        var charsNumber: Int = charactersResult.count.toInt()
                        var pagesNumbers: Int
                        pagesNumbers = (charsNumber / 10).toInt() //calculate how many pages and round it up
                        if (charsNumber.rem(pagesNumbers) < 0.5) {
                            pagesNumbers++
                        }
                        doAsync {
                        for (x in 1..pagesNumbers) {
                            api1.loadCharactersSecond(x)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ finalChar ->
                                        characters?.add(finalChar) //add to adapter
                                    }, { e ->
                                        e.printStackTrace()
                                    }, {
                                        adapter.notifyDataSetChanged()
                                        if(x >= pagesNumbers){
                                            dialog.dismiss()
                                        }
                                    }
                                    )
                            }
                        }
                    }, { e ->
                        e.printStackTrace()
                    }, {
                    }
                    )
        }
    }
}
