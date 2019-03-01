package e.costa.starwarsapp

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_character_details.*

/**
 * Created by costa on 28/02/2019.
 */

class CharacterDetailsActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        val charName = intent.getStringExtra("charName")
        id_tvNameDetails.text = charName
        val charGender = intent.getStringExtra("charGender")
        id_tvGenderDetails.text = charGender
        val charPlanet = intent.getStringExtra("charPlanet")
        id_tvHomeworldDetails.text = charPlanet
        val charSkinColor = intent.getStringExtra("charSkinColor")
        id_tvSkincolorDetails.text = charSkinColor
        val charVehicles = intent.getStringExtra("charVehicles")
        id_tvVehiclesDetails.text = charVehicles

        id_btnGoogleSearch.setOnClickListener{
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, charName)
            startActivity(intent);
        }

    }
}