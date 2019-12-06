package pl.edu.agh.kis.android.ships

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.edu.agh.kis.android.ships.components.Audio
import java.util.*

class GameActivity : AppCompatActivity() {

    // dane
    private val playerShipTable =
        IntArray(100)                                 // tablica statków gracza po lewej stronie
    private val computerShipTable =
        IntArray(100)                               // tablica statków komputera po prawej stronie
    private val shipFieldsShotByPlayer =
        ArrayList<Int>()             // tablica trafionych pól statków przez gracza
    private val shipFieldsShotByPlayerTypes =
        ArrayList<Int>()        // tablica typów trafionych pól statków przez gracza
    private var whoShoots =
        0                                                    // tura gracza czy komputera

    // pola dla AI
    private val aiPlayerCoordinates =
        ArrayList<Int>()                // tabela pól gracza; komputer używa jej w algorytmie strzelania
    private var aiLastDirection =
        -1                                             // ostatni kierunek, w którym ai trafiło pole statku
    private val aiDirections =
        ArrayList<Int>()                       // tablica kierunków, używana do losowego określania kierunku ostrzału ai po trafieniu w część statku
    private var aiLastShotShipField =
        -1                                         // id ostatniego trafionego pola przez komputer
    private var aiFirstShotShipField =
        -1                                        // id pierwszego trafionego pola przez komputer w celach zawracania ostrzału
    private var aiTwoGoodShotsSameDirection =
        -1                                 // w celach zawracania ostrzału bez strzelenia po bokach
    private val aiShipFields =
        ArrayList<Int>()                      // tablica trafionych pól statków przez komputer

    // delay
    var setDelay: Handler = Handler()
    var startDelay: Runnable = Runnable { ai() }

    // sound and vibration player
    private val playSong = Audio()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setDelay = Handler()
        setContentView(R.layout.activity_game)
        findViewById<TableLayout>(R.id.hit_table).visibility = View.GONE

        resetShipTable(playerShipTable)
        shipGenerator(playerShipTable)
        showShips(false, playerShipTable)

        //inicjalizacja aiDirections
        aiDirections.add(0)
        aiDirections.add(1)
        aiDirections.add(2)
        aiDirections.add(3)

        val buttonGenerujPonownie = findViewById<Button>(R.id.generateBoardButton)
        buttonGenerujPonownie.setOnClickListener {
            resetShipTable(playerShipTable)
            shipGenerator(playerShipTable)
            for (i in 0..99) {
                val pole = findViewById<ImageView>(playerFields[i])
                if (playerShipTable[i] == 0) {
                    pole.setImageResource(R.drawable.water_tile)
                } else {
                    pole.setImageResource(R.drawable.ship)
                }
            }
        }

