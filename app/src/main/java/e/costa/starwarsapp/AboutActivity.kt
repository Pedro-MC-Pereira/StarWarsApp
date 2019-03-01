package e.costa.starwarsapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import mehdi.sakout.aboutpage.AboutPage

/**
 * Created by costa on 01/03/2019.
 */

class AboutActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //create an standard "about page" using medyo about page library
        val aboutPage = AboutPage(this)
                .isRTL(false)
                .setDescription("This app was developed by Pedro Pereira, born on August 30, 1991")
                .addGroup("Contacts")
                .addEmail("costapereira.eeic@gmail.com", "Send email")
                .addGroup("Connect with us")
                .addFacebook("https://www.facebook.com/", "Follow on Facebook")
                .create()

        setContentView(aboutPage)
    }
}