        val buttonRozpocznijGre = findViewById<Button>(R.id.startGameButton)
        buttonRozpocznijGre.setOnClickListener {
            findViewById<Button>(R.id.startGameButton).visibility = View.GONE
            findViewById<Button>(R.id.generateBoardButton).visibility = View.GONE
            findViewById<TableLayout>(R.id.hit_table).visibility = View.VISIBLE
            resetShipTable(computerShipTable)
            shipGenerator(computerShipTable)

            run {
                var i = 0
                while (i < 100) {
                    aiPlayerCoordinates.add(i++)
                }
            }

            for (i in 0..99) {
                val pole = findViewById<ImageView>(computerFields[i])
                pole.setOnClickListener {
                    val id = Integer.parseInt(it.tag as String)
                    val vImageView = it as ImageView

                    if (whoShoots == 0) { //jeżeli tura gracza
                        it.setOnClickListener(null) //po naciśnięciu pola, usuwamy funkcję z tego pola
                        if (computerShipTable[id] == 0) { //jeżeli pudło
                            vImageView.setImageResource(R.drawable.miss_tile)

                            whoShoots = 1
                            playSong.playMiss(this@GameActivity)
//                            startDelay = Runnable { ai() }
                            setDelay?.postDelayed(startDelay, 1250)
                        } else { //jezeli hit
                            shipFieldsShotByPlayer.add(id)
                            shipFieldsShotByPlayerTypes.add(computerShipTable[id])
                            vImageView.setImageResource(R.drawable.hit)

                            val oldId = computerShipTable[id]
                            computerShipTable[id] = -1

                            if (checkIfSunk(
                                    computerShipTable,
                                    oldId
                                )
                            ) { // if ship has been sunk
                                playerRemoveAdjacentFields()
                            }

                            whoShoots = 0
                            playSong.playHit(this@GameActivity)
                        }
                    }
                    if (checkTable(computerShipTable)) {
                        deleteAllOnClicks()
                        Toast.makeText(applicationContext, "Wygrałeś!!!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private val playerFields = intArrayOf(
        R.id.player00,
        R.id.player01,
        R.id.player02,
        R.id.player03,
        R.id.player04,
        R.id.player05,
        R.id.player06,
        R.id.player07,
        R.id.player08,
        R.id.player09,
        R.id.player10,
        R.id.player11,
        R.id.player12,
        R.id.player13,
        R.id.player14,
        R.id.player15,
        R.id.player16,
        R.id.player17,
        R.id.player18,
        R.id.player19,
        R.id.player20,
        R.id.player21,
        R.id.player22,
        R.id.player23,
        R.id.player24,
        R.id.player25,
        R.id.player26,
        R.id.player27,
        R.id.player28,
        R.id.player29,
        R.id.player30,
        R.id.player31,
        R.id.player32,
        R.id.player33,
        R.id.player34,
        R.id.player35,
        R.id.player36,
        R.id.player37,
        R.id.player38,
        R.id.player39,
        R.id.player40,
        R.id.player41,
        R.id.player42,
        R.id.player43,
        R.id.player44,
        R.id.player45,
        R.id.player46,
        R.id.player47,
        R.id.player48,
        R.id.player49,
        R.id.player50,
        R.id.player51,
        R.id.player52,
        R.id.player53,
        R.id.player54,
        R.id.player55,
        R.id.player56,
        R.id.player57,
        R.id.player58,
        R.id.player59,
        R.id.player60,
        R.id.player61,
        R.id.player62,
        R.id.player63,
        R.id.player64,
        R.id.player65,
        R.id.player66,
        R.id.player67,
        R.id.player68,
        R.id.player69,
        R.id.player70,
        R.id.player71,
        R.id.player72,
        R.id.player73,
        R.id.player74,
        R.id.player75,
        R.id.player76,
        R.id.player77,
        R.id.player78,
        R.id.player79,
        R.id.player80,
        R.id.player81,
        R.id.player82,
        R.id.player83,
        R.id.player84,
        R.id.player85,
        R.id.player86,
        R.id.player87,
        R.id.player88,
        R.id.player89,
        R.id.player90,
        R.id.player91,
        R.id.player92,
        R.id.player93,
        R.id.player94,
        R.id.player95,
        R.id.player96,
        R.id.player97,
        R.id.player98,
        R.id.player99
    )

    private val computerFields = intArrayOf(
        R.id.computer00,
        R.id.computer01,
        R.id.computer02,
        R.id.computer03,
        R.id.computer04,
        R.id.computer05,
        R.id.computer06,
        R.id.computer07,
        R.id.computer08,
        R.id.computer09,
        R.id.computer10,
        R.id.computer11,
        R.id.computer12,
        R.id.computer13,
        R.id.computer14,
        R.id.computer15,
        R.id.computer16,
        R.id.computer17,
        R.id.computer18,
        R.id.computer19,
        R.id.computer20,
        R.id.computer21,
        R.id.computer22,
        R.id.computer23,
        R.id.computer24,
        R.id.computer25,
        R.id.computer26,
        R.id.computer27,
        R.id.computer28,
        R.id.computer29,
        R.id.computer30,
        R.id.computer31,
        R.id.computer32,
        R.id.computer33,
        R.id.computer34,
        R.id.computer35,
        R.id.computer36,
        R.id.computer37,
        R.id.computer38,
        R.id.computer39,
        R.id.computer40,
        R.id.computer41,
        R.id.computer42,
        R.id.computer43,
        R.id.computer44,
        R.id.computer45,
        R.id.computer46,
        R.id.computer47,
        R.id.computer48,
        R.id.computer49,
        R.id.computer50,
        R.id.computer51,
        R.id.computer52,
        R.id.computer53,
        R.id.computer54,
        R.id.computer55,
        R.id.computer56,
        R.id.computer57,
        R.id.computer58,
        R.id.computer59,
        R.id.computer60,
        R.id.computer61,
        R.id.computer62,
        R.id.computer63,
        R.id.computer64,
        R.id.computer65,
        R.id.computer66,
        R.id.computer67,
        R.id.computer68,
        R.id.computer69,
        R.id.computer70,
        R.id.computer71,
        R.id.computer72,
        R.id.computer73,
        R.id.computer74,
        R.id.computer75,
        R.id.computer76,
        R.id.computer77,
        R.id.computer78,
        R.id.computer79,
        R.id.computer80,
        R.id.computer81,
        R.id.computer82,
        R.id.computer83,
        R.id.computer84,
        R.id.computer85,
        R.id.computer86,
        R.id.computer87,
        R.id.computer88,
        R.id.computer89,
        R.id.computer90,
        R.id.computer91,
        R.id.computer92,
        R.id.computer93,
        R.id.computer94,
        R.id.computer95,
        R.id.computer96,
        R.id.computer97,
        R.id.computer98,
        R.id.computer99
    )

    private fun deleteAllOnClicks() {
        for (i in 0..99) {
            val view = findViewById<ImageView>(computerFields[i])
            view.setOnClickListener(null)
        }
    }

    private fun resetShipTable(shipTable: IntArray) {
        for (i in 0..99) {
            shipTable[i] = 0
        }
    }

    private fun checkIfFieldIsEmpty(
        id: Int,
        shipTable: IntArray,
        direction: Int
    ): Boolean { //sprawdza czy pola po danej stronie są puste; używana w generacji statków
        var isEmpty = 3
        when (direction) {
            0 //po prawej
            -> {
                if (id - 9 < 0) {
                    --isEmpty
                } else if (shipTable[id - 9] == 0) {
                    --isEmpty
                }
                if (id + 1 > 99) {
                    --isEmpty
                } else if (shipTable[id + 1] == 0) {
                    --isEmpty
                }
                if (id + 11 > 99) {
                    --isEmpty
                } else if (shipTable[id + 11] == 0) {
                    --isEmpty
                }

                if (isEmpty == 0) {
                    return true
                }
            }
            1 //po lewej
            -> {
                if (id + 10 > 99) {
                    --isEmpty
                } else if (shipTable[id + 10] == 0) {
                    --isEmpty
                }
                if (id + 9 > 99) {
                    --isEmpty
                } else if (shipTable[id + 9] == 0) {
                    --isEmpty
                }
                if (id + 11 > 99) {
                    --isEmpty
                } else if (shipTable[id + 11] == 0) {
                    --isEmpty
                }

                if (isEmpty == 0) {
                    return true
                }
            }
            2 //na dole
            -> {
                if (id - 11 < 0) {
                    --isEmpty
                } else if (shipTable[id - 11] == 0) {
                    --isEmpty
                }
                if (id + 9 > 99) {
                    --isEmpty
                } else if (shipTable[id + 9] == 0) {
                    --isEmpty
                }
                if (id - 1 < 0) {
                    --isEmpty
                } else if (shipTable[id - 1] == 0) {
                    --isEmpty
                }

                if (isEmpty == 0) {
                    return true
                }
            }
            3 //u góry
            -> {
                if (id - 11 < 0) {
                    --isEmpty
                } else if (shipTable[id - 11] == 0) {
                    --isEmpty
                }
                if (id - 10 < 0) {
                    --isEmpty
                } else if (shipTable[id - 10] == 0) {
                    --isEmpty
                }
                if (id - 9 < 0) {
                    --isEmpty
                } else if (shipTable[id - 9] == 0) {
                    --isEmpty
                }

                if (isEmpty == 0) {
                    return true
                }
            }
        }
        return false
    }

    private fun is3x3Empty(
        id: Int,
        shipTable: IntArray
    ): Boolean { //sprawdza czy dane 3x3 jest puste, wprowadzone bo jest szybsze od czterech checkIfFieldIsEmpty
        if (shipTable[id] != 0) {
            return false
        }
        if (id - 11 > -1) {
            if (shipTable[id - 11] != 0) {
                return false
            }
        }
        if (id - 10 > -1) {
            if (shipTable[id - 10] != 0) {
                return false
            }
        }
        if (id - 9 > -1) {
            if (shipTable[id - 9] != 0) {
                return false
            }
        }
        if (id - 1 > -1) {
            if (shipTable[id - 1] != 0) {
                return false
            }
        }
        if (id + 1 < 100) {
            if (shipTable[id + 1] != 0) {
                return false
            }
        }
        if (id + 9 < 100) {
            if (shipTable[id + 9] != 0) {
                return false
            }
        }
        if (id + 10 < 100) {
            if (shipTable[id + 10] != 0) {
                return false
            }
        }
        if (id + 11 < 100) {
            if (shipTable[id + 11] != 0) {
                return false
            }
        }
        return true
    }

    private fun generateShip4(shipTable: IntArray, id: Int): Boolean {
        val direction = Random().nextInt(4) //0 prawo, 1 dół, 2 lewo, 3 góra

        if (!is3x3Empty(id, shipTable)) {
            return true
        }

        if (direction == 0) { //w prawo
            val id2 = id + 1
            if (id2 > 99) {
                return true
            }
            if (Math.floor((id2 / 10).toDouble()) == Math.floor((id / 10).toDouble()) && checkIfFieldIsEmpty(
                    id2,
                    shipTable,
                    direction
                )
            ) { //czy 1 w prawo jest ok?
                val id3 = id2 + 1
                if (id3 > 99) {
                    return true
                }
                if (Math.floor((id3 / 10).toDouble()) == Math.floor((id2 / 10).toDouble()) && checkIfFieldIsEmpty(
                        id3,
                        shipTable,
                        direction
                    )
                ) { //czy 2 w prawo jest ok?
                    val id4 = id3 + 1
                    if (id4 > 99) {
                        return true
                    }
                    if (Math.floor((id4 / 10).toDouble()) == Math.floor((id3 / 10).toDouble()) && checkIfFieldIsEmpty(
                            id4,
                            shipTable,
                            direction
                        )
                    ) { //czy 3 w prawo jest ok?
                        shipTable[id] = 41
                        shipTable[id2] = 41
                        shipTable[id3] = 41
                        shipTable[id4] = 41
                        return false
                    }
                }
            }
        } else if (direction == 1) { //w dół
            val id2 = id + 10
            if (id2 > 99) {
                return true
            }
            if (checkIfFieldIsEmpty(id2, shipTable, direction)) { //czy 1 w dół jest ok?
                val id3 = id2 + 10
                if (id3 > 99) {
                    return true
                }
                if (checkIfFieldIsEmpty(id3, shipTable, direction)) { //czy 2 w dół jest ok?
                    val id4 = id3 + 10
                    if (id4 > 99) {
                        return true
                    }
                    if (checkIfFieldIsEmpty(id4, shipTable, direction)) { //czy 3 w dół jest ok?
                        shipTable[id] = 41
                        shipTable[id2] = 41
                        shipTable[id3] = 41
                        shipTable[id4] = 41
                        return false
                    }
                }
            }
        } else if (direction == 2) { //w lewo
            val id2 = id - 1
            if (id2 < 0) {
                return true
            }
            if (Math.floor((id2 / 10).toDouble()) == Math.floor((id / 10).toDouble()) && checkIfFieldIsEmpty(
                    id2,
                    shipTable,
                    direction
                )
            ) { //czy 1 w lewo jest ok?
                val id3 = id2 - 1
                if (id3 < 0) {
                    return true
                }
                if (Math.floor((id3 / 10).toDouble()) == Math.floor((id2 / 10).toDouble()) && checkIfFieldIsEmpty(
                        id3,
                        shipTable,
                        direction
                    )
                ) { //czy 2 w lewo jest ok?
                    val id4 = id3 - 1
                    if (id4 < 0) {
                        return true
                    }
                    if (Math.floor((id4 / 10).toDouble()) == Math.floor((id3 / 10).toDouble()) && checkIfFieldIsEmpty(
                            id4,
                            shipTable,
                            direction
                        )
                    ) { //czy 3 w lewo jest ok?
                        shipTable[id] = 41
                        shipTable[id2] = 41
                        shipTable[id3] = 41
                        shipTable[id4] = 41
                        return false
                    }
                }
            }
        } else { //w góre
            val id2 = id - 10
            if (id2 < 0) {
                return true
            }
            if (checkIfFieldIsEmpty(id2, shipTable, direction)) { //czy 1 w góre jest ok?
                val id3 = id2 - 10
                if (id3 < 0) {
                    return true
                }
                if (checkIfFieldIsEmpty(id3, shipTable, direction)) { //czy 2 w góre jest ok?
                    val id4 = id3 - 10
                    if (id4 < 0) {
                        return true
                    }
                    if (checkIfFieldIsEmpty(id4, shipTable, direction)) { //czy 3 w góre jest ok?
                        shipTable[id] = 41
                        shipTable[id2] = 41
                        shipTable[id3] = 41
                        shipTable[id4] = 41
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun generateShip3(shipTable: IntArray, id: Int, shipNumber: Int): Boolean {
        val direction = Random().nextInt(4) //0 prawo, 1 dół, 2 lewo, 3 góra

        if (!is3x3Empty(id, shipTable)) {
            return true
        }

        if (direction == 0) { //w prawo
            val id2 = id + 1
            if (id2 > 99) {
                return true
            }
            if (Math.floor((id2 / 10).toDouble()) == Math.floor((id / 10).toDouble()) && checkIfFieldIsEmpty(
                    id2,
                    shipTable,
                    direction
                )
            ) { //czy 1 w prawo jest ok?
                val id3 = id2 + 1
                if (id3 > 99) {
                    return true
                }
                if (Math.floor((id3 / 10).toDouble()) == Math.floor((id2 / 10).toDouble()) && checkIfFieldIsEmpty(
                        id3,
                        shipTable,
                        direction
                    )
                ) { //czy 2 w prawo jest ok?
                    shipTable[id] = 30 + shipNumber
                    shipTable[id2] = 30 + shipNumber
                    shipTable[id3] = 30 + shipNumber
                    return false
                }
            }
        } else if (direction == 1) { //w dół
            val id2 = id + 10
            if (id2 > 99) {
                return true
            }
            if (checkIfFieldIsEmpty(id2, shipTable, direction)) { //czy 1 w dół jest ok?
                val id3 = id2 + 10
                if (id3 > 99) {
                    return true
                }
                if (checkIfFieldIsEmpty(id3, shipTable, direction)) { //czy 2 w dół jest ok?
                    shipTable[id] = 30 + shipNumber
                    shipTable[id2] = 30 + shipNumber
                    shipTable[id3] = 30 + shipNumber
                    return false
                }
            }
        } else if (direction == 2) { //w lewo
            val id2 = id - 1
            if (id2 < 0) {
                return true
            }
            if (Math.floor((id2 / 10).toDouble()) == Math.floor((id / 10).toDouble()) && checkIfFieldIsEmpty(
                    id2,
                    shipTable,
                    direction
                )
            ) { //czy 1 w lewo jest ok?
                val id3 = id2 - 1
                if (id3 < 0) {
                    return true
                }
                if (Math.floor((id3 / 10).toDouble()) == Math.floor((id2 / 10).toDouble()) && checkIfFieldIsEmpty(
                        id3,
                        shipTable,
                        direction
                    )
                ) { //czy 2 w lewo jest ok?
                    shipTable[id] = 30 + shipNumber
                    shipTable[id2] = 30 + shipNumber
                    shipTable[id3] = 30 + shipNumber
                    return false
                }
            }
        } else { //w góre
            val id2 = id - 10
            if (id2 < 0) {
                return true
            }
            if (checkIfFieldIsEmpty(id2, shipTable, direction)) { //czy 1 w góre jest ok?
                val id3 = id2 - 10
                if (id3 < 0) {
                    return true
                }
                if (checkIfFieldIsEmpty(id3, shipTable, direction)) { //czy 2 w góre jest ok?
                    shipTable[id] = 30 + shipNumber
                    shipTable[id2] = 30 + shipNumber
                    shipTable[id3] = 30 + shipNumber
                    return false
                }
            }
        }
        return true
    }

    private fun generateShip2(shipTable: IntArray, id: Int, shipNumber: Int): Boolean {
        val direction = Random().nextInt(4) //0 prawo, 1 dół, 2 lewo, 3 góra

        if (!is3x3Empty(id, shipTable)) {
            return true
        }

        if (direction == 0) { //w prawo
            val id2 = id + 1
            if (id2 > 99) {
                return true
            }
            if (Math.floor((id2 / 10).toDouble()) == Math.floor((id / 10).toDouble()) && checkIfFieldIsEmpty(
                    id2,
                    shipTable,
                    direction
                )
            ) { //czy 1 w prawo jest ok?
                shipTable[id] = 20 + shipNumber
                shipTable[id2] = 20 + shipNumber
                return false
            }
        } else if (direction == 1) { //w dół
            val id2 = id + 10
            if (id2 > 99) {
                return true
            }
            if (checkIfFieldIsEmpty(id2, shipTable, direction)) { //czy 1 w dół jest ok?
                shipTable[id] = 20 + shipNumber
                shipTable[id2] = 20 + shipNumber
                return false
            }
        } else if (direction == 2) { //w lewo
            val id2 = id - 1
            if (id2 < 0) {
                return true
            }
            if (Math.floor((id2 / 10).toDouble()) == Math.floor((id / 10).toDouble()) && checkIfFieldIsEmpty(
                    id2,
                    shipTable,
                    direction
                )
            ) { //czy 1 w lewo jest ok?
                shipTable[id] = 20 + shipNumber
                shipTable[id2] = 20 + shipNumber
                return false
            }
        } else { //w góre
            val id2 = id - 10
            if (id2 < 0) {
                return true
            }
            if (checkIfFieldIsEmpty(id2, shipTable, direction)) { //czy 1 w góre jest ok?
                shipTable[id] = 20 + shipNumber
                shipTable[id2] = 20 + shipNumber
                return false
            }
        }
        return true
    }

    private fun generateShip1(shipTable: IntArray, id: Int, shipNumber: Int): Boolean {
        if (!is3x3Empty(id, shipTable)) {
            return true
        }
        shipTable[id] = 10 + shipNumber
        return false
    }

    private fun shipGenerator(shipTable: IntArray) {
        var num: Int

        //1 ship 4-polowy
        do {
            num = Random().nextInt(100)
        } while (generateShip4(shipTable, num))

        //2 statki 3-polowe
        do {
            num = Random().nextInt(100)
        } while (generateShip3(shipTable, num, 1))
        do {
            num = Random().nextInt(100)
        } while (generateShip3(shipTable, num, 2))

        //3 statki 2-polowe
        do {
            num = Random().nextInt(100)
        } while (generateShip2(shipTable, num, 1))
        do {
            num = Random().nextInt(100)
        } while (generateShip2(shipTable, num, 2))
        do {
            num = Random().nextInt(100)
        } while (generateShip2(shipTable, num, 3))

        //4 statki 1-polowe
        do {
            num = Random().nextInt(100)
        } while (generateShip1(shipTable, num, 1))
        do {
            num = Random().nextInt(100)
        } while (generateShip1(shipTable, num, 2))
        do {
            num = Random().nextInt(100)
        } while (generateShip1(shipTable, num, 3))
        do {
            num = Random().nextInt(100)
        } while (generateShip1(shipTable, num, 4))
    }

    private fun showShips(
        whichTable: Boolean,
        shipTable: IntArray
    ) { //wyświetla wszystkie statki w danej tablicy
        if (!whichTable) {
            for (i in 0..99) {
                if (shipTable[i] > 0) {
                    val pole = findViewById<ImageView>(playerFields[i])
                    pole.setImageResource(R.drawable.ship)
                }
            }
        } else {
            for (i in 0..99) {
                if (shipTable[i] > 0) {
                    val pole = findViewById<ImageView>(computerFields[i])
                    pole.setImageResource(R.drawable.ship)
                }
            }
        }
    }

    private fun ai() { //strzelanie do pól gracza
        val elem: Int
        if (checkTable(playerShipTable)) {
            Toast.makeText(applicationContext, "Przegrałeś :(", Toast.LENGTH_LONG).show()
            deleteAllOnClicks()
            showShips(true, computerShipTable)
            return
        }

        if (whoShoots == 1) { //jeżeli tura komputera
            if (aiLastShotShipField == -1) { //strzelanie losowo
                elem = aiRandomShoot()
            } else { //strzelanie ai
                var elem1: Int
                do {
                    elem1 = aiShootShip()
                } while (elem1 == -1)
                elem = elem1
            }

            if (playerShipTable[elem] == 0) { //jeżeli pudło
                val v = findViewById<ImageView>(playerFields[elem])
                val vImageView = v as ImageView
                vImageView.setImageResource(R.drawable.miss_tile)
                playSong.playMiss(this@GameActivity)
                if (aiTwoGoodShotsSameDirection != -1) { //zawracanie ostrzału po 2 udanych i 1 nieudanym strzale w tym samym kierunku
                    when (aiTwoGoodShotsSameDirection) {
                        0 -> aiLastDirection = 2
                        1 -> aiLastDirection = 3
                        2 -> aiLastDirection = 0
                        3 -> aiLastDirection = 1
                    }
                    aiLastShotShipField = aiFirstShotShipField
                } else {
                    aiLastDirection = -1 //spudłowaliśmy, więc następny strzał ma być w inną stronę
                }
                whoShoots = 0
            } else { //if hit
                val v = findViewById<ImageView>(playerFields[elem])
                val id = Integer.parseInt(v.tag as String)
                val vImageView = v as ImageView

                aiShipFields.add(id)
                vImageView.setImageResource(R.drawable.hit)

                val tempId = playerShipTable[id]
                playerShipTable[id] = 0
                aiLastShotShipField = id
                if (aiFirstShotShipField != -1 && aiTwoGoodShotsSameDirection == -1) {
                    aiTwoGoodShotsSameDirection = aiLastDirection
                }
                if (aiFirstShotShipField == -1) { //id pierwszego trafionego punktu statku
                    aiFirstShotShipField = id
                }
                if (checkIfSunk(playerShipTable, tempId)) { //jeżeli ship zatopiony
                    aiRemoveAdjacentFields()
                    aiLastShotShipField = -1
                    aiFirstShotShipField = -1
                    aiLastDirection = -1
                    aiTwoGoodShotsSameDirection = -1
                }
                aiDirections.clear()
                aiDirections.add(0)
                aiDirections.add(1)
                aiDirections.add(2)
                aiDirections.add(3)
                playSong.playHit(this@GameActivity)
                whoShoots = 1
//                startDelay = Runnable { ai() }
                setDelay?.postDelayed(startDelay, 1250)
            }
        }
    }

    private fun aiShootShip(): Int {
        if (aiLastDirection == -1) {
            val temp = Random().nextInt(aiDirections.size)
            aiLastDirection = aiDirections[temp]
            aiDirections.removeAt(temp)
        }

        aiGoBack()

        when (aiLastDirection) {
            0 -> {
                if (checkIfExistInCoordinates(aiLastShotShipField + 1)) { //jeżeli istnieje to można strzelać
                    if (Math.floor((aiLastShotShipField / 10).toDouble()) == Math.floor(((aiLastShotShipField + 1) / 10).toDouble())) { //jeżeli nie wyjdzie do następnego wiersza
                        removeFromCoordinates(aiLastShotShipField + 1)
                        return aiLastShotShipField + 1
                    }
                }
                aiLastDirection = -1
                return -1
            }
            1 -> {
                if (checkIfExistInCoordinates(aiLastShotShipField + 10)) { //jeżeli istnieje to można strzelać
                    if (aiLastShotShipField + 10 < 100) { //jeżeli nie wyjdzie za tabelę
                        removeFromCoordinates(aiLastShotShipField + 10)
                        return aiLastShotShipField + 10
                    }
                }
                aiLastDirection = -1
                return -1
            }
            2 -> {
                if (checkIfExistInCoordinates(aiLastShotShipField - 1)) { //jeżeli istnieje to można strzelać
                    if (Math.floor((aiLastShotShipField / 10).toDouble()) == Math.floor(((aiLastShotShipField - 1) / 10).toDouble())) { //jeżeli nie wyjdzie do poprzedniego wiersza
                        removeFromCoordinates(aiLastShotShipField - 1)
                        return aiLastShotShipField - 1
                    }
                }
                aiLastDirection = -1
                return -1
            }
            3 -> {
                if (checkIfExistInCoordinates(aiLastShotShipField - 10)) { //jeżeli istnieje to można strzelać
                    if (aiLastShotShipField - 10 >= 0) { //jeżeli nie wyjdzie przed tabelę
                        removeFromCoordinates(aiLastShotShipField - 10)
                        return aiLastShotShipField - 10
                    }
                }
                aiLastDirection = -1
                return -1
            }
            else -> return -1
        }
    }

    private fun aiRandomShoot(): Int { //zwraca wylosowane pole i usuwa je z aiPlayerCoordinates
        val num = Random().nextInt(aiPlayerCoordinates.size) //wylosowanie pola
        val field = aiPlayerCoordinates[num]
        aiPlayerCoordinates.removeAt(num) //usuniecie pola z tablicy
        return field //zwrocenie pola
    }

    private fun aiGoBack() { //jeżeli wszystkie sąsiednie pola nie istnieją to wracamy do aiFirstShotShipField
        if ((!checkIfExistInCoordinates(aiLastShotShipField + 1) || Math.floor((aiLastShotShipField / 10).toDouble()) != Math.floor(
                ((aiLastShotShipField + 1) / 10).toDouble()
            )) //nie istnieje lub gdy jesteśmy przy ścianie i istnieje w następnym wierszu

            && (!checkIfExistInCoordinates(aiLastShotShipField - 1) || Math.floor((aiLastShotShipField / 10).toDouble()) != Math.floor(
                ((aiLastShotShipField - 1) / 10).toDouble()
            )) //nie istnieje lub gdy jesteśmy przy ścianie i istnieje w poprzednim wierszu

            && !checkIfExistInCoordinates(aiLastShotShipField - 10)
            && !checkIfExistInCoordinates(aiLastShotShipField + 10)
        ) {
            when (aiLastDirection) {
                0 -> aiLastDirection = 2
                1 -> aiLastDirection = 3
                2 -> aiLastDirection = 0
                3 -> aiLastDirection = 1
            }
            aiDirections.clear()
            aiDirections.add(0)
            aiDirections.add(1)
            aiDirections.add(2)
            aiDirections.add(3)
            aiLastShotShipField = aiFirstShotShipField
        }
    }

    private fun aiRemoveAdjacentFields() {
        var elem: ImageView
        for (i in aiShipFields.indices) {
            if (removeFromCoordinates(aiShipFields[i] - 10)) {
                if (playerShipTable[aiShipFields[i] - 10] == 0) {
                    elem = findViewById(playerFields[aiShipFields[i] - 10])
                    elem.setImageResource(R.drawable.miss_tile)
                }
            }
            if (Math.floor(((aiShipFields[i] - 9) / 10).toDouble()) == Math.floor(((aiShipFields[i] - 10) / 10).toDouble())) {
                if (removeFromCoordinates(aiShipFields[i] - 9)) {
                    if (playerShipTable[aiShipFields[i] - 9] == 0) {
                        elem = findViewById(playerFields[aiShipFields[i] - 9])
                        elem.setImageResource(R.drawable.miss_tile)
                    }
                }
            }
            if (Math.floor(((aiShipFields[i] + 1) / 10).toDouble()) == Math.floor((aiShipFields[i] / 10).toDouble())) {
                if (removeFromCoordinates(aiShipFields[i] + 1)) {
                    if (playerShipTable[aiShipFields[i] + 1] == 0) {
                        elem = findViewById(playerFields[aiShipFields[i] + 1])
                        elem.setImageResource(R.drawable.miss_tile)
                    }
                }
            }
            if (Math.floor(((aiShipFields[i] + 11) / 10).toDouble()) == Math.floor(((aiShipFields[i] + 10) / 10).toDouble())) {
                if (removeFromCoordinates(aiShipFields[i] + 11)) {
                    if (playerShipTable[aiShipFields[i] + 11] == 0) {
                        elem = findViewById(playerFields[aiShipFields[i] + 11])
                        elem.setImageResource(R.drawable.miss_tile)
                    }
                }
            }
            if (removeFromCoordinates(aiShipFields[i] + 10)) {
                if (playerShipTable[aiShipFields[i] + 10] == 0) {
                    elem = findViewById(playerFields[aiShipFields[i] + 10])
                    elem.setImageResource(R.drawable.miss_tile)
                }
            }
            if (Math.floor(((aiShipFields[i] + 9) / 10).toDouble()) == Math.floor(((aiShipFields[i] + 10) / 10).toDouble())) {
                if (removeFromCoordinates(aiShipFields[i] + 9)) {
                    if (playerShipTable[aiShipFields[i] + 9] == 0) {
                        elem = findViewById(playerFields[aiShipFields[i] + 9])
                        elem.setImageResource(R.drawable.miss_tile)
                    }
                }
            }
            if (Math.floor(((aiShipFields[i] - 1) / 10).toDouble()) == Math.floor((aiShipFields[i] / 10).toDouble())) {
                if (removeFromCoordinates(aiShipFields[i] - 1)) {
                    if (playerShipTable[aiShipFields[i] - 1] == 0) {
                        elem = findViewById(playerFields[aiShipFields[i] - 1])
                        elem.setImageResource(R.drawable.miss_tile)
                    }
                }
            }
            if (Math.floor(((aiShipFields[i] - 11) / 10).toDouble()) == Math.floor(((aiShipFields[i] - 10) / 10).toDouble())) {
                if (removeFromCoordinates(aiShipFields[i] - 11)) {
                    if (playerShipTable[aiShipFields[i] - 11] == 0) {
                        elem = findViewById(playerFields[aiShipFields[i] - 11])
                        elem.setImageResource(R.drawable.miss_tile)
                    }
                }
            }
        }
        aiShipFields.clear()
    }

    private fun playerRemoveAdjacentFields() {
        var elem: ImageView
        var i = 0
        while (i < shipFieldsShotByPlayer.size) {
            if (checkIfSunk(computerShipTable, shipFieldsShotByPlayerTypes[i])) {
                if (shipFieldsShotByPlayer[i] - 10 > -1) {
                    elem = findViewById(computerFields[shipFieldsShotByPlayer[i] - 10])
                    if (computerShipTable[shipFieldsShotByPlayer[i] - 10] == 0) {
                        elem.setImageResource(R.drawable.miss_tile)
                        elem.setOnClickListener(null)
                    }
                }
                if (Math.floor(((shipFieldsShotByPlayer[i] - 9) / 10).toDouble()) == Math.floor(((shipFieldsShotByPlayer[i] - 10) / 10).toDouble())) {
                    if (shipFieldsShotByPlayer[i] - 9 > -1) {
                        elem = findViewById(computerFields[shipFieldsShotByPlayer[i] - 9])
                        if (computerShipTable[shipFieldsShotByPlayer[i] - 9] == 0) {
                            elem.setImageResource(R.drawable.miss_tile)
                            elem.setOnClickListener(null)
                        }
                    }
                }
                if (Math.floor(((shipFieldsShotByPlayer[i] + 1) / 10).toDouble()) == Math.floor((shipFieldsShotByPlayer[i] / 10).toDouble())) {
                    if (shipFieldsShotByPlayer[i] + 1 < 100) {
                        elem = findViewById(computerFields[shipFieldsShotByPlayer[i] + 1])
                        if (computerShipTable[shipFieldsShotByPlayer[i] + 1] == 0) {
                            elem.setImageResource(R.drawable.miss_tile)
                            elem.setOnClickListener(null)
                        }
                    }
                }
                if (Math.floor(((shipFieldsShotByPlayer[i] + 11) / 10).toDouble()) == Math.floor(((shipFieldsShotByPlayer[i] + 10) / 10).toDouble())) {
                    if (shipFieldsShotByPlayer[i] + 11 < 100) {
                        elem = findViewById(computerFields[shipFieldsShotByPlayer[i] + 11])
                        if (computerShipTable[shipFieldsShotByPlayer[i] + 11] == 0) {
                            elem.setImageResource(R.drawable.miss_tile)
                            elem.setOnClickListener(null)
                        }
                    }
                }
                if (shipFieldsShotByPlayer[i] + 10 < 100) {
                    elem = findViewById(computerFields[shipFieldsShotByPlayer[i] + 10])
                    if (computerShipTable[shipFieldsShotByPlayer[i] + 10] == 0) {
                        elem.setImageResource(R.drawable.miss_tile)
                        elem.setOnClickListener(null)
                    }
                }
                if (Math.floor(((shipFieldsShotByPlayer[i] + 9) / 10).toDouble()) == Math.floor(((shipFieldsShotByPlayer[i] + 10) / 10).toDouble())) {
                    if (shipFieldsShotByPlayer[i] + 9 < 100) {
                        elem = findViewById(computerFields[shipFieldsShotByPlayer[i] + 9])
                        if (computerShipTable[shipFieldsShotByPlayer[i] + 9] == 0) {
                            elem.setImageResource(R.drawable.miss_tile)
                            elem.setOnClickListener(null)
                        }
                    }
                }
                if (Math.floor(((shipFieldsShotByPlayer[i] - 1) / 10).toDouble()) == Math.floor((shipFieldsShotByPlayer[i] / 10).toDouble())) {
                    if (shipFieldsShotByPlayer[i] - 1 > -1) {
                        elem = findViewById(computerFields[shipFieldsShotByPlayer[i] - 1])
                        if (computerShipTable[shipFieldsShotByPlayer[i] - 1] == 0) {
                            elem.setImageResource(R.drawable.miss_tile)
                            elem.setOnClickListener(null)
                        }
                    }
                }
                if (Math.floor(((shipFieldsShotByPlayer[i] - 11) / 10).toDouble()) == Math.floor(((shipFieldsShotByPlayer[i] - 10) / 10).toDouble())) {
                    if (shipFieldsShotByPlayer[i] - 11 > -1) {
                        elem = findViewById(computerFields[shipFieldsShotByPlayer[i] - 11])
                        if (computerShipTable[shipFieldsShotByPlayer[i] - 11] == 0) {
                            elem.setImageResource(R.drawable.miss_tile)
                            elem.setOnClickListener(null)
                        }
                    }
                }
                shipFieldsShotByPlayer.removeAt(i)
                shipFieldsShotByPlayerTypes.removeAt(i--)
            }
            i++
        }
    }

    private fun checkTable(table: IntArray): Boolean { //sprawdza czy tablica ma tylko zera
        for (i in table) {
            if (i > 0) {
                return false
            } //ma jakieś liczby
        }
        return true //ma tylko zera
    }

    private fun checkIfSunk(
        shipTable: IntArray,
        id: Int
    ): Boolean { //sprawdza czy ship o ID zostal zatopiony
        var i = 0
        while (i < 100) {
            if (shipTable[i++] == id) {
                return false
            }
        }
        return true
    }

    private fun checkIfExistInCoordinates(id: Int): Boolean { //sprawdza czy pole o danym id istnieje w aiPlayerCoordinates
        var i = 0
        while (i < aiPlayerCoordinates.size) {
            if (aiPlayerCoordinates[i++] == id) {
                return true
            }
        }
        return false
    }

    private fun removeFromCoordinates(id: Int): Boolean { //usuwa pole o danym id z aiPlayerCoordinates
        for (i in aiPlayerCoordinates.indices) {
            if (aiPlayerCoordinates[i] == id) {
                aiPlayerCoordinates.removeAt(i)
                return true
            }
        }
        return false
    }
}
